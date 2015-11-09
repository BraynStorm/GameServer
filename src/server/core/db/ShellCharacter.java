package server.core.db;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import braynstorm.commonlib.math.Vector3f;
import server.core.Main;
import server.game.items.ItemStack;

/**
 * Serves as a 'shell' for the character list that the client requests.
 * @author Braystorm
 *
 */
public class ShellCharacter {
	private final String name;
	private final Vector3f location;
	private final int raceData;
	private final Map<Character, ItemStack> equipment;
	
	public ShellCharacter(String name, Vector3f location, int raceData, Map<Character, ItemStack> equipment) {
		this.name = name;
		this.location = location;
		this.raceData = raceData;
		this.equipment = equipment;
	}
	
	public String getName(){
	    return name;
	}
	
	public Vector3f getLocation(){
        return location;
	}
	
    public int getRaceData() {
        return raceData;
    }
   
    public Map<Character, ItemStack> getEquipment() {
        return equipment;
    }
    
    public ByteBuffer getData(){
    	HashMap<Character, ByteBuffer> equipmentData = new HashMap<>();
    	int equipmentSize = 0;
    	
    	for(Entry<Character, ItemStack> entry : equipment.entrySet()){
    		ByteBuffer tempBuffer = entry.getValue().getData();
    		equipmentSize += tempBuffer.capacity();
    		equipmentData.put(entry.getKey(), tempBuffer);
    	}
    	
    	String zoneNameStr = Main.getWorld().getZone(location).toString();
    	byte[] name = this.name.getBytes();
    	byte[] zoneName = zoneNameStr.getBytes();
    	
        ByteBuffer data = ByteBuffer.allocate(equipmentSize + zoneName.length+ Integer.BYTES + name.length + 2 * Short.BYTES);
        
        data.putInt(raceData);
        data.putShort((short)this.name.length());
        data.putShort((short)zoneNameStr.length());
        data.put(name);
        data.put(zoneName);
        
        data.put((byte) equipmentData.size());
        
        for(Entry<Character, ByteBuffer> entry : equipmentData.entrySet()){
        	data.put(entry.getValue());
        }
        
        data.flip();
        return data;
    }
	
}
