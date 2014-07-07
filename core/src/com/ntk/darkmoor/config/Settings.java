package com.ntk.darkmoor.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import com.badlogic.gdx.files.FileHandle;
import com.ntk.darkmoor.exception.InitializationException;
import com.ntk.darkmoor.exception.SaveException;

public class Settings {

	// setting labels in the properties file
	private static final String SETTING_FULL_SCREEN = "FullScreen";
	private static final String SETTING_EFFECTS = "Effects";
	private static final String SETTING_MUSIC = "Music";
	private static final String SETTING_HP_AS_BAR = "HPAsBar";
	private static final String SETTING_LANGUAGE = "language";
	private static final String SETTING_INPUTSCHEME = "inputscheme";
	private static final String SETTING_SAVE_SLOTS = "saveSlots";
	
	// last loaded instance. (almost) a singleton
	private static Settings instance;

	// private members for the settings
	private String inputScheme;
	private String language;
	private boolean HPAsBar;
	private boolean music;
	private boolean effects;
	private boolean fullScreen;
	private int saveSlots;

	// the properties that hold all settings
	private Properties props;
	private File settingsFile;

	public static Settings loadSettings(String path, String file) throws IOException {
		return loadSettings(new FileHandle(new File(path + file)));
	}
	
	public static Settings loadSettings(FileHandle handle) throws IOException {

		final Properties props = new Properties();
		
		props.load(handle.read());

		Settings settings = new Settings(props);
		settings.settingsFile = handle.file();
		
		settings.setInputScheme(settings.extractStringProperty(SETTING_INPUTSCHEME, "qwerty", InputType.stringValues()));
		settings.setLanguage(settings.extractStringProperty(SETTING_LANGUAGE, "English", GameLanguage.stringValues()));
		settings.setHPAsBar(settings.extractBooleanProperty(SETTING_HP_AS_BAR, false));
		settings.setMusic(settings.extractBooleanProperty(SETTING_MUSIC, true));
		settings.setEffects(settings.extractBooleanProperty(SETTING_EFFECTS, true));
		settings.setFullScreen(settings.extractBooleanProperty(SETTING_FULL_SCREEN, false));
		settings.setSaveSlots(settings.extractIntProperty(SETTING_SAVE_SLOTS, 4));
		
		instance = settings;
		
		return settings;

	}

	public void saveSettings() throws IOException {
		saveSettings(settingsFile.getPath(), settingsFile.getName());
	}
	
	public void saveSettings(String path, String settingsFile) throws IOException {
		props.store(new FileOutputStream(path + settingsFile), "");
	}

	public static void save() {
		try {
			Settings.getLastLoadedInstance().saveSettings();
		} catch (IOException e) {
			throw new SaveException(e);
		}
		
	}
	
	public Settings(Properties props) {
		this.props = props;
		if (props == null) {
			throw new InitializationException("Settings file is empty");
		}
	}

	private boolean extractBooleanProperty(String setting, boolean defaultValue) {
		if (props.get(setting) != null
				&& ("true".equalsIgnoreCase(props.getProperty(setting)) || "false".equalsIgnoreCase(props
						.getProperty(setting))))
			return Boolean.valueOf(props.get(setting).toString());
		else
			return defaultValue;
	}

	private String extractStringProperty(String setting, String defaultValue, String[] validValues) {
		if (props.get(setting) != null) {
			String value = (String) props.get(setting);
			return validatePropertyValue(validValues, defaultValue, value);
		}
		return defaultValue;
	}
	
	private int extractIntProperty(String setting, int defaultValue) {
		if (props.get(setting) != null) {
			String value = (String) props.get(setting);
			try {
				int parsedInt = Integer.parseInt(value);
				if (parsedInt >= 0) 
					return parsedInt;
			} catch(NumberFormatException e) {
				return defaultValue;
			}
		}
		return defaultValue;
	}

	private String validatePropertyValue(String[] validValues, String defaultValue, String value) {
		for (String s : validValues) {
			if (value.equals(s))
				return value;
		}
		return defaultValue;
	}

	public String getInputScheme() {
		return inputScheme;
	}

	public void setInputScheme(String inputScheme) {
		this.inputScheme = validatePropertyValue(InputType.stringValues(), InputType.getDefault().value(), inputScheme);
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = validatePropertyValue(GameLanguage.stringValues(), GameLanguage.getDefault().value(), language);
	}

	public boolean isHPAsBar() {
		return HPAsBar;
	}

	public void setHPAsBar(boolean hPAsBar) {
		HPAsBar = hPAsBar;
	}

	public boolean isMusic() {
		return music;
	}

	public void setMusic(boolean music) {
		this.music = music;
	}

	public boolean isEffects() {
		return effects;
	}

	public void setEffects(boolean effects) {
		this.effects = effects;
	}

	public boolean isFullScreen() {
		return fullScreen;
	}

	public void setFullScreen(boolean fullScreen) {
		this.fullScreen = fullScreen;
	}

	public int getSaveSlots() {
		return saveSlots;
	}

	public void setSaveSlots(int saveSlots) {
		if (saveSlots >= 0) {
			this.saveSlots = saveSlots;
		} else 
			this.saveSlots = 4;
	}

	public static Settings getLastLoadedInstance() {
		return instance;
	}

}
