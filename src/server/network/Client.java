package server.network;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Deque;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

import braynstorm.commonlib.Logger;

public class Client {
    private SocketChannel channel;
    
    private Queue<Packet> outgoingData;
    private Deque<Byte> incomingData;
    
    private ByteBuffer tempReadBuffer = ByteBuffer.allocate(1024);
    private ByteBuffer headReader = ByteBuffer.allocate(Packet.HEADER_SIZE);
    private ByteBuffer dataReader = ByteBuffer.allocate(1024);
    
    public Client(SocketChannel channel) {
        this.channel = channel;
        
        outgoingData = new LinkedBlockingQueue<>(16);
        incomingData = new LinkedBlockingDeque<>(4096);
    }
    
    public void sendPacket(Packet packet){
        outgoingData.add(packet);
    }
    
    public boolean hasPacketsToRead(SocketChannel from){
        return incomingData.size() >= Packet.HEADER_SIZE;
    }
    
    /**
     * @param from The client who's packets to read.
     * @return a ByteBuffer if there was an unprocessed packet. Null otherwise.
     */
    public boolean tryProcessPacket(){
        if(incomingData.size() > Packet.HEADER_SIZE){
            // Get the first 4 bytes of the Deque
            headReader.put(incomingData.pollFirst());
            headReader.put(incomingData.pollFirst());
            headReader.put(incomingData.pollFirst());
            headReader.put(incomingData.pollFirst());
            headReader.flip();
            
            short type = headReader.getShort();
            short size = headReader.getShort();
            
            // and check if it has enough to contruct a packet.
            if(incomingData.size() >= size){
                
                for(int i = 0; i < size; i++){
                    dataReader.put(incomingData.pollFirst());
                }
                
                dataReader.flip();
                PacketManager.forwardPacket(this, type, dataReader);
                
                return true;
            }else{
                // Return all the bytes to the queue.
            	incomingData.addFirst(headReader.get(3));
            	incomingData.addFirst(headReader.get(2));
                incomingData.addFirst(headReader.get(1));
                incomingData.addFirst(headReader.get(0));
                
            }
        }
        
        headReader.clear();
        dataReader.clear();
        return false;
    }
    
    void read(SelectionKey key){
        try {
            if(channel.read(tempReadBuffer) == 0)
                return;
            
            tempReadBuffer.flip();
            
            while(tempReadBuffer.hasRemaining())
                incomingData.add(tempReadBuffer.get());
            
            while(tryProcessPacket());
            
        } catch (IOException e) {
            Logger.logExceptionInfo(e);
            close();
        }
        
        // Check for amount of data read.
        key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        
        tempReadBuffer.clear();
    }
    
    void write(SelectionKey key){
		if(outgoingData.isEmpty())
		    return;
		
		try {
		    channel.write(outgoingData.poll().getData());
		} catch (IOException e) {
		    Logger.logExceptionInfo(e);
		    close();
		}
		
		key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
    }
    
    public boolean isClosed(){
    	return channel.socket().isClosed();
    }
    
    public void close(){
    	if(!isClosed()){
	    	try {
	    		channel.socket().close();
				channel.close();
			} catch (IOException e) {
				Logger.logExceptionInfo(e);
			}
    	}
    }
}
