package com.ntk.darkmoor.test.general;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.Test;

import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.ntk.darkmoor.engine.Entity.EntityAlignment;
import com.ntk.darkmoor.engine.Monster;

public class MonsterTest extends BaseTestCase {

	@Test
	public void testLoadMonsterFromXml() throws FileNotFoundException, IOException {
		Element root = loadXml(TEST_RESOURCES + "monster1.xml");
		assertNotNull(root);
		Monster monster1 = new Monster();
		monster1.load(root);

		testMonsterAttributes(monster1);
	}

	private void testMonsterAttributes(Monster monster1) {
		assertNotNull(monster1.getAlignment());
		assertEquals(EntityAlignment.LawfulGood, monster1.getAlignment());
		assertEquals(0, monster1.getAntiFire());
		assertEquals(0, monster1.getAntiMagic());
		assertEquals(6, monster1.getArmorClass());
		assertEquals(2, monster1.getBaseSaveBonus());
		assertEquals(10, monster1.getIntelligence().getValue());
		assertEquals(7, monster1.getCharisma().getValue());
		assertEquals(-1, monster1.getCharisma().getModifier());
		assertEquals(13, monster1.getStrength().getValue());
		assertEquals(11, monster1.getWisdom().getValue());
		assertEquals(11, monster1.getDexterity().getValue());
		assertEquals(10, monster1.getConstitution().getValue());
		assertEquals(24, monster1.getHitPoint().getCurrent());
		assertEquals(24, monster1.getHitPoint().getMax());

	}

	@Test
	public void testSaveMonsterToXml() throws FileNotFoundException, IOException {
		Element root = loadXml(TEST_RESOURCES + "monster1.xml");
		assertNotNull(root);
		Monster monster1 = new Monster();
		monster1.load(root);

		// StringWriter writer = new StringWriter();

		FileWriter writer = new FileWriter(TEST_RESOURCES + "monster1_save.xml");
		XmlWriter xml = new XmlWriter(writer);
		monster1.save(xml);
		xml.close();
		// System.out.println(writer.toString());
		root = loadXml(TEST_RESOURCES + "monster1_save.xml");
		assertNotNull(root);
		Monster monster1_saved = new Monster();
		monster1_saved.load(root);

		compareMonsteres(monster1, monster1_saved);
	}

	private void compareMonsteres(Monster monster1, Monster monster2) {
		assertNotNull(monster1.getAlignment());
		assertEquals(monster1.getAlignment(), monster2.getAlignment());
		assertEquals(monster1.getAntiFire(), monster2.getAntiFire());
		assertEquals(monster1.getAntiMagic(), monster2.getAntiMagic());
		assertEquals(monster1.getArmorClass(), monster2.getArmorClass());
		assertEquals(monster1.getHitPoint().getCurrent(), monster2.getHitPoint().getCurrent());
		assertEquals(monster1.getHitPoint().getMax(), monster2.getHitPoint().getMax());
		assertEquals(monster1.getHitPoint().getPeril(), monster2.getHitPoint().getPeril());
		assertEquals(monster1.getBaseSaveBonus(), monster2.getBaseSaveBonus());
		assertEquals(monster1.getCharisma().getValue(), monster2.getCharisma().getValue());
		assertEquals(monster1.getCharisma().getModifier(), monster2.getCharisma().getModifier());
		assertEquals(monster1.getStrength().getValue(), monster2.getStrength().getValue());
		assertEquals(monster1.getWisdom().getValue(), monster2.getWisdom().getValue());
		assertEquals(monster1.getDexterity().getValue(), monster2.getDexterity().getValue());
		assertEquals(monster1.getConstitution().getValue(), monster2.getConstitution().getValue());

		assertEquals(monster1.getAttackSpeed(), monster2.getAttackSpeed());
		assertEquals(monster1.getLocation(), monster2.getLocation());
		assertEquals(monster1.getDirection(), monster2.getDirection());
		assertEquals(monster1.getTile(), monster2.getTile());
		assertEquals(monster1.getItemsInPocket().size(), monster2.getItemsInPocket().size());
		// assertEquals(monster1.getScript(), monster2.getScript());
		assertEquals(monster1.getDamageDice().getFaces(), monster2.getDamageDice().getFaces());
		assertEquals(monster1.getDamageDice().getDiceThrows(), monster2.getDamageDice().getDiceThrows());
		assertEquals(monster1.getDamageDice().getModifier(), monster2.getDamageDice().getModifier());
		assertEquals(monster1.getMagicCastingLevel(), monster2.getMagicCastingLevel());
		assertEquals(monster1.getStealRate(), monster2.getStealRate(), 0.01);
		assertEquals(monster1.getPickupRate(), monster2.getPickupRate(), 0.01);
		assertEquals(monster1.getBaseAttack(), monster2.getBaseAttack(), 0.01);
		assertEquals(monster1.getDefaultBehaviour(), monster2.getDefaultBehaviour());
		assertEquals(monster1.getCurrentBehaviour(), monster2.getCurrentBehaviour());
		assertEquals(monster1.isNonMaterial(), monster2.isNonMaterial());
		assertEquals(monster1.isPoisonImmunity(), monster2.isPoisonImmunity());
		assertEquals(monster1.isCanSeeInvisible(), monster2.isCanSeeInvisible());
		assertEquals(monster1.isBackRowAttack(), monster2.isBackRowAttack());
		assertEquals(monster1.isTeleports(), monster2.isTeleports());
		assertEquals(monster1.isUsesStairs(), monster2.isUsesStairs());
		assertEquals(monster1.isFleesAfterAttack(), monster2.isFleesAfterAttack());
		assertEquals(monster1.hasDrainMagic(), monster2.hasDrainMagic());
		assertEquals(monster1.hasHealMagic(), monster2.hasHealMagic());
		assertEquals(monster1.hasThrowWeapons(), monster2.hasThrowWeapons());
		assertEquals(monster1.isFlying(), monster2.isFlying());
		assertEquals(monster1.isSmartAI(), monster2.isSmartAI());
		assertEquals(monster1.getWeaponName(), monster2.getWeaponName());
	}
}
