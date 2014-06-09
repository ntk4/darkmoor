package com.ntk.darkmoor.test.general;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.Test;

import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.ntk.darkmoor.engine.Entity.EntityAlignment;
import com.ntk.darkmoor.engine.Hero;
import com.ntk.darkmoor.engine.Hero.HeroClass;
import com.ntk.darkmoor.engine.Hero.InventoryPosition;

public class HeroTest extends BaseTestCase {


	@Test
	public void testLoadHeroFromXml() throws FileNotFoundException, IOException {
		Element root = loadXml(TEST_RESOURCES + "hero1.xml");
		assertNotNull(root);
		Hero hero1 = new Hero();
		hero1.load(root);

		testHeroAttributes(hero1);
	}

	private void testHeroAttributes(Hero hero1) {
		assertNotNull(hero1.getAlignment());
		assertEquals(EntityAlignment.NeutralGood, hero1.getAlignment());
		assertEquals(0, hero1.getAntiFire());
		assertEquals(0, hero1.getAntiMagic());
		assertEquals(0, hero1.getArmorBonus());
		assertEquals(10, hero1.getArmorClass());
		assertEquals(2, hero1.getAttacks().length);
		assertNotNull(hero1.getBackPack());
		assertEquals(12, hero1.getBaseAttackBonus());
		assertEquals(7, hero1.getBaseSaveBonus());
		assertEquals(13, hero1.getCharisma().getValue());
		assertEquals(1, hero1.getCharisma().getModifier());
		assertEquals(12, hero1.getStrength().getValue());
		assertEquals(13, hero1.getWisdom().getValue());
		assertEquals(14, hero1.getIntelligence().getValue());
		assertEquals(8, hero1.getDexterity().getValue());
		assertEquals(10, hero1.getConstitution().getValue());
		assertEquals(100, hero1.getFood());
		assertEquals(0, hero1.getDodgeBonus());
		assertEquals(2, hero1.getHandActions().length);
		assertEquals(16, hero1.getHitPoint().getCurrent());
		assertEquals(23, hero1.getHitPoint().getMax());

		assertNotNull(hero1.getProfessions());
		assertNotNull(hero1.getProfession(HeroClass.Fighter));
		assertNotNull(hero1.getProfession(HeroClass.Paladin));
		assertEquals(98324, hero1.getProfession(HeroClass.Fighter).getExperience());
		assertEquals(32505, hero1.getProfession(HeroClass.Paladin).getExperience());
	}

	@Test(expected=AssertionError.class) //it should work as soon as items are loaded as resources
	public void testLoadHeroItemsFromXml() throws FileNotFoundException, IOException {
		Element root = loadXml(TEST_RESOURCES + "hero1.xml");
		assertNotNull(root);
		Hero hero1 = new Hero();
		hero1.load(root);
		assertNotNull(hero1.getWaistPack());
		assertEquals(3, hero1.getWaistPack().length);
		assertEquals(null, hero1.getWaistPackItem(0).getName());
		assertEquals(null, hero1.getWaistPackItem(1).getName());
		assertEquals(null, hero1.getWaistPackItem(2).getName());
		assertEquals("short sword", hero1.getInventoryItem(InventoryPosition.Primary));
	}
	
	@Test
	public void testSaveHeroToXml() throws FileNotFoundException, IOException {
		Element root = loadXml(TEST_RESOURCES + "hero1.xml");
		assertNotNull(root);
		Hero hero1 = new Hero();
		hero1.load(root);
		
//		 StringWriter writer = new StringWriter();
		 
		 FileWriter writer = new FileWriter(TEST_RESOURCES + "hero1_save.xml");
		 XmlWriter xml = new XmlWriter(writer);
		hero1.save(xml);
		xml.close();
//		System.out.println(writer.toString());
		root = loadXml(TEST_RESOURCES + "hero1_save.xml");
		assertNotNull(root);
		Hero hero1_saved = new Hero();
		hero1_saved.load(root);
		
		compareHeroes(hero1,hero1_saved);
	}

	private void compareHeroes(Hero hero1, Hero hero2) {
		assertNotNull(hero1.getAlignment());
		assertEquals(hero1.getAlignment(), hero2.getAlignment());
		assertEquals(hero1.getAntiFire(), hero2.getAntiFire());
		assertEquals(hero1.getAntiMagic(), hero2.getAntiMagic());
		assertEquals(hero1.getArmorBonus(), hero2.getArmorBonus());
		assertEquals(hero1.getArmorClass(), hero2.getArmorClass());
		assertEquals(hero1.getAttacks().length, hero2.getAttacks().length);
		assertNotNull(hero1.getBackPack());
		assertNotNull(hero2.getBackPack());
		assertEquals(hero1.getHitPoint().getCurrent(), hero2.getHitPoint().getCurrent());
		assertEquals(hero1.getHitPoint().getMax(), hero2.getHitPoint().getMax());
		assertEquals(hero1.getHitPoint().getPeril(), hero2.getHitPoint().getPeril());
		assertEquals(hero1.getBaseAttackBonus(), hero2.getBaseAttackBonus());
		assertEquals(hero1.getBaseSaveBonus(), hero2.getBaseSaveBonus());
		assertEquals(hero1.getCharisma().getValue(), hero2.getCharisma().getValue());
		assertEquals(hero1.getCharisma().getModifier(), hero2.getCharisma().getModifier());
		assertEquals(hero1.getStrength().getValue(), hero2.getStrength().getValue());
		assertEquals(hero1.getWisdom().getValue(), hero2.getWisdom().getValue());
		assertEquals(hero1.getIntelligence().getValue(), hero2.getIntelligence().getValue());
		assertEquals(hero1.getDexterity().getValue(), hero2.getDexterity().getValue());
		assertEquals(hero1.getConstitution().getValue(), hero2.getConstitution().getValue());
		assertEquals(hero1.getFood(), hero2.getFood());
		assertEquals(hero1.getDodgeBonus(), hero2.getDodgeBonus());
		assertEquals(hero1.getHandActions().length, hero2.getHandActions().length);
		assertEquals(hero1.getHitPoint().getCurrent(), hero2.getHitPoint().getCurrent());
		assertEquals(hero1.getHitPoint().getMax(), hero2.getHitPoint().getMax());

		assertNotNull(hero1.getProfessions());
		assertEquals(hero1.getProfessionsCount(), hero2.getProfessionsCount());
		for (int i=0;i<hero1.getProfessionsCount();i++) {
			assertEquals(hero1.getProfession(i).getHeroClass(), hero2.getProfession(i).getHeroClass());
			assertEquals(hero1.getProfession(i).getLevel(), hero2.getProfession(i).getLevel());
			assertEquals(hero1.getProfession(i).getExperience(), hero2.getProfession(i).getExperience());
		}
	}
}
