package com.ntk.darkmoor.engine;

public enum SavingThrowType {

	/**
	 * These saves measure the character's ability to stand up to massive physical punishment or attacks against his or
	 * her vitality and health such as poison and paralysis. Apply the character's Constitution modifier to his or her
	 * Fortitude saving throws.
	 */
	Fortitude,

	/**
	 * These saves test the character's ability to dodge massive attacks such as explosions or car wrecks. (Often, when
	 * damage is inevitable, the character gets to make a Reflex save to take only half damage.) Apply the character's
	 * Dexterity modifier to his or her Reflex saving throws.
	 */

	Reflex,

	/**
	 * These saves reflect the character's resistance to mental influence and domination as well as to many magical
	 * effects. Apply the character's Wisdom modifier to his or her Will saving throws.
	 */
	Will,
}
