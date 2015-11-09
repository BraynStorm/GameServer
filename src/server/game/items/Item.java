package server.game.items;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import server.game.entities.EntityLiving;
import server.game.spells.Spell;

public abstract class Item {
    public static final int BYTES = Integer.BYTES * 2;
	protected static Map<Integer, Item> items = new HashMap<Integer,Item>();
	
    protected final int id;
    protected final short metadata;
    protected final byte maxStackSize;
    
    protected List<Spell> castOnAqquire;
    protected List<Spell> castOnEquip;
    
    public int getId() {
        return id;
    }

    public int getMetadata() {
        return metadata;
    }

    public byte getMaxStackSize() {
        return maxStackSize;
    }
    
    public Item(int id, short metadata, byte maxStackSize) {
        this.id = id;
        this.metadata = metadata;
        this.maxStackSize = maxStackSize;
        
        castOnAqquire = new ArrayList<>();
        castOnEquip = new ArrayList<>();
    }

    /**
     * Called after equipping the item.
     */
    public void afterEquipItem(EntityLiving entity){
        castOnEquip.forEach(s -> {
            s.cast(entity, entity);
        });
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        result = prime * result + metadata;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Item other = (Item) obj;
        if (id != other.id)
            return false;
        if (metadata != other.metadata)
            return false;
        return true;
    }
    
	public static Item getItemByID(int id) {
		if(!items.containsKey(id))
			throw new ItemDoesntExistException(id);
		
		return items.get(id);
	}

    public ByteBuffer getData() {
        ByteBuffer data = ByteBuffer.allocate(Integer.BYTES + Short.BYTES);
        data.putInt(id);
        data.putShort(metadata);
        return data;
    }
    
    
    
}
