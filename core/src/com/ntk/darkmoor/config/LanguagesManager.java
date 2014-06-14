package com.ntk.darkmoor.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

public class LanguagesManager {
	private static LanguagesManager _instance = null;
	public static final String DEFAULT_FILE = "StringTable.xml";

	private static final String LANGUAGES_FILE = "data/languages.xml";
	private static final String DEFAULT_LANGUAGE = "English";

	private HashMap<String, String> messages = null;
	/** String table file from which the instance was loaded */
	private String fileName;
	/** Current language setting (set by LanguagesManager users from Settings class) */
	private String language;

	private LanguagesManager(String stringTableFile) {
		// Create language map
		messages = new HashMap<String, String>();
		fileName = stringTableFile;

		InputStream inputStream = null;
		if (new File(stringTableFile).exists()) {
			try {
				inputStream = new FileInputStream(stringTableFile);
			} catch (FileNotFoundException e) {
				Log.error("[LanguagesManager]: string table file '%s' was not found!", stringTableFile);
			}
		} else {
			inputStream = Gdx.files.internal(LANGUAGES_FILE).read();
		}

		loadMessages(inputStream);
	}

	public static LanguagesManager getInstance(String stringTableFile) {
		if (_instance == null) {
			_instance = new LanguagesManager(stringTableFile);
		} else if (!_instance.fileName.equalsIgnoreCase(stringTableFile)) {
			_instance.dispose();
			_instance = new LanguagesManager(stringTableFile);
		}

		return _instance;
	}

	public static LanguagesManager getInstance() {
		if (_instance == null) {
			_instance = new LanguagesManager(DEFAULT_FILE);
		}

		return _instance;
	}

	private void dispose() {
		messages.clear();
		messages = null;
	}

	public String getString(String languageName, String section, int key) {
		String string;

		if (messages != null) {
			// Look for string in selected language
			string = messages.get(languageName + "|" + section + "|" + key);

			if (string != null) {
				return string;
			}
		}

		// Key not found, return an explaining message
		return String.format("Key %d not found in string table", key);
	}

	public String buildMessage(String section, int id, Object[] args) {
		String message = getString(language == null ? DEFAULT_LANGUAGE : language, section, id);
		if (message != null)
			return String.format(message, args);
		return "";
	}

	private boolean loadMessages(InputStream inputStream) {
		try {
			XmlReader reader = new XmlReader();
			Element root = reader.parse(inputStream);

			messages.clear();
			Array<Element> sectionElements = root.getChildrenByName("stringtable");
			for (Element sectionElem : sectionElements) {

				Array<Element> languages = sectionElem.getChildrenByName("language");

				for (int i = 0; i < languages.size; ++i) {
					Element language = languages.get(i);

					Array<Element> strings = language.getChildrenByName("string");

					for (int j = 0; j < strings.size; ++j) {
						Element string = strings.get(j);
						String key = string.getAttribute("id");
						String value = string.getText();
						value = value.replace("&lt;br /&gt;&lt;br /&gt;", "\n");
						messages.put(
								language.getAttribute("name") + "|" + sectionElem.getAttribute("name") + "|" + key,
								value);
					}

				}
			}
		} catch (Exception e) {
			System.out.println("Error loading languages file " + LANGUAGES_FILE);
			return false;
		}

		return false;
	}

	public String getFileName() {
		return fileName;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

}