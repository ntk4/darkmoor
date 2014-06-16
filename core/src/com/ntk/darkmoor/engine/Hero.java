package com.ntk.darkmoor.engine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.ntk.commons.StringUtils;

import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.ntk.darkmoor.config.GameMechanics;
import com.ntk.darkmoor.config.Log;
import com.ntk.darkmoor.engine.Compass.CardinalPoint;
import com.ntk.darkmoor.engine.HandAction.ActionResult;
import com.ntk.darkmoor.engine.Item.BodySlot;
import com.ntk.darkmoor.engine.Item.ItemType;
import com.ntk.darkmoor.resource.Resources;
import com.ntk.darkmoor.stub.GameScreen;
import com.ntk.darkmoor.stub.GameTime;

/**
 * Represents a hero in the team http://uaf.wiki.sourceforge.net/Player%27s+Guide
 * http://www.aidedd.org/regles-f97/creation-de-perso-t1456.html<br/>
 * http://christiansarda.free.fr/anc_jeux/eob1_intro.html
 * 
 * @author Nick
 * 
 */
public class Hero extends Entity {

	private static final int HAND_PENALTY_MILLIS = 250;
	public static final String TAG = "hero";

	public enum HeroClass {
		Fighter(0x1), Ranger(0x2), Paladin(0x4), Mage(0x8), Cleric(0x10), Thief(0x20);

		private int value;

		private HeroClass(int value) {
			this.value = value;
		}

		public int value() {
			return value;
		}

		public static Set<HeroClass> parse(String classList) {
			Set<HeroClass> result = new HashSet<Hero.HeroClass>(6);

			if (StringUtils.isEmpty(classList)) {
				return result;
			}

			for (HeroClass heroClass : values()) {
				if (classList.contains(heroClass.toString())) {
					result.add(heroClass);
				}
			}
			return result;
		}

	}

	public enum HeroHand {
		// / Right hand
		Primary(0),

		// / Left hand
		Secondary(1);

		private int value;

		private HeroHand(int value) {
			this.value = value;
		}

		public int value() {
			return value;
		}

		public static Set<HeroHand> parse(String handList) {
			Set<HeroHand> result = new HashSet<HeroHand>(6);

			if (StringUtils.isEmpty(handList)) {
				return result;
			}

			for (HeroHand hand : values()) {
				if (handList.contains(hand.toString())) {
					result.add(hand);
				}
			}
			return result;
		}
	}

	public enum InventoryPosition {
		Armor(0), Wrist(1), Secondary(2), Ring_Left(3), Ring_Right(4), Feet(5), Primary(6), Neck(7), Helmet(8);

		private int value;

		private InventoryPosition(int value) {
			this.value = value;
		}

		public int value() {
			return value;
		}
	}

	public enum HeroRace {

		Human(0), Elf(1), HalfElf(2), Dwarf(4), Gnome(8), Halfling(16);

		private int value;

		private HeroRace(int value) {
			this.value = value;
		}

		public int value() {
			return value;
		}

		public static HeroRace valueOf(int race) {
			switch (race) {
			case 0:
				return Human;
			case 1:
				return Elf;
			case 2:
				return HalfElf;
			case 4:
				return Dwarf;
			case 8:
				return Gnome;
			case 16:
				return Halfling;
			default:
				return Human;
			}
		}
	}

	public enum HeroGender {
		Male, Female;
		
		public static HeroGender valueOf(int gender) {
			if (gender == 0) {
				return Male;
			} else return Female;
		}
	}

	private String name;
	private ArrayList<Profession> professions;
	private ArrayList<String> learnedSpells;
	private ArrayList<List<Spell>> clericSpells;
	private ArrayList<List<Spell>> mageSpells;
	private boolean disposed;
	private int head;
	private Item[] inventory;
	private Item[] backPack;
	private Item[] waistPack;
	private Attack[] attacks;
	private HandAction[] handActions;
	private long[] handPenality;
	private int food;
	private int quiver;
	private HeroRace race;
	private HeroGender gender;

	private static final List<List<Spell>> noSpells = new ArrayList<List<Spell>>(6);

	static {
		initializeSpellListToEmpty(noSpells);
	}

