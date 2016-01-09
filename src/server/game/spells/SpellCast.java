package server.game.spells;

import server.game.entities.EntityLiving;

public class SpellCast {
	private long startCastTime; // ms
	int castTime;
	Spell spell;
	
	EntityLiving src;
	EntityLiving target;
}
