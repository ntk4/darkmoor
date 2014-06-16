package com.ntk.darkmoor.engine;

import java.util.HashMap;
import java.util.Map;

import com.ntk.darkmoor.engine.Hero.HeroClass;
import com.ntk.darkmoor.engine.Hero.HeroRace;

public class HeroAllowedClasses {
	public static final Map<HeroRace, int[]> allowedClass = new HashMap<HeroRace, int[]>();

	static {
		allowedClass.put(HeroRace.Human, new int[] {
			HeroClass.Fighter.value(),
			HeroClass.Ranger.value(),
			HeroClass.Mage.value(),
			HeroClass.Paladin.value(),
			HeroClass.Cleric.value(),
			HeroClass.Thief.value(),
		});

		allowedClass.put(HeroRace.Elf, new int[] {
			HeroClass.Fighter.value(),
			HeroClass.Ranger.value(),
			HeroClass.Mage.value(),
			HeroClass.Cleric.value(),
			HeroClass.Thief.value(),
			HeroClass.Fighter.value() | HeroClass.Thief.value(),
			HeroClass.Fighter.value() | HeroClass.Mage.value(),
			HeroClass.Fighter.value() | HeroClass.Mage.value() | HeroClass.Thief.value(),
			HeroClass.Thief.value() | HeroClass.Mage.value(),
		});

		allowedClass.put(HeroRace.HalfElf, new int[] {
			HeroClass.Fighter.value(),
			HeroClass.Ranger.value(),
			HeroClass.Mage.value(),
			HeroClass.Cleric.value(),
			HeroClass.Thief.value(),
			HeroClass.Fighter.value() | HeroClass.Cleric.value(),
			HeroClass.Fighter.value() | HeroClass.Thief.value(),
			HeroClass.Fighter.value() | HeroClass.Mage.value(),
			HeroClass.Fighter.value() | HeroClass.Mage.value() | HeroClass.Thief.value(),
			HeroClass.Thief.value() | HeroClass.Mage.value(),
			HeroClass.Fighter.value() | HeroClass.Cleric.value() | HeroClass.Mage.value(),
			HeroClass.Ranger.value() | HeroClass.Cleric.value(),
			HeroClass.Cleric.value() | HeroClass.Mage.value()
		});

		allowedClass.put(HeroRace.Dwarf, new int[] {
			HeroClass.Fighter.value(),
			HeroClass.Cleric.value(),
			HeroClass.Thief.value(),
			HeroClass.Fighter.value() | HeroClass.Cleric.value(),
			HeroClass.Fighter.value() | HeroClass.Thief.value()
		});

		allowedClass.put(HeroRace.Gnome, new int[] {
			HeroClass.Fighter.value(),
			HeroClass.Cleric.value(),
			HeroClass.Thief.value(),
			HeroClass.Fighter.value() | HeroClass.Cleric.value(),
			HeroClass.Fighter.value() | HeroClass.Thief.value(),
			HeroClass.Cleric.value() | HeroClass.Thief.value()
		});

		allowedClass.put(HeroRace.Halfling, new int[] {
			HeroClass.Fighter.value(),
			HeroClass.Cleric.value(),
			HeroClass.Thief.value(),
			HeroClass.Fighter.value() | HeroClass.Thief.value()
		});
	}
}
