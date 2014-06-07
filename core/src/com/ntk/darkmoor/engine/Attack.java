package com.ntk.darkmoor.engine;

import java.util.Date;

import com.ntk.darkmoor.stub.GameScreen;

/**
 * This class handle an attack between to entities. There is a difference between Attacking and damaging. If you attack
 * a monster, it is an attempt to do damage to it. It is not guaranteed that the attack is successful. In case of damage
 * the attack was successful.
 * 
 * @author Nick
 * 
 */
public class Attack {

	private Entity striker;
	private Entity target;
	private Item item;
	private Date time;
	private int range;
	private int hit;

	/**
	 * http://nwn2.wikia.com/wiki/Attack_sequence
	 * 
	 * @param attacker
	 * @param target
	 * @param item
	 */
	public Attack(Entity attacker, Entity target, Item item) {
		Team team = GameScreen.getTeam();
		time = new Date();
		this.striker = attacker;
		this.target = target;
		this.item = item;

		if (attacker == null || target == null)
			return;

		// Ranged attack ?
		DungeonLocation from = null;
		DungeonLocation to = null;
		if (attacker instanceof Hero)
			from = team.getLocation();
		else
			from = ((Monster) attacker).getLocation();

		if (target instanceof Hero)
			to = team.getLocation();
		else
			to = ((Monster) target).getLocation();

		range = (int) Math.sqrt((from.getCoordinates().y - to.getCoordinates().y)
				* (from.getCoordinates().y - to.getCoordinates().y) + (from.getCoordinates().x - to.getCoordinates().x)
				* (from.getCoordinates().x - to.getCoordinates().x));

		// Attack roll
		int attackdie = Dice.getD20(1);

		// Critical fail ?
		if (attackdie == 1)
			attackdie = -100000;

		// Critical Hit ?
		if (attackdie == 20)
			attackdie = 100000;

		// Base attack bonus
		int baseattackbonus = 0;
		int modifier = 0; // modifier
		int sizemodifier = 0; // Size modifier
		int rangepenality = 0; // Range penality

		if (attacker instanceof Hero) {
			baseattackbonus = ((Hero) attacker).getBaseAttackBonus();
		} else if (attacker instanceof Monster) {
			Monster monster = (Monster) attacker;
			// sizemodifier = (int)monster.Size;
		}

		// Range penality
		if (isRangedAttack()) {
			modifier = attacker.dexterity.getModifier();

			// TODO : Add range penality
		} else
			modifier = attacker.strength.getModifier();

		// Attack bonus
		int attackbonus = baseattackbonus + modifier + sizemodifier + rangepenality;
		if (target.armorClass > attackdie + attackbonus)
			return;

		if (item != null)
			hit = item.getDamage().roll();
		else {
			Dice dice = new Dice(1, 4, 0);
			hit = dice.roll();
		}

		if (isAHit())
			target.hit(this);

	}

	public boolean isOutdated(Date time, int length)
	{
		if (this.time == null) {
			return true; // TODO: ntk: to be discussed!
		}
		return (this.time.getTime() + length < time.getTime());
	}
	
	public boolean isAHit() {
		return hit > 0;
	}

	public boolean isAMiss() {
		return hit == 0;
	}
	
	public boolean isRangedAttack() {
		return range > 1;
	}

	public Entity getStriker() {
		return striker;
	}

	public void setStriker(Entity striker) {
		this.striker = striker;
	}

	public Entity getTarget() {
		return target;
	}

	public void setTarget(Entity target) {
		this.target = target;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}

	public int getHit() {
		return hit;
	}

	public void setHit(int hit) {
		this.hit = hit;
	}

}
