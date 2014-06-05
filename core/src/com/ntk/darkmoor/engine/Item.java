package com.ntk.darkmoor.engine;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.ntk.commons.StringUtils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.ntk.darkmoor.engine.interfaces.IItem;
import com.ntk.darkmoor.stub.Hero.HeroClass;
import com.ntk.darkmoor.stub.Hero.HeroHand;
import com.ntk.darkmoor.stub.ScriptInterface;

public class Item {

	public enum ItemType {
		Adornment, Ammo, Armor, Consumable, Miscellaneous, Potion, Scroll, Shield, Wand, Weapon, Key, Book, HolySymbol,
	}

	/** Torso slots */
	public enum BodySlot {
		/** Left hand */
		Primary(0x1),
		/** Right hand */
		Secondary(0x2),
		/** Quiver */
		Quiver(0x4),
		/** Armour */
		Torso(0x8),
		/** Necklace */
		Neck(0x10),
		/** Fingers */
		Fingers(0x20),
		/** Bracers */
		Wrists(0x40),
		/** Boots */
		Feet(0x80),
		/** Helmet */
		Head(0x100),
		/** Belt */
		Belt(0x200),
		/** Slotless worn items, items which are not worn */
		None(0x400);

		private int value;

		private BodySlot(int value) {
			this.value = value;
		}

		public int value() {
			return value;
		}

		public static BodySlot fromValue(int value) {
			for (BodySlot slot : BodySlot.values()) {
				if (slot.value == value)
					return slot;
			}
			return BodySlot.Primary;
		}

		public static Set<BodySlot> parse(String slotList) {
			Set<BodySlot> result = new HashSet<BodySlot>();

			if (StringUtils.isEmpty(slotList)) {
				return result;
			}

			for (BodySlot slot : values()) {
				if (slotList.contains(slot.toString())) {
					result.add(slot);
				}
			}
			return result;
		}
	}

	/**
	 * Weapons are classified according to the type of damage they deal. Some monsters may be resistant or immune to
	 * attacks from certain types of weapons.
	 */
	public enum DamageType {
		None(0x0), Slash(0x1), Bludge(0x2), Pierce(0x4);

		private int value;

		private DamageType(int value) {
			this.value = value;
		}

		public int value() {
			return value;
		}

		public static DamageType fromValue(int value) {
			for (DamageType slot : DamageType.values()) {
				if (slot.value == value)
					return slot;
			}
			return DamageType.Slash;
		}
	}

	private Set<HeroClass> allowedClasses;
	private Dice damage;
	private Dice damageVsBig;
	private Dice damageVsSmall;
	private DamageType damageType;
	private ScriptInterface<IItem> script;
	private boolean canIdentify;
	private boolean disposed;
	private boolean identified;
	private String identifiedName;
	private String name;
	private ItemType type;
	private Set<BodySlot> slot;
	private int weight;
	private Vector2 critical;
	private int criticalMultiplier;
	private String shortName;
	private boolean big;
	private int speed;
	private byte armorClass;
	private String tileSetName;
	private int tileID;
	private int groundTileID;
	private int incomingTileID;
	private int throwTileID;
	private Set<HeroHand> allowedHands;
	private boolean cursed;
	private boolean twoHanded;
	private int incomingTileId;

	public Item() {
		allowedClasses = new HashSet<HeroClass>();
		allowedClasses.addAll(Arrays.asList(HeroClass.values()));

		damage = new Dice();
		damageVsBig = new Dice();
		damageVsSmall = new Dice();
		damageType = DamageType.None;
		script = new ScriptInterface<IItem>();
		canIdentify = true;

		disposed = false;
	}

	public boolean init() {
		return true;
	}

	public String getName() {
		return identified ? identifiedName : name;
	}

	public void dispose() {
	}