	public Hero() {
		professions = new ArrayList<Profession>();
		learnedSpells = new ArrayList<String>();

		clericSpells = new ArrayList<List<Spell>>(6);
		initializeSpellListToEmpty(clericSpells);

		mageSpells = new ArrayList<List<Spell>>(6);
		initializeSpellListToEmpty(mageSpells);

		disposed = false;

		head = -1;
		inventory = new Item[26];
		backPack = new Item[14];
		waistPack = new Item[3];
		attacks = new Attack[2];
		handActions = new HandAction[2];
		handActions[0] = new HandAction(ActionResult.Ok);
		handActions[1] = new HandAction(ActionResult.Ok);
		handPenality = new long[2];

		food = (byte) GameMechanics.getRandom().nextInt(20) + 80;
	}

	public boolean isDisposed() {
		return disposed;
	}

	public void dispose() {
		disposed = true;
	}

	private static void initializeSpellListToEmpty(List<List<Spell>> spellList) {
		for (int i = 0; i < spellList.size(); i++)
			spellList.set(i, new ArrayList<Spell>());
	}

	public boolean load(Element xml) {
		if (xml == null)
			return false;

		this.name = xml.getAttribute("name");

		String name = null;
		for (int i = 0; i < xml.getChildCount(); i++) {
			Element child = xml.getChild(i);
			name = child.getName();

			if ("inventory".equalsIgnoreCase(name)) {
				setInventoryItem(InventoryPosition.valueOf(child.getAttribute("position")),
						Resources.getItemAsset(child.getAttribute("name")));

			} else if ("waist".equalsIgnoreCase(name)) {
				setWaistPackItem(Integer.parseInt(child.getAttribute("position")),
						Resources.getItemAsset(child.getAttribute("name")));

			} else if ("backpack".equalsIgnoreCase(name)) {
				setBackPackItem(Integer.parseInt(child.getAttribute("position")),
						Resources.getItemAsset(child.getAttribute("name")));

			} else if ("quiver".equalsIgnoreCase(name)) {
				quiver = Integer.parseInt(child.getAttribute("count"));

			} else if ("head".equalsIgnoreCase(name)) {
				head = Integer.parseInt(child.getAttribute("id"));

			} else if ("food".equalsIgnoreCase(name)) {
				food = Integer.parseInt(child.getAttribute("value"));

			} else if ("race".equalsIgnoreCase(name)) {
				race = HeroRace.valueOf(child.getAttribute("value"));

			} else if ("gender".equalsIgnoreCase(name)) {
				gender = HeroGender.valueOf(child.getAttribute("value"));

			} else if ("profession".equalsIgnoreCase(name)) {
				Profession prof = new Profession(child);
				professions.add(prof);

			} else if ("spell".equalsIgnoreCase(name)) {
				Spell spell = Resources.createAsset(Spell.class, child.getAttribute("name"));
				if (spell != null) {
					Log.debug("@Loading Hero Part : Unknown spell " + child.getAttribute("name"));
					continue;
				}
				// TODO: ntk: next code should be probably in the spell != null case!
				/*
				 * for (HeroClass hClass: classes) { if (hClass == spell.getHeroClass()) { List<List<Spell>> spells =
				 * getSpellsFromHeroClass(hClass); spells.get(spell.getLevel() - 1).add(spell); break; } }
				 */
			} else if ("learnedspell".equalsIgnoreCase(name)) {
				learnedSpells.add(child.getAttribute("name"));

			} else {
				super.load(child);
			}
		}

		return true;
	}

	public boolean save(XmlWriter writer) throws IOException {
		if (writer == null)
			return false;

		writer.element("hero").attribute("name", name);
		super.save(writer);

		writer.element("quiver").attribute("count", quiver).pop();
		writer.element("head").attribute("id", head).pop();
		writer.element("food").attribute("value", food).pop();
		writer.element("race").attribute("value", race.toString()).pop();
		writer.element("gender").attribute("value", gender.toString()).pop();

		for (Profession prof : professions)
			prof.save(writer);

		for (InventoryPosition pos : InventoryPosition.values()) {
			Item item = getInventoryItem(pos);
			if (item == null)
				continue;

			writer.element("inventory").attribute("position", pos.toString()).attribute("name", item.getName()).pop();
		}

		for (int id = 0; id < 3; id++) {
			if (waistPack[id] != null)
				writer.element("waist").attribute("position", id).attribute("name", waistPack[id].getName()).pop();
		}

		for (int id = 0; id < 14; id++) {
			if (backPack[id] != null)
				writer.element("backpack").attribute("position", id).attribute("name", backPack[id].getName());
		}

		for (int i = 0; i < 6; i++) {
			for (HeroClass hClass : getClasses()) {
				List<List<Spell>> spells = getSpellsFromHeroClass(hClass);
				// TODO: ntk: check if it's correct: added a new iteration in classes
				if (spells.size() > 0)
					for (Spell spell : spells.get(i))
						writer.element("spell").attribute("name", spell.getName()).pop();
			}
		}

		for (String name : learnedSpells)
			writer.element("learnedspell").attribute("name", name).pop();

		writer.pop();

		return true;
	}

