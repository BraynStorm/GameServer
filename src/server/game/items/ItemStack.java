package server.game.items;

import java.nio.ByteBuffer;
import java.util.List;

public class ItemStack {
    protected Item item;
    protected List<EnchantmentStack> enchant;
    protected byte amount;
    
    public ItemStack(Item item){
        this(item, (byte)1);
    }
    
    public ItemStack(Item item, byte amount) {
        this.item = item;
        this.amount = amount;
    }
    
    public ItemStack(int id) {
        this(id, (byte)1);
    }
    
    public ItemStack(int id, byte amount) {
		this(Item.getItemByID(id), amount);
	}

	public Item getItem() {
        return item;
    }

    public int getAmount() {
        return amount;
    }

    /**
     * @param amount Ubyte
     */
    public void setAmount(byte amount) {
        this.amount = amount;
    }
    
    public void setAmount(int amount) {
        setAmount((byte) amount);
    }
    
    public ByteBuffer getData(){
        ByteBuffer data = ByteBuffer.allocate(getByteCount());
        data.put(item.getData());
        
        enchant.forEach(ench -> {
            data.put(ench.getData());
        });
        
        data.put(amount);
        data.flip();
        
        return data;
    }
    
    public int getByteCount(){
        return Integer.BYTES + Item.BYTES + enchant.size() * Enchantment.BYTES;
    }
}
