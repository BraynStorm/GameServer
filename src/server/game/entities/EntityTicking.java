package server.game.entities;

import com.google.common.eventbus.Subscribe;

import braynstorm.commonlib.math.Vector3f;
import server.core.events.EntityTickEvent;

public abstract class EntityTicking extends Entity {

    public EntityTicking(int displayID, Vector3f position, boolean isVisible) {
        super(displayID, position, isVisible);
    }
    
    @Subscribe
    public abstract void tick(EntityTickEvent event);

}
