package com.ntk.darkmoor.engine.interfaces;

import com.ntk.darkmoor.engine.Monster;

public interface IMonster
{

	/**
	 * Updates monster state
	 * @param monster
	 */
	void nUpdate(Monster monster);


	/**
	 * Draws the monster
	 * @param monster
	 */
	void onDraw(Monster monster);


	/**
	 *  Fired when the monster is killed
	 * @param monster
	 */
	void onDeath(Monster monster);
}
