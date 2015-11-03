package server.network;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Deque;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

public class Client {
    private SocketChannel channel;
    
    private Queue<Packet> outgoingData;
    private Deque<Byte> incomingData;
    
    public Client(SocketChannel channel) {
        this.channel = channel;
        
        outgoingData = new LinkedBlockingQueue<>(16);
        incomingData = new LinkedBlockingDeque<>(4096);
    }
    
    public void read(SelectionKey key){
        
    }
    
    public void write(SelectionKey key){
        
    }
    
    
}
