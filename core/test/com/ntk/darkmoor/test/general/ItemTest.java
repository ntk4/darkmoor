package com.ntk.darkmoor.test.general;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.Test;

import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.ntk.darkmoor.engine.Hero.HeroClass;
import com.ntk.darkmoor.engine.Hero.HeroHand;
import com.ntk.darkmoor.engine.Item;
import com.ntk.darkmoor.engine.Item.BodySlot;
import com.ntk.darkmoor.engine.Item.ItemType;
import com.ntk.darkmoor.resource.ItemAssets;

public class ItemTest extends BaseTestCase {

	@Test
	public void testItemAssets() throws FileNotFoundException, IOException {
		ItemAssets.getInstance(TEST_RESOURCES + "Item.xml"); //initialization only
		Item boots = ItemAssets.getItem("boots");
		testItemAttributes(boots);
	}
	
	@Test
	public void testLoadItemFromXml() throws FileNotFoundException, IOException {
		Element root = loadXml(TEST_RESOURCES + "item1.xml");
		assertNotNull(root);
		Item item1 = new Item();
		item1.load(root);

		testItemAttributes(item1);
	}

	private void testItemAttributes(Item item1) {
		assertNotNull(item1);
		assertNotNull(item1.getAllowedClasses());
		assertEquals(3, item1.getAllowedClasses().size());
		assertTrue(item1.getAllowedClasses().contains(HeroClass.Thief));
		assertTrue(item1.getAllowedClasses().contains(HeroClass.Ranger));
		assertTrue(item1.getAllowedClasses().contains(HeroClass.Mage));
		assertEquals("boots",item1.getName());
		assertEquals("Items", item1.getTextureSetName());
		assertEquals(13, item1.getIncomingTextureID());
		assertEquals(13, item1.getGroundTileID());
		assertEquals(13, item1.getThrowTextureID());
		assertEquals(103, item1.getTextureID());
		assertEquals(ItemType.Adornment, item1.getType());
		assertEquals(0, item1.getArmorClass());
		assertNotNull(item1.getSlot());
		assertTrue(item1.getSlot().contains(BodySlot.Feet));
		assertEquals(1, item1.getSlot().size());
		assertEquals(2000, item1.getWeight());
		assertEquals(1, item1.getDamage().getDiceThrows());
		assertEquals(0, item1.getDamage().getFaces());
		assertEquals(0, item1.getDamage().getModifier());
		assertEquals(0, item1.getDamageVsSmall().getDiceThrows());
		assertEquals(1, item1.getDamageVsSmall().getFaces());
		assertEquals(0, item1.getDamageVsSmall().getModifier());
		assertEquals(0, item1.getDamageVsBig().getDiceThrows());
		assertEquals(1, item1.getDamageVsBig().getFaces());
		assertEquals(0, item1.getDamageVsBig().getModifier());
		assertEquals(0, (int)item1.getCritical().x);
		assertEquals(0, (int)item1.getCritical().y);
		assertEquals(0, item1.getCriticalMultiplier());
		assertNotNull(item1.getScript());
		assertEquals(null, item1.getScript().getInstance());
		assertTrue(item1.getAllowedHands().contains(HeroHand.Primary));
		assertEquals("boots",item1.getShortName());
		assertEquals("The Super Boots!",item1.getIdentifiedName());
		assertEquals(1000, item1.getAttackSpeed());
	}
	
	@Test
	public void testSaveItemToXml() throws FileNotFoundException, IOException {
		Element root = loadXml(TEST_RESOURCES + "item1.xml");
		assertNotNull(root);
		Item item1 = new Item();
		item1.load(root);

		// StringWriter writer = new StringWriter();

		FileWriter writer = new FileWriter(TEST_RESOURCES + "item1_save.xml");
		XmlWriter xml = new XmlWriter(writer);
		item1.save(xml);
		xml.close();
		// System.out.println(writer.toString());
		root = loadXml(TEST_RESOURCES + "item1_save.xml");
		assertNotNull(root);
		Item item1_saved = new Item();
		item1_saved.load(root);

		compareItemes(item1, item1_saved);
	}

	private void compareItemes(Item item1, Item item2) {
		assertNotNull(item1);
		assertNotNull(item2);
		assertEquals(item1.getAllowedClasses(), item2.getAllowedClasses());
		assertEquals(item1.getName(),item2.getName());
		assertEquals(item1.getTextureSetName(), item2.getTextureSetName());
		assertEquals(item1.getIncomingTextureID(), item2.getIncomingTextureID());
		assertEquals(item1.getGroundTileID(), item2.getGroundTileID());
		assertEquals(item1.getThrowTextureID(), item2.getThrowTextureID());
		assertEquals(item1.getTextureID(), item2.getTextureID());
		assertEquals(item1.getType(), item2.getType());
		assertEquals(item1.getArmorClass(), item2.getArmorClass());
		assertEquals(item1.getSlot(), item2.getSlot());
		assertEquals(item1.getWeight(), item2.getWeight());
		assertEquals(item1.getDamage().getDiceThrows(), item2.getDamage().getDiceThrows());
		assertEquals(item1.getDamage().getFaces(), item2.getDamage().getFaces());
		assertEquals(item1.getDamage().getModifier(), item2.getDamage().getModifier());
		assertEquals(item1.getDamageVsSmall().getDiceThrows(), item2.getDamageVsSmall().getDiceThrows());
		assertEquals(item1.getDamageVsSmall().getFaces(), item2.getDamageVsSmall().getFaces());
		assertEquals(item1.getDamageVsSmall().getModifier(), item2.getDamageVsSmall().getModifier());
		assertEquals(item1.getDamageVsBig().getDiceThrows(), item2.getDamageVsBig().getDiceThrows());
		assertEquals(item1.getDamageVsBig().getFaces(), item2.getDamageVsBig().getFaces());
		assertEquals(item1.getDamageVsBig().getModifier(), item2.getDamageVsBig().getModifier());
		assertEquals((int)item1.getCritical().x, (int)item2.getCritical().x);
		assertEquals((int)item1.getCritical().y, (int)item2.getCritical().y);
		assertEquals(item1.getCriticalMultiplier(), item2.getCriticalMultiplier());
		assertEquals(item1.getScript().getInstance(), item2.getScript().getInstance());
		assertEquals(item1.getAllowedHands(), item2.getAllowedHands());
		assertEquals(item1.getShortName(),item2.getShortName());
		assertEquals(item1.getIdentifiedName(),item2.getIdentifiedName());
		assertEquals(item1.getAttackSpeed(), item2.getAttackSpeed());
	}
}
