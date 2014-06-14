package com.ntk.darkmoor.test.general;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;

import com.ntk.darkmoor.config.LanguagesManager;

public class LanguagesManagerTest extends BaseTestCase {

	LanguagesManager lang = null;

	@Test
	public void testIntroStringsEnglish() throws FileNotFoundException, IOException {
		lang = LanguagesManager.getInstance(TEST_RESOURCES + "StringTable.xml");
		assertEquals("Their fate is sealed.", lang.getString("English", "Intro", 9));
		assertEquals("Give call to the heroes of the land, andlet us choose our champions.",
				lang.getString("English", "Intro", 2));

	}

	@Test
	public void testIntroStringsFrench() throws FileNotFoundException, IOException {
		lang = LanguagesManager.getInstance(TEST_RESOURCES + "StringTable.xml");
		assertEquals("Leur sort est scellé.", lang.getString("Francais", "Intro", 9));
		assertEquals("Donnez appel aux heros de la terre, etlaissez-nous choisir nos champions.",
				lang.getString("Francais", "Intro", 2));

	}

	@Test
	public void testSpellsStringsEnglish() throws FileNotFoundException, IOException {
		lang = LanguagesManager.getInstance(TEST_RESOURCES + "StringTable.xml");
		assertEquals("Magic Missile", lang.getString("English", "Spells", 4));
		assertEquals("Burning Hands", lang.getString("English", "Spells", 2));

	}

	@Test
	public void testSpellsStringsFrench() throws FileNotFoundException, IOException {
		lang = LanguagesManager.getInstance(TEST_RESOURCES + "StringTable.xml");
		assertEquals("Missile magique", lang.getString("French", "Spells", 4));
		assertEquals("Mains brulantes", lang.getString("French", "Spells", 2));

	}
	
	@Test
	public void testCampStringsEnglish() throws FileNotFoundException, IOException {
		lang = LanguagesManager.getInstance(TEST_RESOURCES + "StringTable.xml");
		assertEquals("Scribe Scrolls", lang.getString("English", "Camp", 4));
		assertEquals("Memorize Spells", lang.getString("English", "Camp", 2));

	}

	@Test
	public void testCampStringsFrench() throws FileNotFoundException, IOException {
		lang = LanguagesManager.getInstance(TEST_RESOURCES + "StringTable.xml");
		assertEquals("Ecritures", lang.getString("Francais", "Camp", 4));
		assertEquals("Apprendre des sorts", lang.getString("Francais", "Camp", 2));

	}
	
	@Test
	public void testChargenStringsEnglish() throws FileNotFoundException, IOException {
		lang = LanguagesManager.getInstance(TEST_RESOURCES + "StringTable.xml");
		assertEquals("RANGER", lang.getString("English", "Chargen", 4));
		assertEquals("SELECT CLASS :", lang.getString("English", "Chargen", 2));

	}

	@Test
	public void testChargenStringsFrench() throws FileNotFoundException, IOException {
		lang = LanguagesManager.getInstance(TEST_RESOURCES + "StringTable.xml");
		assertEquals("RANGER", lang.getString("Francais", "Chargen", 4));
		assertEquals("CLASSE DU HERO :", lang.getString("Francais", "Chargen", 2));

	}

}
