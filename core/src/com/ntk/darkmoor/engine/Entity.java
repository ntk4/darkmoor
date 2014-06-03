package com.ntk.darkmoor.engine;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

import org.ntk.commons.StringUtils;

import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.ntk.darkmoor.config.Log;
import com.ntk.darkmoor.stub.GameMechanics;

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
	protected Date lastUpdate;
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

		hitPoint.setCurrent(hitPoint.getCurrent() - damage.roll());

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

		if (!StringUtils.isEmpty(node.getAttribute("strength"))) {
			strength.load(node);

		} else if (!StringUtils.isEmpty(node.getAttribute("intelligence"))) {
			intelligence.load(node);

		} else if (!StringUtils.isEmpty(node.getAttribute("wisdom"))) {
			wisdom.load(node);

		} else if (!StringUtils.isEmpty(node.getAttribute("dexterity"))) {
			dexterity.load(node);

		} else if (!StringUtils.isEmpty(node.getAttribute("constitution"))) {
			constitution.load(node);

		} else if (!StringUtils.isEmpty(node.getAttribute("alignment"))) {
			alignment = EntityAlignment.valueOf(node.getAttribute("value"));

		} else if (!StringUtils.isEmpty(node.getAttribute("hitpoint"))) {
			hitPoint.load(node);

		} else if (!StringUtils.isEmpty(node.getAttribute("movespeed"))) {
			moveSpeed = Integer.parseInt(node.getAttribute("value"));

		} else {
			Log.error("[Entity] load() : Unknown node : <%s>", node.getName());
		}

		return true;
	}

	public boolean isDead() {
		return hitPoint.getCurrent() <= 0;
	}
}
