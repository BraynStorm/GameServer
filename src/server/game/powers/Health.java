package server.game.powers;

import server.game.entities.EntityLiving;

public class Health extends Power {

    public Health(String name, float max, float current) {
        super("health", max, current);
    }
    
    @Override
    public void tick(EntityLiving entity) {
        
    }
    
}
