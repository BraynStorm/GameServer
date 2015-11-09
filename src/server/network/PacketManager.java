package server.network;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import braynstorm.commonlib.Common;
import braynstorm.commonlib.Logger;
import braynstorm.commonlib.network.PacketSize;
import braynstorm.commonlib.network.PacketType;
import server.core.Main;
import server.core.db.AccountDoesntExistException;
import server.core.db.AccountIsSuspendedException;
import server.core.db.WrongPasswordException;

public class PacketManager {
    private static int temp;
    public static void forwardPacket(Client client, short opcode, ByteBuffer data){
        Logger.logInfo("Forwarding packet: " + Integer.toHexString(opcode) + " :");
        Logger.logInfo(data.array());
        
        switch(opcode){
            case PacketType.PING:
                // TODO Limit the number of pingPackets for a given user per second.
                client.sendPacket(Packet.createPingPacket());
                break;
            
            case PacketType.LOGIN_ATTEMPT:
                StringBuilder sbEmail = new StringBuilder(); 
                StringBuilder sbPass = new StringBuilder(61); // Password is 60 characters long.
                
                for(int i = 0; data.hasRemaining() && i < 350; i++)
                    if(i < 60)
                        sbPass.append(data.getChar());
                    else
                        sbEmail.append(data.getChar());
                
                ByteBuffer packet;
                
                try {
                    client.setAccount(Main.getDatabase().fetchAccount(sbEmail.toString(), sbPass.toString()));
                    
                    
                    temp = 0;
                    ArrayList<ByteBuffer> charactersData = new ArrayList<>();
                    
                    client.getAccount().getCharacterList().forEach(character ->{
                        ByteBuffer charData = character.getData();
                        temp += charData.capacity();
                        charactersData.add(charData);
                    });
                    
                    packet = Common.createPacket(PacketType.LOGIN_STATUS, 1 + 1 + temp)
                        .put((byte) 1) // Status
                        .put((byte) charactersData.size());
                    
                    for(ByteBuffer charData : charactersData){
                    	packet.put(charData);
                    }
                    
                } catch (WrongPasswordException e) {
                    Logger.logInfo("Wrong password attempt from " + client.getAddress().toString());
                    packet = Common.createPacket(PacketType.LOGIN_STATUS, 1);
                    
                    // Status
                    packet.put((byte) 3);
                } catch (AccountDoesntExistException e) {
                    Logger.logInfo(e);
                    packet = Common.createPacket(PacketType.LOGIN_STATUS, 1);
                    
                    // Status
                    packet.put((byte) 3);
                } catch (AccountIsSuspendedException e) {
                    Logger.logInfo(e);
                    packet = Common.createPacket(PacketType.LOGIN_STATUS, 1 + Integer.BYTES);
                    
                    // Status
                    packet.put((byte) 2);
                    
                    // Suspension left
                    packet.putLong((System.currentTimeMillis() - e.getSuspendedUntil().getTime()) / 1000);
                }
                
                if(packet == null){
                    // Something fucked up...
                	Logger.logImpossibru("Packet is NULL");
                	break;
                }
                
                client.sendPacket(packet);
                break;
        }
    }
    
}