	public boolean save(XmlWriter writer) throws IOException {
		if (writer == null)
			return false;

		writer.element("item").attribute("name", name);

		writer.element("tile").attribute("name", tileSetName).attribute("inventory", tileID)
				.attribute("ground", groundTileID).attribute("incoming", incomingTileId)
				.attribute("moveaway", throwTileID).pop();

		writer.element("type").attribute("value", type).pop();
		writer.element("damagetype").attribute("value", damageType).pop();
		writer.element("ac").attribute("value", armorClass).pop();
		writer.element("slot").attribute("value", slot).pop();
		writer.element("classes").attribute("value", allowedClasses.toArray().toString()).pop();
		writer.element("weight").attribute("value", weight).pop();

		damage.save("damage", writer);
		damageVsBig.save("damagevsbig", writer);
		damageVsSmall.save("damagevssmall", writer);

		writer.element("critical").attribute("min", critical.x).attribute("max", critical.x)
				.attribute("multiplier", criticalMultiplier).pop();

		script.save("script", writer);

		if (cursed)
			writer.element("iscursed").attribute("value", cursed).pop();

		if (identified)
			writer.element("isidentified").attribute("value", identified).pop();

		if (canIdentify)
			writer.element("canidentify").attribute("value", canIdentify).pop();

		if (big)
			writer.element("isbig").attribute("value", big).pop();

		writer.element("allowedhands").attribute("value", allowedHands.toArray().toString()).pop();

		writer.element("shortname", shortName);
		writer.element("identifiedname", identifiedName);
		writer.element("speed").attribute("value", speed).pop();

		writer.pop();

		return true;
	}

	public boolean load(Element xml) {
		if (xml == null)
			return false;

		if (!StringUtils.equals("item", xml.getName()))
			return false;

		String name = xml.getAttribute("name");

		for (int i = 0; i < xml.getChildCount(); i++) {
			Element node = xml.getChild(i);
			if ("script".equalsIgnoreCase(name)) {
				script.load(node);

			} else if ("type".equalsIgnoreCase(name)) {
				type = ItemType.valueOf(node.getAttribute("value"));

			} else if ("slot".equalsIgnoreCase(name)) {
				slot = BodySlot.parse(node.getAttribute("value"));

			} else if ("damagetype".equalsIgnoreCase(name)) {
				damageType = DamageType.fromValue(damageType.value()
						| DamageType.valueOf(node.getAttribute("value")).value());

			} else if ("weight".equalsIgnoreCase(name)) {
				weight = Integer.parseInt(node.getAttribute("value"));

			} else if ("damage".equalsIgnoreCase(name)) {
				damage.load(node);

			} else if ("damagevsbig".equalsIgnoreCase(name)) {
				damageVsBig.load(node);

			} else if ("damagevssmall".equalsIgnoreCase(name)) {
				damageVsSmall.load(node);

			} else if ("critical".equalsIgnoreCase(name)) {
				critical = new Vector2(Integer.parseInt(node.getAttribute("min")), Integer.parseInt(node
						.getAttribute("max")));
				criticalMultiplier = Integer.parseInt(node.getAttribute("multiplier"));

			} else if ("shortname".equalsIgnoreCase(name)) {
				shortName = node.getText();

			} else if ("identifiedname".equalsIgnoreCase(name)) {
				identifiedName = node.getText();

			} else if ("isidentified".equalsIgnoreCase(name)) {
				identified = Boolean.parseBoolean(node.getAttribute("value"));

			} else if ("isbig".equalsIgnoreCase(name)) {
				big = true;

			} else if ("canidentify".equalsIgnoreCase(name)) {
				canIdentify = Boolean.parseBoolean(node.getAttribute("value"));

			} else if ("speed".equalsIgnoreCase(name)) {
				speed = Integer.parseInt(node.getAttribute("value"));

			} else if ("ac".equalsIgnoreCase(name)) {
				armorClass = Byte.parseByte(node.getAttribute("value"));

			} else if ("tile".equalsIgnoreCase(name)) {
				tileSetName = node.getAttribute("name");
				tileID = Integer.parseInt(node.getAttribute("inventory"));
				groundTileID = Integer.parseInt(node.getAttribute("ground"));
				incomingTileID = Integer.parseInt(node.getAttribute("incoming"));
				throwTileID = Integer.parseInt(node.getAttribute("moveaway"));

			} else if ("classes".equalsIgnoreCase(name)) {
				allowedClasses = HeroClass.parse(node.getAttribute("value"));

			} else if ("allowedhands".equalsIgnoreCase(name)) {
				allowedHands = HeroHand.parse(node.getAttribute("value"));

			} else if ("cursed".equalsIgnoreCase(name)) {
				cursed = Boolean.parseBoolean(node.getAttribute("value"));

			} else if ("twohanded".equalsIgnoreCase(name)) {
				twoHanded = Boolean.parseBoolean(node.getAttribute("value"));

			}
		}

		return true;
	}

