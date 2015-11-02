package server.game.powers;

import server.game.entities.EntityLiving;

public class Mana extends Power {
    
    public Mana(float max) {
        super("mana", max, 0);
    }
    
    @Override
    public void tick(EntityLiving entity) {
        //TODO check if entity is in combat and slow down the regen
        add(regenRate);
    }
    
}
