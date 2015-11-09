package server.game.entities;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
//import java.util.concurrent.ConcurrentHashMap;



import braynstorm.commonlib.Common;
import braynstorm.commonlib.math.Vector3f;
import braynstorm.commonlib.network.PacketSize;
import braynstorm.commonlib.network.PacketType;
import server.core.events.EntityTickEvent;
import server.game.CalculatableStats;
import server.game.BaseStats;
import server.game.auras.Aura;
import server.game.items.ItemStack;
import server.game.powers.Health;
import server.game.powers.Power;

public class EntityLiving extends EntityTicking {
    
    public static final int EQUIPMENT_SLOTS_COUNT = 33;
	
    protected String name;
    
    protected Health hp;
    protected Power power;
    protected BaseStats baseStats;
    protected CalculatableStats calculatableStats;
    
    protected boolean isInMotion;
    
    /**
     * 0-helm
     * 1-shoulder
     * 2-cloak
     * 3-chest
     * 4-wrist
     * 5-gloves
     * 6-belt
     * 7-pants
     * 8-boots
     * 9-19 fingers
     * 20-25 trinkets
     * 26-32 weapons
     */
    protected Set<Aura> auras;
    protected Map<Character, ItemStack> equipment;
    protected Map<Character, ItemStack> inventory;
    
    protected short inventorySize = 22;
    
    public EntityLiving(String name, int displayID, Vector3f position){
        super(displayID, position, true);
        this.name = name;
        this.auras = new HashSet<>();//Collections.newSetFromMap(new ConcurrentHashMap<Aura, Boolean>());
        this.inventory = new HashMap<>(); // TODO Make Concurrent
        this.equipment = new HashMap<>(); // TODO Make Concurrent
        this.baseStats = new BaseStats(this);
        this.calculatableStats = new CalculatableStats(this);
        dirtyFlags = new Dirtyness();
    }
    
    @Override
    public void tick(EntityTickEvent event) {
        if(isInMotion){
        	move(forward, calculatableStats.getStat("movement_speed"));
        }
    }
    
    public void addAura(Aura aura){ auras.add(aura); }
    
    public boolean hasAura(Aura aura) {
        return auras.contains(aura);
    }
    
    public boolean removeAura(Aura aura){
        return auras.remove(aura);
    }
    
    public boolean hasItemInEquipmentSlot(int slot){
        return equipment.containsKey(slot);
    }
    
    public ItemStack equipItem(char slot, ItemStack itemStack){
        return equipment.put(slot, itemStack);
    }
    
    public boolean addItemToInventory(char slotID, ItemStack itemStack){
        if(inventory.containsKey(slotID)) {
            ItemStack stackInSlot = inventory.get(slotID);
            int maxStackSize = itemStack.getItem().getMaxStackSize();
            
            if(stackInSlot.getItem().equals(itemStack.getItem()) && stackInSlot.getAmount() != stackInSlot.getItem().getMaxStackSize()) {
                int newStackSize = (stackInSlot.getAmount() + itemStack.getAmount());
                
                if(newStackSize <= maxStackSize) {
                    itemStack.setAmount(0);
                    stackInSlot.setAmount(newStackSize);
                    return true;
                }
                
                itemStack.setAmount(newStackSize - maxStackSize);
                stackInSlot.setAmount(maxStackSize);
            } else {
                return false;
            }
        } else {
            inventory.put(slotID, itemStack);
            return true;
        }
        
        return true;
    }
    
    public ByteBuffer getPacketMotionUpdate(){
        ByteBuffer packet = Common.createPacket(PacketType.ENTITY_MOTION_UPDATE, PacketSize.ENTITY_MOTION_UPDATE);
        
        packet.put(isInMotion ? (byte) 1 : 0);
        packet.put(position.getByteBuffer());
        packet.put(forward.getByteBuffer());
        packet.put(up.getByteBuffer());
        
        packet.flip();
        return packet;
    }
    
    public boolean isInMotion(){ return isInMotion; }
    public boolean isDead(){ return hp.isZero(); }
    
    public float getCurrentHP(){ return hp.getCurrent(); }
    public float getMaxHP(){ return hp.getMax(); }
    public float getPower(){ return power.getCurrent(); }
    public float getMaxPower(){ return power.getMax(); }
    
    public void setHP(float hp){ this.hp.setCurrent(hp); markDirty(Dirtyness.POWERS); }
    public void setPower(float power){ this.power.setCurrent(power); markDirty(Dirtyness.POWERS); }
    public void setMaxHP(float maxHP){ this.hp.setMax(maxHP); markDirty(Dirtyness.POWERS);}
    public void setMaxPower(float maxPower){ this.power.setMax(maxPower); markDirty(Dirtyness.POWERS); }
    
    public void setIsInMotion(boolean v){ this.isInMotion = v; }
    
    public Health getHealth() {
        return hp;
    }
    
    public BaseStats getBaseStats() {
        return baseStats;
    }
    
    public CalculatableStats getCalculatedStats() {
        return calculatableStats;
    }

	public String getName() {
		return name;
	}
}