	@Override
	public void rollAbilities() {
		super.rollAbilities();

		addExperience(69000);
	}

	public void addExperience(int amount) {
		if (amount == 0)
			return;

		for (Profession prof : professions) {
			if (prof.addXP(amount / getProfessionsCount())) {

				// New level gained
				GameMessage.addMessage(getName() + " gained a level in " + prof.getHeroClass() + " !");
			}
		}

	}

	public void update(GameTime time) {

		if (food > 100)
			food = 100;

		// Remove olds attacks
		// Attacks.RemoveAll(
		// delegate(AttackResult attack)
		// {
		// return attack.Date + attack.Hold < DateTime.Now;
		// });
	}

	public int getNewHPBonus() {

		int bonus = 0;
		int[] data = null;

		for (Profession prof : professions) {
			switch (prof.getHeroClass()) {
			case Fighter:
				data = HeroHPBonus.Fighter;
				break;
			case Ranger:
				data = HeroHPBonus.Ranger;
				break;
			case Paladin:
				data = HeroHPBonus.Paladin;
				break;
			case Mage:
				data = HeroHPBonus.Mage;
				break;
			case Cleric:
				data = HeroHPBonus.Cleric;
				break;
			case Thief:
				data = HeroHPBonus.Thief;
				break;
			default:
				return 0;
			}

			int min = data[prof.getLevel() * 2];
			bonus = Math.max(bonus, GameMechanics.getRandom().nextInt(1) + min);
		}

		return bonus;
	}

	@Override
	public String toString() {

		String txt = null;
		for (Profession prof : professions) {
			txt = String.format("{0} {1} ", txt, prof);
		}

		return String.format("%s (%d), %d", getName(), getHitPoint(), txt);
	}

