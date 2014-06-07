package com.ntk.darkmoor.engine.interfaces;

import com.ntk.darkmoor.engine.Hero;
import com.ntk.darkmoor.engine.Spell;

public interface ISpell {

	public static enum SpellRange {
		/** The spell affects only the caster. */
		Personal,

		/** Affect the whole team */
		Team,

		/** You must touch a creature or object to affect it. */
		Touch,

		/** The spell reaches as far as 1 block */
		Close,

		/** The spell reaches as far as 2 blocks */
		Medium,

		/** The spell reaches as far as 3 blocks */
		Long,

		/** The spell reaches far as possible */
		Unlimited;

	}

	/**
	 * When the spell is casted
	 * 
	 * @param spell
	 *            spell handle
	 * @param hero
	 *            hero casting the spell
	 */
	void onCast(Spell spell, Hero hero);
}
