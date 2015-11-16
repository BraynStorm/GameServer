package server.game.spells;

import java.util.List;

import server.core.Main;
import server.game.Damage;
import server.game.DamageType;
import server.game.entities.EntityLiving;
import server.game.entities.Player;
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

	@Override // TODO Unknown if it works.
	public void cast(EntityLiving src, Object target) {
		Damage damage = new Damage(100f, DamageType.PHYSICAL, 0);
		
		if(target instanceof EntityLiving) {
			//Entity
			EntityLiving targetEntity = (EntityLiving) target;
			float[] result = damage.dealTo(targetEntity);
			
			String.format("%s's hit %s for %3.2f(%s)(%3.2f).", src.getName(), id, targetEntity.getName(), result[0], DamageType.PHYSICAL.name(), result[1]);
		}else if (target instanceof Vector3f){
			Vector3f targetArea = (Vector3f) target;
			
			List<EntityLiving> affectedEntities = Main.getWorld().getAllEntitiesInShape(new ShapeCircle(targetArea, 15f));
			
			affectedEntities.forEach(entity -> {
				float[] result = damage.dealTo(entity);
				
				String.format("%s's hit %s for %3.2f(%s)(%3.2f).", src.getName(), id, entity.getName(), result[0], DamageType.PHYSICAL.name(), result[1]);
			});
			
		}
	}
}