	public int getMaxSpellCount(HeroClass heroClass, int level) {
		int[][] ClercLevels = new int[][] {
			new int[] {
				1, 0, 0, 0, 0, 0
			}, // 1
			new int[] {
				2, 0, 0, 0, 0, 0
			}, // 2
			new int[] {
				2, 1, 0, 0, 0, 0
			}, // 3
			new int[] {
				3, 2, 0, 0, 0, 0
			}, // 4
			new int[] {
				3, 3, 1, 0, 0, 0
			}, // 5
			new int[] {
				3, 3, 2, 0, 0, 0
			}, // 6
			new int[] {
				3, 3, 2, 1, 0, 0
			}, // 7
			new int[] {
				3, 3, 3, 2, 0, 0
			}, // 8
			new int[] {
				4, 4, 3, 2, 1, 0
			}, // 9
			new int[] {
				4, 4, 3, 3, 2, 0
			}, // 10
			new int[] {
				5, 4, 4, 3, 2, 1
			}, // 11
			new int[] {
				6, 5, 5, 3, 2, 2
			}, // 12
			new int[] {
				6, 6, 6, 4, 2, 2
			}, // 13
		};

		int[][] ClercBonus = new int[][] {
			new int[] {
				0, 0, 0, 0, 0, 0
			}, new int[] {
				0, 0, 0, 0, 0, 0
			}, new int[] {
				0, 0, 0, 0, 0, 0
			}, new int[] {
				0, 0, 0, 0, 0, 0
			}, new int[] {
				0, 0, 0, 0, 0, 0
			}, new int[] {
				0, 0, 0, 0, 0, 0
			}, new int[] {
				0, 0, 0, 0, 0, 0
			}, new int[] {
				0, 0, 0, 0, 0, 0
			}, new int[] {
				0, 0, 0, 0, 0, 0
			}, new int[] {
				0, 0, 0, 0, 0, 0
			}, new int[] {
				0, 0, 0, 0, 0, 0
			}, new int[] {
				0, 0, 0, 0, 0, 0
			}, new int[] {
				1, 0, 0, 0, 0, 0
			}, new int[] {
				2, 0, 0, 0, 0, 0
			}, new int[] {
				2, 1, 0, 0, 0, 0
			}, new int[] {
				2, 2, 0, 0, 0, 0
			}, new int[] {
				2, 2, 1, 0, 0, 0
			}, new int[] {
				2, 2, 1, 1, 0, 0
			}, new int[] {
				3, 2, 1, 2, 0, 0
			},
		};

		int[][] MageLevels = new int[][] {
			new int[] {
				1, 0, 0, 0, 0, 0
			}, // 1
			new int[] {
				2, 0, 0, 0, 0, 0
			}, // 2
			new int[] {
				2, 1, 0, 0, 0, 0
			}, // 3
			new int[] {
				3, 2, 0, 0, 0, 0
			}, // 4
			new int[] {
				4, 2, 1, 0, 0, 0
			}, // 5
			new int[] {
				4, 2, 2, 0, 0, 0
			}, // 6
			new int[] {
				4, 3, 2, 1, 0, 0
			}, // 7
			new int[] {
				4, 3, 3, 2, 0, 0
			}, // 8
			new int[] {
				4, 3, 3, 2, 1, 0
			}, // 9
			new int[] {
				4, 4, 3, 2, 2, 0
			}, // 10
			new int[] {
				4, 4, 4, 3, 3, 0
			}, // 11
			new int[] {
				4, 4, 4, 4, 4, 1
			}, // 12
			new int[] {
				5, 5, 5, 4, 4, 2
			}, // 13
		};

		int[][] PaladinLevels = new int[][] {
			new int[] {
				0, 0, 0
			}, new int[] {
				0, 0, 0
			}, new int[] {
				0, 0, 0
			}, new int[] {
				0, 0, 0
			}, new int[] {
				0, 0, 0
			}, new int[] {
				0, 0, 0
			}, new int[] {
				0, 0, 0
			}, new int[] {
				0, 0, 0
			}, new int[] {
				1, 0, 0
			}, new int[] {
				2, 0, 0
			}, new int[] {
				2, 1, 0
			}, new int[] {
				2, 2, 0
			}, new int[] {
				2, 2, 1
			},
		};

		Profession prof = getProfession(heroClass);
		if (prof == null)
			return 0;

		int count = 0;
		switch (heroClass) {
		case Paladin: {
			count = PaladinLevels[Math.min(13, prof.getLevel() - 1)][level - 1];
		}
			break;

		case Mage: {
			count = MageLevels[Math.min(13, prof.getLevel() - 1)][level - 1];
		}
			break;

		case Cleric: {
			// Base
			count = ClercLevels[Math.min(13, prof.getLevel() - 1)][level - 1];

			// Bonus
			if (prof.getLevel() >= 13)
				count += ClercBonus[Math.min(13, wisdom.value - 1)][level - 1];
		}
			break;
		default:
			break;
		}

		return count;
	}

	public Spell popSpell(HeroClass heroClass, int level, int number) {
		// Out of border
		if (level > 6 || number < 0)
			return null;

		Profession prof = getProfession(heroClass);
		if (prof == null)
			return null;

		// Get the spells of the desired level
		List<List<Spell>> spells = getSpellsFromHeroClass(heroClass);
		if (spells.get(level - 1).size() <= number - 1)
			return null;

		// Remove it
		Spell spell = spells.get(level - 1).get(number - 1);
		spells.get(level - 1).remove(number - 1);

		return spell;
	}

	private List<List<Spell>> getSpellsFromHeroClass(HeroClass heroClass) {
		List<List<Spell>> spells;
		if (heroClass == HeroClass.Paladin || heroClass == HeroClass.Cleric)
			spells = clericSpells;
		else if (heroClass == HeroClass.Mage)
			spells = mageSpells;
		else
			spells = noSpells;
		return spells;
	}

