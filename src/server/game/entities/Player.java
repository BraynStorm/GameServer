package server.game.entities;

import java.nio.ByteBuffer;

import server.core.db.ShellCharacter;
import server.network.Client;

public class Player extends EntityLiving {
    
    private Client client;
    
    public Player(Client client, ShellCharacter shell) {
        super(shell.getRaceData(), shell.getLocation());
        this.client = client;
        this.equipment = shell.getEquipment();
    }
    
    public void savePlayer(){
        /* TODO save all the data into the DB.
         * Location, Equipment, Inventory, 
         * Cooldowns, Exp, ....
         */
        
        throw new RuntimeException();
    }
    
    public void sendPacket(ByteBuffer packet) {
        client.sendPacket(packet);
    }

	
}
