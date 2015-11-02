package server.game.auras;

import java.util.List;

import com.google.common.eventbus.Subscribe;

import server.core.events.AuraTickEvent;
import server.game.entities.EntityLiving;

public abstract class Aura {
    protected int id;
    protected int startTime;
    protected int timeLeft;
    protected List<Object> data;
    
    public void applyTo(EntityLiving entity){
        entity.addAura(this);
    }

    public int getID() {
        return id;
    }
    
    @Subscribe
    public void tick(AuraTickEvent e){
        
    }
    
    
}
