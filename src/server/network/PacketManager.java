package server.network;

import java.nio.ByteBuffer;
import braynstorm.commonlib.Logger;

public class PacketManager {
    
    public static void forwardPacket(Client client, short opcode, ByteBuffer data){
        Logger.logInfo("Forwarding packet: " + Integer.toHexString(opcode) + " :");
        Logger.logInfo(data.array());
        
        switch(opcode){
            case PacketType.PING:
                // TODO Limit the number of pingPackets for a given user per second.
                client.sendPacket(Packet.createPingPacket());
                break;
            case PacketType.LOGIN_ATTEMPT:
                
                break;
            case PacketType.LOGIN_STATUS:
                
                break;
        }
    }
    
}
