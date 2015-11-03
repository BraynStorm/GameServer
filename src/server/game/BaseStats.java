package server.game;

import server.game.entities.EntityLiving;

public class BaseStats {
    private EntityLiving entity;
    
    public BaseStats(EntityLiving entity) {
        this.entity = entity;
    }

    /**
     * PhysicalPower +=         str * 1.00f.<br>
     * Armor +=                 str * 5.50f.<br>
     * PhysicalCritDamage +=    str * 0.35f.<br>
     */
    public float strength;
    
    /**
     * PhysicalPower +=          dex * 0.10f.<br>
     * PhysicalCritChance +=     dex * 0.03f.<br>
     * PhysicalCritDamage +=     if(crit > 1.0) crit - 1.<br>
     * TimePerAttack(sec) -=     dex * 0.10f.<br>
     * WalkSpeed +=              dex * 0.12f.<br>
     * SpellDamage +=            dex * 0.30f.<br>
     * TimeToCastSpell(sec) -=   dex * 0.08f.<br>
     * SpellCritChance +=        dex * 0.29f.<br>
     */
    public float dextirity;
    public float intellect; // increases healing slightly (1/7 of wisdom) and spell damage, increases resistances, spell ranking up.
    public float wisdom; // Chance not to use mana, decreases chance to miss. Increases healing power. Cast speed.
    public float concentration; // Haste (3 * wisdom). Crit ( 2/3 * dextirity). Reduces cooldowns. Increases chance to hit. Reduces movement speed for casters.
    
    public float armor;
    
}
