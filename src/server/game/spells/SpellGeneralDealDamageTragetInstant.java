package server.game.spells;

import server.game.Damage;
import server.game.DamageType;
import server.game.entities.EntityLiving;
import server.game.entities.Player;
import braynstorm.commonlib.Logger;
import braynstorm.commonlib.math.Vector3f;

public abstract class SpellGeneralDealDamageTragetInstant extends Spell {
	
	protected float maxRange;
	
	public SpellGeneralDealDamageTragetInstant(int id, float maxRange,  byte rank, boolean learnable) {
		super(id, rank, learnable);
		this.maxRange = maxRange;
	}
	
	@Override
	public boolean canCast(Player src, EntityLiving target) {
		return
				!target.isDead()
				&& !src.isDead()
				&& (	Vector3f.getDistanceSquared(src.getPosition(), target.getPosition()) <= maxRange 
						|| maxRange < 0);
	}

	@Override
	public boolean canCast(Player src, Vector3f traget) {
		return false;
	}

	@Override
	public void cast(EntityLiving src, Object target) {
		
		
		if(target instanceof EntityLiving) {
			target = (EntityLiving) target;
		} else {
			target = (Vector3f) target;
		}
		
		
		
		float[] result = Damage.dealDamageTo(100f, DamageType.PHYSICAL, 0, target);
		// TODO a lot of work on the 'log' left...
		String.format("%s's hit %s for %3.2f(%s)(%3.2f).", src.getName(), id, target.getName(), result[0], DamageType.PHYSICAL.name(), result[1]);
	}

}