	public boolean canUseWithHand(HeroHand hand) {
		switch (hand) {
		case Primary:
			return slot.contains(BodySlot.Primary);

		case Secondary:
			return slot.contains(BodySlot.Secondary);
		default:
			return true;
		}
	}
	
	@Override
	public String toString() {
		return name;
	}

	public Dice getDamage() {
		return damage;
	}

	public void setDamage(Dice damage) {
		this.damage = damage;
	}

	public Dice getDamageVsBig() {
		return damageVsBig;
	}

	public void setDamageVsBig(Dice damageVsBig) {
		this.damageVsBig = damageVsBig;
	}

	public Dice getDamageVsSmall() {
		return damageVsSmall;
	}

	public void setDamageVsSmall(Dice damageVsSmall) {
		this.damageVsSmall = damageVsSmall;
	}

	public DamageType getDamageType() {
		return damageType;
	}

	public void setDamageType(DamageType damageType) {
		this.damageType = damageType;
	}

	public ScriptInterface<IItem> getScript() {
		return script;
	}

	public void setScript(ScriptInterface<IItem> script) {
		this.script = script;
	}

	public boolean isCanIdentify() {
		return canIdentify;
	}

	public void setCanIdentify(boolean canIdentify) {
		this.canIdentify = canIdentify;
	}

	public boolean isIdentified() {
		return identified;
	}

	public void setIdentified(boolean identified) {
		this.identified = identified;
	}

	public String getIdentifiedName() {
		return identifiedName;
	}

	public void setIdentifiedName(String identifiedName) {
		this.identifiedName = identifiedName;
	}

	public ItemType getType() {
		return type;
	}

	public void setType(ItemType type) {
		this.type = type;
	}

	public Set<BodySlot> getSlot() {
		return slot;
	}

	public void setSlot(Set<BodySlot> slot) {
		this.slot = slot;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public Vector2 getCritical() {
		return critical;
	}

	public void setCritical(Vector2 critical) {
		this.critical = critical;
	}

	public int getCriticalMultiplier() {
		return criticalMultiplier;
	}

	public void setCriticalMultiplier(int criticalMultiplier) {
		this.criticalMultiplier = criticalMultiplier;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public boolean isBig() {
		return big;
	}

	public void setBig(boolean big) {
		this.big = big;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public byte getArmorClass() {
		return armorClass;
	}

	public void setArmorClass(byte armorClass) {
		this.armorClass = armorClass;
	}

	public String getTileSetName() {
		return tileSetName;
	}

	public void setTileSetName(String tileSetName) {
		this.tileSetName = tileSetName;
	}

	public int getTileID() {
		return tileID;
	}

	public void setTileID(int tileID) {
		this.tileID = tileID;
	}

	public int getGroundTileID() {
		return groundTileID;
	}

	public void setGroundTileID(int groundTileID) {
		this.groundTileID = groundTileID;
	}

	public int getIncomingTileID() {
		return incomingTileID;
	}

	public void setIncomingTileID(int incomingTileID) {
		this.incomingTileID = incomingTileID;
	}

	public int getThrowTileID() {
		return throwTileID;
	}

	public void setThrowTileID(int throwTileID) {
		this.throwTileID = throwTileID;
	}

	public boolean isCursed() {
		return cursed;
	}

	public void setCursed(boolean cursed) {
		this.cursed = cursed;
	}

	public boolean isTwoHanded() {
		return twoHanded;
	}

	public void setTwoHanded(boolean twoHanded) {
		this.twoHanded = twoHanded;
	}

	public int getIncomingTileId() {
		return incomingTileId;
	}

	public void setIncomingTileId(int incomingTileId) {
		this.incomingTileId = incomingTileId;
	}

	public Set<HeroClass> getAllowedClasses() {
		return allowedClasses;
	}

	public boolean isDisposed() {
		return disposed;
	}

	public Set<HeroHand> getAllowedHands() {
		return allowedHands;
	}

	public void setName(String name) {
		this.name = name;
	}

}
