package server.network;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import braynstorm.commonlib.entities.Entity;
import braynstorm.commonlib.math.Vector3f;
import braynstorm.commonlib.network.PacketType;

public class Packet {
    
    /**
     * short OPCODE;
     * short lenght;
     * byte[] data;
     */
    public static final int HEADER_SIZE = Short.BYTES * 2;
    
    protected ByteBuffer data;
    
    public Packet(ByteBuffer fullData) {
        this.data = fullData;
    }
    
    public void writeToChannel(SocketChannel channel) throws IOException{
        channel.write(data);
    }
    
    public static ByteBuffer allocateFor(int bytes, int shorts, int ints, int longs){
        return ByteBuffer.allocate(bytes * Byte.BYTES + shorts * Short.BYTES + ints * Integer.BYTES + longs * Long.BYTES);
    }
    
    public static ByteBuffer allocatePacket(int size){
        return allocateFor(size, 2, 0, 0);
    }
    
    public static Packet createPingPacket(){
        ByteBuffer buffer = allocatePacket(Long.BYTES);
        
        buffer.putShort(PacketType.PING);
        buffer.putShort((short) Long.BYTES);
        buffer.putLong(System.currentTimeMillis());
        
        return new Packet(buffer);
    }
    
    public static Packet createLoginStatusPacket(byte status){
        ByteBuffer buffer = allocatePacket(Short.BYTES);
        
        buffer.putShort(PacketType.LOGIN_STATUS);
        buffer.putShort((short) 1);
        buffer.put(status);
        
        return new Packet(buffer);
    }
    
    public static Packet createEntityUpdatePacket(Entity entity){
        short size = Integer.SIZE + 1 + Vector3f.BYTES * 3;
        
        ByteBuffer buffer = allocatePacket(size);
        
        // Header
        buffer.putShort(PacketType.ENTITY_MOTION_UPDATE);
        buffer.putShort(size);
        
        // Data
        buffer.putInt(entity.getDisplayID());
        buffer.put((byte) (entity.isVisible() ? 1 : 0));
        buffer.put(entity.getPosition().getByteBuffer());
        buffer.put(entity.getForward().getByteBuffer());
        buffer.put(entity.getUp().getByteBuffer());
        
        return new Packet(buffer);
    }

    public ByteBuffer getData() {
        return data;
    }
    
    
    
}
