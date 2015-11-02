package server.game.items;

public class ItemStack {
    protected Item item;
    protected int amount;
    
    public ItemStack(Item item){
        this(item, 1);
    }
    
    public ItemStack(Item item, int amount) {
        this.item = item;
        this.amount = amount;
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
