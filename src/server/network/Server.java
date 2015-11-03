package server.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Deque;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

import braynstorm.commonlib.Logger;
import server.core.Config;

public class Server implements Runnable {
    
    private ServerSocketChannel serverChannel;
    private Selector selector;
    
    @Deprecated private Map<SocketChannel, Queue<Packet>> toWriteData = new ConcurrentHashMap<>();
    @Deprecated private Map<SocketChannel, Deque<Byte>> readData = new ConcurrentHashMap<>();
    
    private Set<Client> clients = new ConcurrentSkipListSet<>();
    
    private ByteBuffer tempReadBuffer = ByteBuffer.allocate(1024);
    private ByteBuffer headReader = ByteBuffer.allocate(Packet.HEADER_SIZE);
    private ByteBuffer dataReader = ByteBuffer.allocate(1024);
    
    private void init(){
        try {
            selector = Selector.open();
            serverChannel = ServerSocketChannel.open();
            serverChannel.configureBlocking(false);
            serverChannel.socket().bind(new InetSocketAddress("127.0.0.1", Config.getValuei("serverPort")));
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
            
            Logger.logInfo("Started server " + serverChannel.getLocalAddress().toString());
        } catch (IOException e) {
            Logger.logExceptionCritical(e);
        }
    }
    
    
    @Override
    public void run() {
        init();
        
        while(!Thread.currentThread().isInterrupted()){
            // Wait up to 1 sec for a select.
            try{ selector.select(1000); } catch (IOException e){ continue; }
            
            Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
            
            while(keys.hasNext()){
                SelectionKey key = keys.next();
                keys.remove();
                
                if(!key.isValid()){
                    // TODO: disconnect.
                    Logger.logInfo("Client disconnected. (Invalid SelectionKey)");
                    continue;
                }
                
                if(key.isAcceptable()){
                    Logger.logInfo("Accepting client connection");
                    accept(key);
                }
                
                if(key.isWritable()){
                    //Logger.logInfo("Writing to client connection");
                    write(key);
                }
                
                if(key.isReadable()){
                    //Logger.logInfo("Reading from client connection");
                    read(key);
                }
            }
        }
        
        closeServer();
    }
    
    public void sendPacket(SocketChannel to, Packet packet){
        toWriteData.get(to).add(packet);
    }
    
    public boolean hasPacketsToRead(SocketChannel from){
        return !readData.get(from).isEmpty();
    }
    
    /**
     * @param from The client who's packets to read.
     * @returna ByteBuffer if there was an unprocessed packet. Null otherwise.
     */
    public boolean tryReadNextPacket(SocketChannel from){
        Deque<Byte> bytes = readData.get(from);
        
        if(bytes.size() > Packet.HEADER_SIZE){
            // Get the first 4 bytes of the Deque
            headReader.put(bytes.pollFirst());
            headReader.put(bytes.pollFirst());
            headReader.put(bytes.pollFirst());
            headReader.put(bytes.pollFirst());
            headReader.flip();
            
            short type = headReader.getShort();
            short size = headReader.getShort();
            
            // and check if it has enough to contruct a packet.
            if(bytes.size() >= size){
                
                for(int i = 0; i < size; i++){
                    dataReader.put(bytes.pollFirst());
                }
                
                dataReader.flip();
                PacketManager.forwardPacket(from, type, dataReader);
                dataReader.clear();
                return true;
            }else{
                // Return all the bytes to the queue.
                bytes.addFirst(headReader.get(3));
                bytes.addFirst(headReader.get(2));
                bytes.addFirst(headReader.get(1));
                bytes.addFirst(headReader.get(0));
                headReader.clear();
            }
        }
        
        return false;
    }
    
    @Override
    protected void finalize() throws Throwable {
        closeServer();
        super.finalize();
    }
    
    private void closeServer(){
        try {
            selector.close();
            serverChannel.socket().close();
            serverChannel.close();
        } catch (IOException e) {
            Logger.logExceptionWarning(e);
        }
        Thread.currentThread().interrupt();
    }
    
    private void closeChannel(SocketChannel channel){
        try {
            channel.socket().close();
            channel.close();
            toWriteData.remove(channel);
        } catch (IOException e) {}
    }
    
    private void read(SelectionKey key) {
        SocketChannel channel = (SocketChannel) key.channel();
        
        try {
            int readAmount = channel.read(tempReadBuffer);
            if(readAmount == 0)
                return;
            tempReadBuffer.flip();
            
            Deque<Byte> unprocessedData = readData.get(channel);
            
            while(tempReadBuffer.hasRemaining())
                unprocessedData.add(tempReadBuffer.get());
            
            tempReadBuffer.clear();
            
            while( tryReadNextPacket(channel) );
            
        } catch (IOException e) {
            Logger.logExceptionInfo(e);
            closeChannel(channel);
        }
        
        // Check for amount of data read.
        key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
    }
    

    private void write(SelectionKey key) {
        SocketChannel channel = (SocketChannel) key.channel();
        
        Queue<Packet> dataQueue = toWriteData.get(channel);
        
        if(dataQueue.isEmpty())
            return;
        
        try {
            channel.write(dataQueue.poll().getData());
        } catch (IOException e) {
            Logger.logExceptionInfo(e);
            closeChannel(channel);
        }
        
        key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
    }


    private void accept(SelectionKey key) {
        ServerSocketChannel channel = (ServerSocketChannel) key.channel();
        try {
            SocketChannel socketChannel = channel.accept();
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
            
            toWriteData.put(socketChannel, new LinkedBlockingQueue<Packet>(16));
            readData.put(socketChannel, new LinkedBlockingDeque<Byte>(4096));
            
            Logger.logInfo("Accepted connection "+ socketChannel.getRemoteAddress().toString());
        } catch (IOException e) {
            try {
                channel.socket().close();
                channel.close();
            } catch (IOException e1) { Logger.logDebug("\b[Networking] WTF"); Logger.logExceptionDebug(e1);}
            
            Logger.logExceptionWarning(e);
        }
        
        
    }
    
}
