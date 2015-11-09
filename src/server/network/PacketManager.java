package server.network;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import braynstorm.commonlib.Common;
import braynstorm.commonlib.Logger;
import braynstorm.commonlib.math.Vector3f;
import braynstorm.commonlib.network.PacketType;
import server.core.Main;
import server.core.db.AccountDoesntExistException;
import server.core.db.AccountIsSuspendedException;
import server.core.db.WrongPasswordException;
import server.game.entities.Player;
import server.game.spells.Spell;

public class PacketManager {
    private static int temp;
    
    private static ByteBuffer packetConfirm;
    private static ByteBuffer packetReject;
    
    public static void forwardPacket(Client client, short opcode, ByteBuffer data){
        Logger.logInfo("Forwarding packet: " + Integer.toHexString(opcode) + " :");
        Logger.logInfo(data.array());
        
        Player player = null;
        
        if(client.getAccount() != null){
        	player = client.getAccount().getPlayerCharacter();
        }
        
        switch(opcode){
            case PacketType.PING:
                // TODO Limit the number of pingPackets for a given user per second.
                client.sendPacket(Packet.createPingPacket());
                break;
            
            case PacketType.LOGIN_ATTEMPT:
                StringBuilder sbEmail = new StringBuilder(256); 
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
                    
                    client.getAccount().getCharacterList().forEach(character -> {
                        ByteBuffer charData = character.getData();
                        temp += charData.capacity();
                        charactersData.add(charData);
                    });
                    
                    packet = Common.createPacket(PacketType.LOGIN_STATUS, 1 + 1 + temp);
                    packet.put((byte) 1); // Status
                    packet.put((byte) charactersData.size());
                    
                    for(ByteBuffer charData : charactersData){
                    	packet.put(charData);
                    }
                    
                } catch (AccountIsSuspendedException e) {
                    Logger.logInfo(e);
                    packet = Common.createPacket(PacketType.LOGIN_STATUS, 1 + Integer.BYTES);
                    
                    // Status
                    packet.put((byte) 2);
                    
                    // Suspension left
                    packet.putLong((System.currentTimeMillis() - e.getSuspendedUntil().getTime()) / 1000);
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
                }
                
                client.sendPacket(packet);
                break;
                
            case PacketType.ENTITY_MOTION_UPDATE:
            	if(player == null){
            		Logger.logWarning("ENTITY_MOTION_UPDATE packet recieved form an account that hasn't chosen a character. SKIPPING.");
            		break;
            	}
            	
            	player.setIsInMotion(data.get() == 0 ? false : true);
            	player.setPosition(Vector3f.readFromBuffer(data));
            	player.setForward(Vector3f.readFromBuffer(data));
            	player.setUp(Vector3f.readFromBuffer(data));
            	
            	break;
            case PacketType.ENTITY_STARTED_CASTING_SPELL:
            	if(player == null){
            		Logger.logWarning("ENTITY_STARTED_CASTING_SPELL packet recieved form an account that hasn't chosen a character. SKIPPING.");
            		break;
            	}
            	
            	Spell
            	
            	//if(player.isInMotion())
            		//rejectPacket(client);
            	
            	
            	break;
        }
    }
    
    public static void confirmPacket(Client client){
    	client.sendPacket(packetConfirm);
    }
    
    public static void rejectPacket(Client client){
    	client.sendPacket(packetReject);
    }

	public static void init() {
		packetReject = Common.createPacket(PacketType.LAST_PACKET_STATUS, 1);
		packetReject.put((byte) 0);
		packetConfirm = Common.createPacket(PacketType.LAST_PACKET_STATUS, 1);
		packetConfirm.put((byte) 1);
	}
}
