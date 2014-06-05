package com.ntk.darkmoor.engine;

import java.io.IOException;
import java.util.Set;

import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.ntk.darkmoor.engine.interfaces.Spell.SpellRange;
import com.ntk.darkmoor.stub.Hero.HeroClass;
import com.ntk.darkmoor.stub.ScriptInterface;

/**
 * http://dmreference.com/SRD/Magic.htm http://www.d20srd.org/srd/magicOverview/spellDescriptions.htm
 * 
 * @author Nick
 * 
 */
public class SpellImpl {
	public static final String TAG = "spell";
	
	private boolean disposed;
	private ScriptInterface<SpellImpl> script;
	private int level;
	private String name;
	private Set<HeroClass> heroClass;
	private SpellRange range;
	private int duration;
	private int castingTime;
	private String description;

	public SpellImpl() {
		disposed = false;
		script = new ScriptInterface<SpellImpl>();
		level = 1;
	}

	public boolean init() {
		return true;
	}

	public void dispose() {
	}

	@Override
	public String toString() {
		return String.format("%s, %d level %d", name, heroClass, level);
	}

	public boolean load(Element xml) {
		if (xml == null)
			return false;

		Element node = null;
		String name = null;

		this.name = xml.getAttribute("name");

		for (int i = 0; i < xml.getChildCount(); i++) {
			node = xml.getChild(i);
			name = node.getName();

			if ("range".equalsIgnoreCase(name)) {
				range = SpellRange.valueOf(node.getAttribute("value"));

			} else if ("class".equalsIgnoreCase(name)) {
				heroClass = HeroClass.parse(node.getAttribute("value"));

			} else if ("level".equalsIgnoreCase(name)) {
				level = Integer.parseInt(node.getAttribute("value"));

			} else if ("duration".equalsIgnoreCase(name)) {
				duration = Integer.parseInt(node.getAttribute("value"));

			} else if ("castingtime".equalsIgnoreCase(name)) {
				castingTime = Integer.parseInt(node.getAttribute("value"));

			} else if ("description".equalsIgnoreCase(name)) {
				description = node.getText();

			} else if ("script".equalsIgnoreCase(name)) {
				script.load(node);
			}

		}

		return true;
	}

	public boolean save(XmlWriter writer) throws IOException {
		if (writer == null)
			return false;

		writer.element("spell").attribute("name", name);

		writer.element("description", description);
		writer.element("duration").attribute("name", duration).pop();
		writer.element("castingtime").attribute("value", castingTime).pop();
		writer.element("class").attribute("value", heroClass).pop();
		writer.element("range").attribute("value", range).pop();
		writer.element("level").attribute("value", level).pop();

		script.save("script", writer);

		writer.pop();

		return true;
	}

	public static int getClericSpellBonus(int wisdom, int level) {
		int[] bonus = new int[] {//@formatter:off
			1, 0, 0, 0, 0, 0,
			2, 0, 0, 0, 0, 0,
			2, 1, 0, 0, 0, 0,
			2, 2, 0, 0, 0, 0,
			2, 2, 1, 0, 0, 0,
			2, 2, 1, 1, 0, 0,
			3, 2, 1, 2, 0, 0,
		};//@formatter:on

		// Not more than level 6
		if (level <= 0 || level > 6)
			return 0;

		// No bonus under wisdom level 13
		if (wisdom < 13)
			return 0;

		// Max wisdom 19
		wisdom = Math.min(19, wisdom);

		return bonus[(wisdom - 13) * 6 + (level - 1)];
	}

	public static int getSpellProgression(HeroClass type, int wisdom, int level) {
		// Not more than level 6
		if (level <= 0 || level > 6)
			return 0;

		switch (type) {
		case Paladin: {
			int[] bonus = new int[] {//@formatter:off
					1,0,0,
					2,0,0,
					2,1,0,
					2,2,0,
					2,2,1,
				};//@formatter:on

			// Level 3 maximum
			if (level > 3 || wisdom < 9)
				return 0;

			// Max level 13
			wisdom = Math.min(wisdom, 13);

			return bonus[(wisdom - 10) * 3 + (level - 1)];
		}

		case Mage: {
			int[] bonus = new int[] {//@formatter:off
					1,0,0,0,0,0,
					2,0,0,0,0,0,
					2,1,0,0,0,0,
					3,2,0,0,0,0,
					4,2,1,0,0,0,
					4,2,2,0,0,0,
					4,3,2,1,0,0,
					4,3,3,2,0,0,
					4,3,3,2,1,0,
					4,4,3,2,2,0,
					4,4,4,3,3,0,
					4,4,4,4,4,1,
					5,5,5,4,4,2,
				};//@formatter:on

			// Max level 13
			wisdom = Math.min(wisdom, 13);

			return bonus[(wisdom - 1) * 6 + (level - 1)];
		}

		case Cleric: {
			int[] bonus = new int[] {//@formatter:off
					1,0,0,0,0,0,
					2,0,0,0,0,0,
					2,1,0,0,0,0,
					3,2,0,0,0,0,
					3,3,1,0,0,0,
					3,3,2,0,0,0,
					3,3,2,1,0,0,
					3,3,3,2,0,0,
					4,4,3,2,1,0,
					4,4,3,3,2,0,
					5,4,3,3,2,0,
					6,5,5,3,2,2,
					6,6,6,4,2,2,
				};//@formatter:on

			// Max level 13
			wisdom = Math.min(wisdom, 13);

			return bonus[(wisdom - 1) * 6 + (level - 1)];
		}
		default:
			break;
		}
		return 0;
	}

	public boolean isDisposed() {
		return disposed;
	}

	public ScriptInterface<SpellImpl> getScript() {
		return script;
	}

	public int getLevel() {
		return level;
	}

	public String getName() {
		return name;
	}

	public Set<HeroClass> getHeroClass() {
		return heroClass;
	}

	public SpellRange getRange() {
		return range;
	}

	public int getDuration() {
		return duration;
	}

	public int getCastingTime() {
		return castingTime;
	}

	public String getDescription() {
		return description;
	}
}
