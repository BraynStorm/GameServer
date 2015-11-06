package server.game.items;

import java.util.List;

public class ItemStack {
    protected Item item;
    protected List<EnchantmentStack> enchant;
    protected int amount;
    
    public ItemStack(Item item){
        this(item, 1);
    }
    
    public ItemStack(Item item, int amount) {
        this.item = item;
        this.amount = amount;
    }
    
    
    
    public ItemStack(int id, int amount) {
		this(Item.getItemByID(id), amount);
	}

	public Item getItem() {
        return item;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
    
    
}
