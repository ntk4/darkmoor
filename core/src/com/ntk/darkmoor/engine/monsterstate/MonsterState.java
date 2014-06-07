package com.ntk.darkmoor.engine.monsterstate;

import com.ntk.darkmoor.engine.Monster;
import com.ntk.darkmoor.exception.StateException;

public abstract class MonsterState extends StateAdapter {
	
	private Monster monster;
	
	public MonsterState(Monster monster)
	{
		if (monster == null)
			throw new StateException("Monster parameter is missing in MonsterState initialization");

		this.setMonster(monster);
	}

	public Monster getMonster() {
		return monster;
	}

	public void setMonster(Monster monster) {
		this.monster = monster;
	}

	
}