	public boolean pushSpell(Spell spell) {
		if (spell == null)
			return false;

		// TODO: ntk: this loop pushes the spell to ALL the classes of this hero. Check if it's correct
		for (HeroClass hClass : getClasses()) {
			List<List<Spell>> spells = getSpellsFromHeroClass(hClass);
			spells.get(spell.getLevel() - 1).add(spell);
		}

		return true;
	}

	public List<Spell> getSpells(HeroClass heroClass, int level) {
		List<Spell> lspells = new ArrayList<Spell>();
		// TODO: ntk: this loop gets the spells of all classes of this hero. Check if it's correct
		for (HeroClass hClass : getClasses()) {
			List<List<Spell>> spells = getSpellsFromHeroClass(hClass);

			// Bad level
			if (level < 0 || level > 6)
				return lspells;

			// Wrong profession
			if (getProfession(hClass) == null)
				return lspells;

			// Get a list of available spells for this level
			for (Spell spell : spells.get(level - 1)) {
				if (spell.getHeroClass() == hClass)
					lspells.add(spell);
			}

		}
		return lspells;
	}

	public Item getInventoryItem(InventoryPosition position) {
		return inventory[position.value()];
	}

	public boolean setInventoryItem(InventoryPosition position, Item item) {
		if (item == null) {
			inventory[position.value()] = item;
			return true;
		}

		boolean res = false;
		switch (position) {
		case Armor:
			if (item.getSlot().contains(BodySlot.Torso))
				res = true;
			break;

		case Wrist:
			if (item.getSlot().contains(BodySlot.Wrists))
				res = true;
			break;

		case Secondary:
			if (item.getSlot().contains(BodySlot.Secondary))
				res = true;
			break;

		case Ring_Left:
		case Ring_Right:
			if (item.getSlot().contains(BodySlot.Fingers))
				res = true;
			break;

		case Feet:
			if (item.getSlot().contains(BodySlot.Feet))
				res = true;
			break;

		case Primary:
			if (item.getSlot().contains(BodySlot.Primary))
				res = true;
			break;

		case Neck:
			if (item.getSlot().contains(BodySlot.Neck))
				res = true;
			break;

		case Helmet:
			if (item.getSlot().contains(BodySlot.Head))
				res = true;
			break;
		}

		if (res)
			inventory[position.value()] = item;

		return res;
	}

	public boolean addToInventory(Item item) {
		if (item == null)
			return false;

		// Arrow
		if (item.getSlot().contains(BodySlot.Quiver)) {
			quiver++;
			return true;
		}

		// Neck
		if (item.getSlot().contains(BodySlot.Neck) && getInventoryItem(InventoryPosition.Neck) == null) {
			setInventoryItem(InventoryPosition.Neck, item);
			return true;
		}

		// Armor
		if (item.getSlot().contains(BodySlot.Torso) && getInventoryItem(InventoryPosition.Armor) == null) {
			setInventoryItem(InventoryPosition.Armor, item);
			return true;
		}

		// Wrists
		if (item.getSlot().contains(BodySlot.Wrists) && getInventoryItem(InventoryPosition.Wrist) == null) {
			setInventoryItem(InventoryPosition.Wrist, item);
			return true;
		}

		// Helmet
		if (item.getSlot().contains(BodySlot.Head) && getInventoryItem(InventoryPosition.Helmet) == null) {
			setInventoryItem(InventoryPosition.Helmet, item);
			return true;
		}

		// Primary
		if (item.getSlot().contains(BodySlot.Primary)
				&& (item.getType() == ItemType.Weapon || item.getType() == ItemType.Shield || item.getType() == ItemType.Wand)
				&& getInventoryItem(InventoryPosition.Primary) == null && canUseHand(HeroHand.Primary)) {
			setInventoryItem(InventoryPosition.Primary, item);
			return true;
		}

		// Secondary
		if (item.getSlot().contains(BodySlot.Secondary)
				&& (item.getType() == ItemType.Weapon || item.getType() == ItemType.Shield || item.getType() == ItemType.Wand)
				&& getInventoryItem(InventoryPosition.Secondary) == null && canUseHand(HeroHand.Secondary)) {
			setInventoryItem(InventoryPosition.Secondary, item);
			return true;
		}

		// Boots
		if (item.getSlot().contains(BodySlot.Feet) && getInventoryItem(InventoryPosition.Feet) == null) {
			setInventoryItem(InventoryPosition.Feet, item);
			return true;
		}

		// Fingers
		if (item.getSlot().contains(BodySlot.Fingers)) {
			if (getInventoryItem(InventoryPosition.Ring_Left) == null) {
				setInventoryItem(InventoryPosition.Ring_Right, item);
				return true;
			}
			// else
			if (getInventoryItem(InventoryPosition.Ring_Right) == null) {
				setInventoryItem(InventoryPosition.Ring_Left, item);
				return true;
			}
		}

		// Belt
		if (item.getSlot().contains(BodySlot.Belt)) {
			for (int i = 0; i < 3; i++) {
				if (getWaistPackItem(i) == null) {
					setWaistPackItem(i, item);
					return true;
				}
			}
		}

		// Else anywhere in the bag...
		for (int i = 0; i < 14; i++) {
			if (backPack[i] == null) {
				backPack[i] = item;
				return true;
			}
		}
		// Sorry no room !
		return false;
	}

