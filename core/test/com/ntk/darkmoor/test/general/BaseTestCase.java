package com.ntk.darkmoor.test.general;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

public class BaseTestCase {

	protected static final String TEST_RESOURCES = "./test/test-resources/";

	protected static final String SETTINGS_CORRECT = "settings_correct.properties";

	protected Element loadXml(String xmlResource) throws FileNotFoundException, IOException {
		XmlReader reader = new XmlReader();
		return reader.parse(new FileInputStream(xmlResource));
	}
}
