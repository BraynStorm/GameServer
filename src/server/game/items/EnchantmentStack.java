package server.game.items;

import java.nio.ByteBuffer;

/*
 * DB itemstack_enchants:
 * 		itemStackID,
 * 		enchantID,
 * 		timeLeft (< 0 means inf.)
 * 
 * @author User8
 *
 */
public class EnchantmentStack {
    public static final int BYTES = Integer.BYTES + Enchantment.BYTES;
	protected Enchantment enchantment;
	protected int timeLeft;
	
    public ByteBuffer getData() {
        
        ByteBuffer data = ByteBuffer.allocate(BYTES);
        data.put(enchantment.getData());
        data.putInt(timeLeft);
        
        return data;
    }
}
