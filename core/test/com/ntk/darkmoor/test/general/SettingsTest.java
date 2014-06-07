package com.ntk.darkmoor.test.general;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.junit.Test;

import com.ntk.darkmoor.config.Settings;

public class SettingsTest {

	private static final String SETTINGS_TEMP = "temp.properties";
	private static final String SETTINGS_CORRECT = "settings_correct.properties";
	private static final String FOLDER_TEST_RESOURCES = "test/test-resources/";

	@Test
	public void thatSettingsAreLoaded() throws IOException {
		Settings s = Settings.loadSettings(FOLDER_TEST_RESOURCES, SETTINGS_CORRECT);

		assertNotNull(s);
		assertEquals("qwerty", s.getInputScheme());
		assertEquals("English", s.getLanguage());
		assertEquals(false, s.isHPAsBar());
		assertEquals(true, s.isMusic());
		assertEquals(true, s.isEffects());
		assertEquals(false, s.isFullScreen());
		assertEquals(6, s.getSaveSlots());
	}

	@Test
	public void thatSettingsAreStoredAndLoaded() throws IOException {
		Settings s = Settings.loadSettings(FOLDER_TEST_RESOURCES, SETTINGS_CORRECT);

		assertNotNull(s);
		
		s.saveSettings(FOLDER_TEST_RESOURCES, SETTINGS_TEMP);
		File tempFile = new File(FOLDER_TEST_RESOURCES, SETTINGS_TEMP);
		assertTrue(tempFile.exists());

		Settings s2 = Settings.loadSettings(FOLDER_TEST_RESOURCES, SETTINGS_TEMP);
		assertNotNull(s);
		assertEquals(s.getInputScheme(), s2.getInputScheme());
		assertEquals(s.getLanguage(), s2.getLanguage());
		assertEquals(s.isHPAsBar(), s2.isHPAsBar());
		assertEquals(s.isMusic(), s2.isMusic());
		assertEquals(s.isEffects(), s2.isEffects());
		assertEquals(s.isFullScreen(), s2.isFullScreen());
		assertEquals(s.getSaveSlots(), s2.getSaveSlots());
		
		tempFile.delete();
	}
	
	@Test
	public void thatDefaultsAreLoadedOnError() throws IOException {
		Settings s = new Settings(new Properties());
		
		s.setEffects(false);
		s.setMusic(false);
		s.setFullScreen(false);
		s.setHPAsBar(true);
		s.setInputScheme("error");
		s.setLanguage("error");
		s.setSaveSlots(-1);

		assertEquals("qwerty", s.getInputScheme());
		assertEquals("English", s.getLanguage());
		assertEquals(true, s.isHPAsBar());
		assertEquals(false, s.isMusic());
		assertEquals(false, s.isEffects());
		assertEquals(false, s.isFullScreen());
		assertEquals(6, s.getSaveSlots());
	}
}
