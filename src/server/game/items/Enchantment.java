package server.game.items;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * DB enchantments:
 * 		enchantID,
 * 		auraID
 * 
 * @author Braynstorm
 */
public class Enchantment {
    public static final int BYTES = Integer.BYTES;
    protected static Map<Integer, Enchantment> enchantments = new HashMap<Integer, Enchantment>();
	
	protected final int enchantID;
	protected final int auraID;
	protected final int maxDuration;
	
	public Enchantment(int enchantID, int auraID, int maxDuration) {
		this.enchantID = enchantID;
		this.maxDuration = maxDuration;
		this.auraID = auraID;
	}

	public static Enchantment getEnchantByID(int id){
		
		if(!enchantments.containsKey(id))
			throw new EnchantmentDoesntExistException(id);
		
		return enchantments.get(id);
	}

    public ByteBuffer getData() {
        return ByteBuffer.allocate(BYTES).putInt(enchantID);
    }
}