	@Override
	public void hit(Attack attack) {
		if (attack == null)
			return;

		lastAttack = attack;
		if (lastAttack.isAMiss())
			return;

		hitPoint.damage(lastAttack.getHit());
	}

	public void addHandPenality(HeroHand hand, long duration) {

		handPenality[hand.value()] = new Date().getTime() + duration;
	}

	public void useHand(HeroHand hand) {
		// No action possible
		if (!canUseHand(hand))
			return;

		Team team = GameScreen.getTeam();

		// Find the entity in front of the hero
		Entity target = team.getFrontEntity(team.getHeroGroundPosition(this));

		// Which item is used for the attack
		Item item = getInventoryItem(hand == HeroHand.Primary ? InventoryPosition.Primary : InventoryPosition.Secondary);
		CardinalPoint side = Compass.getOppositeDirection(team.getDirection());

		// Hand attack
		if (item == null) {
			if (team.isHeroInFront(this)) {
				if (team.getFrontSquare() != null)
					team.getFrontSquare().onBash(side, item);
				else
					attacks[hand.value()] = new Attack(this, target, null);
			} else
				handActions[hand.value()] = new HandAction(ActionResult.CantReach);

			addHandPenality(hand, HAND_PENALTY_MILLIS);
			return;
		}

		// Use item
		DungeonLocation loc = new DungeonLocation(team.getLocation());
		loc.setPosition(team.getHeroGroundPosition(this));
		switch (item.getType()) {

		case Ammo: {
			// throw ammo
			team.getMaze().getThrownItems().add(new ThrownItem(this, item, loc, 250, Integer.MAX_VALUE));

			// Empty hand
			setInventoryItem(hand == HeroHand.Primary ? InventoryPosition.Primary : InventoryPosition.Secondary, null);
		}
			break;

		case Scroll:
			break;

		case Wand:
			break;

		case Weapon: {
			// Belt weapon
			if (item.getSlot().contains(BodySlot.Belt)) {
			}

			// Weapon use quiver
			else if (item.isUseQuiver()) {
				if (quiver > 0) {
					team.getMaze().getThrownItems()
							.add(new ThrownItem(this, Resources.getItemAsset("Arrow"), loc, 250, Integer.MAX_VALUE));
					quiver--;
				} else
					handActions[hand.value()] = new HandAction(ActionResult.NoAmmo);

				addHandPenality(hand, 500);
			}

			else {
				// Check is the weapon can reach the target
				if (team.isHeroInFront(this) && item.getRange() == 0) {
					// Attack front monster
					if (target != null) {
						attacks[hand.value()] = new Attack(this, target, item);
					} else if (team.getFrontSquare() != null)
						team.getFrontSquare().onHack(side, item);
					else
						attacks[hand.value()] = new Attack(this, target, item);
				} else
					handActions[hand.value()] = new HandAction(ActionResult.CantReach);

				addHandPenality(hand, item.getAttackSpeed());
			}
		}
			break;

		case HolySymbol:
		case Book: {
			GameScreen.getSpellBook().open(this, item);

			// Spell spell = ResourceManager.CreateAsset<Spell>("CreateFood");
			// spell.Init();
			// spell.Script.Instance.OnCast(spell, this);
		}
			break;
		}

	}

