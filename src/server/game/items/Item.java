package server.game.items;

import java.util.ArrayList;
import java.util.List;

import server.game.entities.EntityLiving;
import server.game.spells.Spell;

public abstract class Item {
    protected final int id;
    protected final int metadata;
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
    
    public Item(int id, int metadata, byte maxStackSize) {
        this.id = id;
        this.metadata = metadata;
        this.maxStackSize = maxStackSize;
        
        castOnAqquire = new ArrayList<>();
        castOnEquip = new ArrayList<>();
    }

    /**
     * Called after equpping the item.
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
    
    
    
}
