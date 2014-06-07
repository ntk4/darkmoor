package com.ntk.darkmoor.engine.monsterstate;

import com.badlogic.gdx.math.Vector2;
import com.ntk.darkmoor.engine.Square;
import com.ntk.darkmoor.engine.Compass.CardinalPoint;
import com.ntk.darkmoor.stub.GameTime;
import com.ntk.darkmoor.stub.Monster;
import com.ntk.darkmoor.util.RandomEnum;

public class Idle extends MonsterState {

	public Idle(Monster monster) {
		super(monster);
	}

	@Override
	public void update(GameTime time) {
		// Target range
		//ntk: not used
//		int range = GameMechanics.getRandom().nextInt(Monster.getSightRange());

		// Direction to face to

		while (true) {

			CardinalPoint direction = new RandomEnum<CardinalPoint>(CardinalPoint.class).get();
			if (direction == getMonster().getLocation().getDirection())
				continue;

			// int dir = Dice.GetD20(1);
			Vector2 vector = getMonster().getLocation().getCoordinates();

			//@formatter:off
			/* 
			// Depending the current direction
			switch (Monster.Location.Direction)
			{
				case CardinalPoint.North:
				{
					if (dir < 10)
					{
						direction = CardinalPoint.West;
						vector.X--;
					}
					else
					{
						direction = CardinalPoint.East;
						vector.X++;
					}
				}
				break;
				case CardinalPoint.South:
				{
					if (dir < 10)
					{
						direction = CardinalPoint.East;
						vector.X++;
					}
					else
					{
						direction = CardinalPoint.West;
						vector.X--;
					}

				}
				break;
				case CardinalPoint.West:
				{
					if (dir < 10)
					{
						direction = CardinalPoint.North;
						vector.Y--;
					}
					else
					{
						direction = CardinalPoint.South;
						vector.Y++;
					}

				}
				break;
				case CardinalPoint.East:
				{
					if (dir < 10)
					{
						direction = CardinalPoint.South;
						vector.Y++;
					}
					else
					{
						direction = CardinalPoint.North;
						vector.Y--;
					}

				}
				break;
			}
		*/
			//@formatter:on 
			
			// Depending the current direction
			switch (direction) {
			case North: {
				vector.y--;
			}
				break;
			case South: {
				vector.y++;
			}
				break;
			case West: {
				vector.x--;
			}
				break;
			case East: {
				vector.x++;
			}
				break;
			}

			// Check the block
			Square block = Monster.getMaze().getSquare(vector);
			if (block != null) {
				// Automatic direction changing
				// Monster.Location.Direction = direction;

				// Monster.StateManager.PushState(new MoveState(Monster, range, direction));
				return;
			}
		}
	}
}
