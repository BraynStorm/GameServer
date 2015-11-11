package server.game.spells;

import braynstorm.commonlib.math.Vector3f;
import server.game.entities.EntityLiving;
import server.game.entities.Player;

public abstract class Spell {
    protected int id;
    protected byte rank;
    protected boolean learnable;

    public Spell(int id, byte rank, boolean learnable) {
        this.id = id;
        this.rank = rank;
        this.learnable = learnable;
    }
    
    public abstract boolean canCast(Player source, EntityLiving target);
    public abstract boolean canCast(Player source, Vector3f traget);
    
    public abstract void cast(EntityLiving src, Object target);
}
