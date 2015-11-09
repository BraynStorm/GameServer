package server.game.entities;

import java.nio.ByteBuffer;

import com.google.common.eventbus.Subscribe;

import braynstorm.commonlib.Common;
import braynstorm.commonlib.math.Vector3f;
import braynstorm.commonlib.network.PacketSize;
import braynstorm.commonlib.network.PacketType;
import server.core.events.EntityTickEvent;

public abstract class EntityTicking extends Entity {

    public EntityTicking(int displayID, Vector3f position, boolean isVisible) {
        super(displayID, position, isVisible);
    }
    
    @Subscribe
    public abstract void tick(EntityTickEvent event);
}
