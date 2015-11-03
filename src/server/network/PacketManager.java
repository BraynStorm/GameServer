package server.network;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import braynstorm.commonlib.Logger;
import server.core.Main;

public class PacketManager {
    
    public static void forwardPacket(Client client, short opcode, ByteBuffer data){
        Logger.logInfo("Forwarding packet: " + Integer.toHexString(opcode) + " :");
        Logger.logInfo(data.array());
        switch(opcode){
            case OPCodes.PING:
                // TODO Limit the number of pingPackets for a given user per second.
                client.sendPacket(Packet.createPingPacket());
                break;
        }
    }
    
}
