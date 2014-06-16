package com.ntk.darkmoor.engine;

import java.io.IOException;
import java.util.Arrays;

import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.ntk.darkmoor.config.GameMechanics;
import com.ntk.darkmoor.config.Log;

public abstract class Entity {

	public enum EntityAlignment {
		LawfulGood("Lawfull good"), NeutralGood("Neutral good"), ChaoticGood("Chaotic good"),

		LawfulNeutral("Lawful neutral"), TrueNeutral("Neutral neutral"), ChaoticNeutral("Chaotic neutral"),

		LawfulEvil("Lawfull evil"), NeutralEvil("Neutral evil"), ChaoticEvil("Chaotic evil");

		private String description;

		private EntityAlignment(String description) {
			this.description = description;
		}

		public String description() {
			return description;
		}
	}

	public HitPoint hitPoint;
	public Ability charisma;
	public Ability strength;
	public Ability constitution;
	public Ability dexterity;
	public Ability intelligence;
	public Ability wisdom;
	public int armorClass;
	public int moveSpeed;
	public int baseSaveBonus;
	public byte antiMagic;
	public byte antiFire;

	public Attack lastAttack;
	protected long lastUpdate;
	public EntityAlignment alignment;

	// temporary variables
	int[] arr = new int[4];

	public Entity() {
		charisma = new Ability();
		strength = new Ability();
		constitution = new Ability();
		dexterity = new Ability();
		hitPoint = new HitPoint();
		intelligence = new Ability();
		wisdom = new Ability();
		moveSpeed = 1000; // 1sec
	}

	public void rollAbilities() {
		charisma.value = rollForAbility();
		strength.value = rollForAbility();
		constitution.value = rollForAbility();
		dexterity.value = rollForAbility();
		intelligence.value = rollForAbility();
		wisdom.value = rollForAbility();

		hitPoint = new HitPoint(GameMechanics.getRandom().nextInt(31) + 6);
	}

	private int rollForAbility() {
		Dice dice = new Dice(1, 6, 0);
		arr[0] = dice.roll();
		arr[1] = dice.roll();
		arr[2] = dice.roll();
		arr[3] = dice.roll();
		Arrays.sort(arr);

		return arr[1] + arr[2] + arr[3];
	}

	public abstract void hit(Attack attack);

	public void damage(Dice damage, SavingThrowType type, int difficulty) {
		if (damage == null)
			return;

		int save = Dice.getD20(1);

		// No damage
		if (save == 20 || save + savingThrow(type) > difficulty)
			return;

		hitPoint.damage(damage.roll());

	}

	public int savingThrow(SavingThrowType type) {
		int ret = baseSaveBonus;

		switch (type) {
		case Fortitude:
			ret += constitution.getModifier();
			break;
		case Reflex:
			ret += dexterity.getModifier();
			break;
		case Will:
			ret += wisdom.getModifier();
			break;
		}

		ret += Dice.getD20(1);

		return ret;
	}

	public boolean save(XmlWriter writer) throws IOException {
		if (writer == null)
			return false;

		// TODO: ntk: here we can't assess if we're currently are in an existing element
		hitPoint.save(writer);
		strength.save("strength", writer);
		intelligence.save("intelligence", writer);
		wisdom.save("wisdom", writer);
		dexterity.save("dexterity", writer);
		constitution.save("constitution", writer);
		charisma.save("charisma", writer);

		writer.element("alignment").attribute("value", alignment.toString()).pop();
		writer.element("movespeed").attribute("value", moveSpeed).pop();

		return true;
	}

	public boolean load(Element node) {
		if (node == null)
			return false;

		String name = node.getName();

		if ("strength".equalsIgnoreCase(name)) {
			strength.load(node);

		} else if ("intelligence".equalsIgnoreCase(name)) {
			intelligence.load(node);

		} else if ("wisdom".equalsIgnoreCase(name)) {
			wisdom.load(node);

		} else if ("dexterity".equalsIgnoreCase(name)) {
			dexterity.load(node);

		} else if ("constitution".equalsIgnoreCase(name)) {
			constitution.load(node);

		} else if ("alignment".equalsIgnoreCase(name)) {
			alignment = EntityAlignment.valueOf(node.getAttribute("value"));

		} else if ("charisma".equalsIgnoreCase(name)) {
			charisma.load(node);

		} else if ("hitpoint".equalsIgnoreCase(name)) {
			hitPoint.load(node);

		} else if ("movespeed".equalsIgnoreCase(name)) {
			moveSpeed = Integer.parseInt(node.getAttribute("value"));

		} else {
			Log.error("[Entity] load() : Unknown node : <%s>", name);
		}

		return true;
	}

	public boolean isDead() {
		return hitPoint.getCurrent() <= 0;
	}

	public HitPoint getHitPoint() {
		return hitPoint;
	}

	public Ability getCharisma() {
		return charisma;
	}

	public Ability getStrength() {
		return strength;
	}

	public Ability getConstitution() {
		return constitution;
	}

	public Ability getDexterity() {
		return dexterity;
	}

	public Ability getIntelligence() {
		return intelligence;
	}

	public Ability getWisdom() {
		return wisdom;
	}

	public int getArmorClass() {
		return armorClass;
	}

	public int getMoveSpeed() {
		return moveSpeed;
	}

	public int getBaseSaveBonus() {
		return baseSaveBonus;
	}

	public byte getAntiMagic() {
		return antiMagic;
	}

	public byte getAntiFire() {
		return antiFire;
	}

	public Attack getLastAttack() {
		return lastAttack;
	}

	public long getLastUpdate() {
		return lastUpdate;
	}

	public EntityAlignment getAlignment() {
		return alignment;
	}

	public int[] getArr() {
		return arr;
	}

	public void setAlignment(EntityAlignment alignment) {
		this.alignment = alignment;
	}
}