	public void setBackPackItem(int position, Item item) {
		if (position < 0 || position > 14)
			return;

		backPack[position] = item;
	}

	public Item getBackPackItem(int position) {
		if (position < 0 || position > 13)
			return null;

		return backPack[position];
	}

	public boolean setWaistPackItem(int position, Item item) {
		if (position < 0 || position > 2)
			return false;

		if (item == null) {
			waistPack[position] = null;
			return true;
		}

		if (!item.getSlot().contains(BodySlot.Belt))
			return false;

		waistPack[position] = item;
		return true;
	}

	public Item getWaistPackItem(int position) {
		if (position < 0 || position > 3)
			return null;

		return waistPack[position];
	}

	public boolean checkMultiClassValidity() {
		// Fighter
		// Ranger
		// Paladin
		// Mage
		// Cleric
		// Thief
		// Fighter/Cleric
		// Fighter/Thief
		// Fighter/Mage
		// Fighter/Mage/Thief
		// Thief/Mage
		// Cleric/Thief
		// Fighter/Cleric/Mage
		// Ranger/Cleric
		// Cleric/Mage

		return true;
	}

	/**
	 * Checks if the hero belongs to a class
	 * 
	 * @param heroClass
	 * @return
	 */
	public boolean checkClass(HeroClass heroClass) {
		for (Profession prof : professions)
			if (prof != null) {
				if (prof.getHeroClass() == heroClass)
					return true;
			}

		return false;
	}

	public Profession getProfession(HeroClass heroClass) {
		for (Profession prof : professions)
			if (prof.getHeroClass() == heroClass)
				return prof;

		return null;
	}

	public Profession getProfession(int professionIndex) {
		if (professionIndex < professions.size() && professionIndex >= 0)
			return professions.get(professionIndex);
		return null;
	}

	public boolean canUseHand(HeroHand hand) {
		if (isDead() || isUnconscious())
			return false;

		// Check the item in the other hand
		Item item = getInventoryItem(hand == HeroHand.Primary ? InventoryPosition.Secondary : InventoryPosition.Primary);
		if (item != null && item.isTwoHanded())
			return false;

		return handPenality[hand.value()] < new Date().getTime();
	}

	public Attack getLastAttack(HeroHand hand) {
		return attacks[hand.value()];
	}

	public HandAction getLastActionResult(HeroHand hand) {
		return handActions[hand.value()];
	}

	public boolean canHeal() {
		// Check for hero class
		if (!checkClass(HeroClass.Cleric))
			return false;

		List<Spell> spells = null;

		// Level 1 - Cure Light Wounds
		spells = getSpells(HeroClass.Cleric, 1);

		for (Spell spell : spells)
			if ("Cure Light Wounds".equalsIgnoreCase(spell.getName()))
				return true;

		// Level 4 - Cure Serious Wounds
		spells = getSpells(HeroClass.Cleric, 4);

		for (Spell spell : spells)
			if ("Cure Serious Wounds".equalsIgnoreCase(spell.getName()))
				return true;

		// Level 5 - Cure Critical Wounds
		spells = getSpells(HeroClass.Cleric, 5);

		for (Spell spell : spells)
			if ("Cure Critical Wounds".equalsIgnoreCase(spell.getName()))
				return true;

		// Level 6 - Heal
		spells = getSpells(HeroClass.Cleric, 6);

		for (Spell spell : spells)
			if ("Heal".equalsIgnoreCase(spell.getName()))
				return true;

		return false;
	}

	public void heal(Hero hero) {
		if (!canHeal() || hero == null)
			return;

		// Spell spell = Hero.PopSpell(Class, SpellLevel, i + 1);
		// if (spell != null && spell.Script.Instance != null)
		// spell.Script.Instance.OnCast(spell, Hero);

		List<Spell> spells = null;

		// Level 1 - Cure Light Wounds
		spells = getSpells(HeroClass.Cleric, 1);

		for (Spell spell : spells) {
			if ("Cure Light Wounds".equalsIgnoreCase(spell.getName())) {
				spell.getScript().getInstance().onCast(spell, hero);
				return;
			}

		}

		// Level 4 - Cure Serious Wounds
		spells = getSpells(HeroClass.Cleric, 4);

		for (Spell spell : spells)
			if ("Cure Serious Wounds".equalsIgnoreCase(spell.getName())) {
			}

		// Level 5 - Cure Critical Wounds
		spells = getSpells(HeroClass.Cleric, 5);

		for (Spell spell : spells)
			if ("Cure Critical Wounds".equalsIgnoreCase(spell.getName())) {
			}

		// Level 6 - Heal
		spells = getSpells(HeroClass.Cleric, 6);

		for (Spell spell : spells)
			if ("Heal".equalsIgnoreCase(spell.getName())) {
			}

	}

