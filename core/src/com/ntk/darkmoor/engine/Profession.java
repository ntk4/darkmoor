package com.ntk.darkmoor.engine;

import java.io.IOException;

import com.badlogic.gdx.utils.XmlWriter;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.ntk.darkmoor.engine.Hero.HeroClass;

public class Profession {

	public final String TAG = "profession";

	private int experience;
	private HeroClass heroClass;

	public Profession(Element node) {
		load(node);
	}

	public Profession(int xp, HeroClass heroClass) {
		experience = Math.max(1, xp);
		this.heroClass = heroClass;
	}

	@Override
	public String toString() {
		return String.format("%s, level %d (XP=%d)", heroClass.toString(), getLevel(), experience);
	}

	public boolean addXP(int amount) {
		int newlevel = this.getLevel();

		experience += amount;
		
		return newlevel != getLevel();
	}

	public boolean load(Element xml) {
		if (xml == null)
			return false;

		Element node = null;
		String name = null;

		for (int i = 0; i < xml.getChildCount(); i++) {
			node = xml.getChild(i);
			name = node.getName();

			if ("class".equalsIgnoreCase(name)) {
				heroClass = HeroClass.valueOf(node.getAttribute("name"));

			} else if ("xp".equalsIgnoreCase(name)) {
				if (node.getAttribute("points") != null)
					experience = Integer.parseInt(node.getAttribute("points"));
			}

		}

		return true;
	}

	public boolean save(XmlWriter writer) throws IOException {
		if (writer == null)
			return false;

		writer.element(TAG );
		
		writer.element("class").attribute("name", heroClass.toString()).pop();
		writer.element("xp").attribute("points", experience).pop();

		writer.pop();

		return true;
	}

	public int getExperience() {
		return experience;
	}

	public void setExperience(int experience) {
		this.experience = experience;
	}

	public HeroClass getHeroClass() {
		return heroClass;
	}

	public void setHeroClass(HeroClass heroClass) {
		this.heroClass = heroClass;
	}

	public int getLevel() {
		
		int[] data = null;

		switch (heroClass)
		{
			case Fighter:
			data = HeroExperienceLevels.Fighter;
			break;
			case Ranger:
			data = HeroExperienceLevels.Ranger;
			break;
			case Paladin:
			data = HeroExperienceLevels.Paladin;
			break;
			case Mage:
			data = HeroExperienceLevels.Mage;
			break;
			case Cleric:
			data = HeroExperienceLevels.Cleric;
			break;
			case Thief:
			data = HeroExperienceLevels.Thief;
			break;
			default:
				return 0;
		}

		for (int i = data.length; i > 0; i--)
		{
			if (experience >= data[i - 1])
				return i;
		}

		return 0;
	}
}
