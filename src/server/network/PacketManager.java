package server.network;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import braynstorm.commonlib.Logger;
import server.core.Main;

public class PacketManager {
    
    public static void forwardPacket(SocketChannel source, short opcode, ByteBuffer data){
        Logger.logInfo("Forwarding packet: " + Integer.toHexString(opcode) + " :");
        Logger.logInfo(data.array());
        switch(opcode){
            case OPCodes.PING:
                // TODO Wrap the SocketChannel in a Client class, with more sophisticated methods and.
                // TODO Limit the number of pingPackets for a given user per second.
                Main.getServer().sendPacket(source, Packet.createPingPacket());
                break;
        }
    }
    
}
