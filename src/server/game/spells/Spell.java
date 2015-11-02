package server.game.spells;

import server.game.entities.EntityLiving;

public abstract class Spell {
    protected int id;
    protected byte rank;
    protected boolean learnable;

    public Spell(int id, byte rank, boolean learnable) {
        this.id = id;
        this.rank = rank;
        this.learnable = learnable;
    }
    
    public abstract void cast(EntityLiving src, EntityLiving target);
}