	@Override
	public boolean isDead() {
		return hitPoint.getCurrent() <= -10;
	}

	public boolean isUnconscious() {
		return hitPoint.getCurrent() > -10 && hitPoint.getCurrent() <= 0;
	}

	@Override
	public int getArmorClass() {
		return 10 + getArmorBonus() + getShieldBonus() + getDodgeBonus() + getNaturalArmorBonus();
	}

	public Set<HeroClass> getClasses() {
		Set<HeroClass> hclass = new HashSet<HeroClass>();

		for (Profession prof : professions)
			hclass.add(prof.getHeroClass());

		return hclass;
	}

	public int getBaseAttackBonus() {
		int value = 0;

		for (Profession prof : professions) {
			if (prof == null)
				continue;

			if (prof.getHeroClass() == HeroClass.Fighter || prof.getHeroClass() == HeroClass.Ranger
					|| prof.getHeroClass() == HeroClass.Paladin)
				value += prof.getLevel();

			if (prof.getHeroClass() == HeroClass.Cleric || prof.getHeroClass() == HeroClass.Mage
					|| prof.getHeroClass() == HeroClass.Thief)
				value += (prof.getLevel() * 4) / 3;
		}

		return value;
	}

	public Item popWaistItem() {
		return null;
	}

	public int getProfessionsCount() {
		return professions.size();
	}

	public ArrayList<Profession> getProfessions() {
		return professions;
	}

	public ArrayList<String> getLearnedSpells() {
		return learnedSpells;
	}

	public ArrayList<List<Spell>> getClericSpells() {
		return clericSpells;
	}

	public ArrayList<List<Spell>> getMageSpells() {
		return mageSpells;
	}

	public int getHead() {
		return head;
	}

	public Item[] getInventory() {
		return inventory;
	}

	public Item[] getBackPack() {
		return backPack;
	}

	public Item[] getWaistPack() {
		return waistPack;
	}

	public Attack[] getAttacks() {
		return attacks;
	}

	public HandAction[] getHandActions() {
		return handActions;
	}

	public long[] getHandPenality() {
		return handPenality;
	}

	public int getFood() {
		return food;
	}

	public int getQuiver() {
		return quiver;
	}

	public int getArmorBonus() {
		byte value = 0;

		Item item = getInventoryItem(InventoryPosition.Helmet);
		if (item != null)
			value += item.getArmorClass();

		item = getInventoryItem(InventoryPosition.Armor);
		if (item != null)
			value += item.getArmorClass();

		item = getInventoryItem(InventoryPosition.Wrist);
		if (item != null)
			value += item.getArmorClass();

		return value;
	}

	public int getShieldBonus() {
		Item item = getInventoryItem(InventoryPosition.Secondary);
		if (item == null)
			return 0;

		return item.getArmorClass();
	}

	public int getDodgeBonus() {
		Item item = getInventoryItem(InventoryPosition.Feet);
		if (item == null)
			return 0;

		return item.getArmorClass();
	}

	public int getNaturalArmorBonus() {
		Item item = getInventoryItem(InventoryPosition.Neck);
		if (item == null)
			return 0;

		return item.getArmorClass();
	}

	public int getBaseSaveBonus() {
		int value = 2;

		for (Profession prof : professions) {
			if (prof == null)
				continue;
			value += prof.getLevel() / 2;
		}
		return value;
	}

	public String getName() {
		return name;
	}

	public HeroRace getRace() {
		return race;
	}

	public void setRace(HeroRace race) {
		this.race = race;
	}

	public HeroGender getGender() {
		return gender;
	}

	public void setGender(HeroGender gender) {
		this.gender = gender;
	}

	public void setHead(int head) {
		this.head = head;
	}

	public void setName(String name) {
		this.name = name;
	}

}
