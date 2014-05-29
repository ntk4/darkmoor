package com.ntk.darkmoor.engine.monsterstate;

import com.ntk.darkmoor.stub.GameScreen;
import com.ntk.darkmoor.stub.GameTime;
import com.ntk.darkmoor.stub.Monster;
import com.ntk.darkmoor.stub.Team;

public class Attack extends MonsterState {

	public Attack(Monster monster) {
		super(monster);
	}
	
	@Override
	public void update(GameTime time)
	{
		Team team = GameScreen.getTeam();

		// Can see the team anymore
		if (!getMonster().canSee(team.getLocation()) || !getMonster().canDetect(team.getLocation()))
		{
			exit = true;
			return;
		}

		// Facing the team ?
		if (!getMonster().getLocation().isFacing(team.getLocation()))
		{
			getMonster().turnTo(team.getLocation());
			return;
		}


		// Can attack ?
		if (getMonster().attack(team.getLocation()))
			return;

		// Move to the team
	}

}
