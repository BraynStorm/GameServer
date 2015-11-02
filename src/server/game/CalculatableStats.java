package server.game;

import java.util.HashMap;
import java.util.Map;

import server.game.entities.EntityLiving;

public class CalculatableStats {
    /*
     * Flat additive amplifiers.
     */
    
    public float physicalPower;
    public float spellDamagePower;
    public float spellHealPower;
    
    public float chanceToMissPhysical;
    public float chanceToMissMagical;
    
    public float chanceToCritPhysical;
    public float chanceToCritMagical;
    
    public float movementSpeed; // obvious
    
    /*
     * Flat damage reductions. (aka dmg - reduction)
     */
    public Map<String, Float> allStats;
    public Map<DamageType, Float> flatReductions;
    
    public float percentDamageReduction; // Auras only
    public float percentPhysicalDamageReduction; // auras only
    public float percentMagicalDamageReduction; // auras only
    
    public CalculatableStats() {
        flatReductions = new HashMap<DamageType, Float>();
    }
    
    public void recalcAll(EntityLiving entity){
        //TODO recalculate all the stats, +checking for stats-increasing auras, and passive spells. (?)
        BaseStats stats = entity.getBaseStats();
        
        allStats = new HashMap<>(30);
        allStats.put("PHYSICAL_increase_flat", (stats.strength * 1f) + (stats.dextirity * 0.1f));
        allStats.put("PHYSICAL_defence_flat", (stats.strength * 5.5f) + stats.armor);
        
    }
    
    public void increaseStat(String stat, float amount){
        allStats.put(stat, allStats.get(stat) + amount);
    }
    
    public void reduceStat(String stat, float amount){
        allStats.put(stat, allStats.get(stat) - amount);
    }
    
    public void setStatTo(String stat, float value){
        allStats.put(stat, value);
    }

    public float getStat(String stat) {
        return allStats.get(stat);
    }
}
