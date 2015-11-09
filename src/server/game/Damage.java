package server.game;

import server.game.entities.EntityLiving;

public class Damage {
    
    float amount;
    DamageType type;
    int absolutness;
    
    /**
     * @param amount the amount of damage to be done
     * @param type {@link DamageType}
     * @param absolutness 0 = regular damage, 1 = ignore defences, 2 = ignore immunities and defences.
     */
    public Damage(float amount, DamageType type, int absolutness){
        this.amount = amount;
        this.type = type;
        this.absolutness = absolutness;
    }
    
    /**
     * Deals the specified amount of damage to the entity.<br>
     * If the damage is absolute, it ignores all defences (true damage).<br>
     * Otherwise the damage respects all damage reductions.
     * 
     * Damage always respects immunities.<br>
     * @param entity
     * @return 1: Actual Damage Delt<br>2: Overkill.
     */
    public float[] dealTo(EntityLiving entity){
        
        float finalDamage = amount;
        CalculatableStats stats = entity.getCalculatedStats();
        
        if(absolutness < 1){
            finalDamage -= stats.getStat(type.toString() + "_defence_flat");
            
            if(type.isMagic()){
                finalDamage -= stats.getStat("magic_defence_flat");
                
                finalDamage *= stats.getStat("magic_defence_percent");
            }
        }
        
        if(finalDamage < 1f)
            finalDamage = 0;
        
        return new float[] {finalDamage, entity.getHealth().use(finalDamage, true)};
    }
    
    public static float[] dealDamageTo(float amount, DamageType type, int absolutness, EntityLiving entity){
        return new Damage(amount, type, absolutness).dealTo(entity);
    }
}
