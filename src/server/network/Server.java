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
    
    private Map<SocketChannel, Client> clients = new ConcurrentHashMap<>();
    
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
                	clients.get(key.channel()).write(key);
                }
                
                if(key.isReadable()){
                	clients.get(key.channel()).read(key);
                }
            }
        }
        
        closeServer();
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
    
    private void accept(SelectionKey key) {
        ServerSocketChannel channel = (ServerSocketChannel) key.channel();
        try {
            SocketChannel socketChannel = channel.accept();
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
            
            clients.put(socketChannel, new Client(socketChannel));
            
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
