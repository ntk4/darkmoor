package com.ntk.darkmoor.resource;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.ntk.darkmoor.engine.DecorationSet;
import com.ntk.darkmoor.exception.LoadException;

/**
 * Singleton class Maintains
 * 
 * @author Nick
 * 
 */
public class DecorationAssets {

	private final String DECORATION_FILE = "DecorationSet.xml";
	private static DecorationAssets instance;

	private Map<String, DecorationSet> decorationCache;

	public static DecorationAssets getAssets() {
		if (instance == null) {
			instance = new DecorationAssets();
		}
		return instance;
	}

	private DecorationAssets() {
		decorationCache = new HashMap<String, DecorationSet>();
	}

	public DecorationSet getTextureSet(String decorationSetName) throws LoadException {
		DecorationSet cachedDecorationSet = decorationCache.get(decorationSetName);
		if (cachedDecorationSet != null)
			return cachedDecorationSet;
		cachedDecorationSet = load(decorationSetName);
		decorationCache.put(decorationSetName, cachedDecorationSet);
		return cachedDecorationSet;
	}

	private DecorationSet load(String decorationSetName) throws LoadException {

		Element root = ResourceUtility.extractRootElement(Resources.getResourcePath() + DECORATION_FILE);

		DecorationSet set = null;
		try {

			if (root == null) {
				throw new LoadException("Could not read root element of file " + DECORATION_FILE);
			}

			Array<Element> children = root.getChildrenByName("decorationset");

			for (Element child : children) {
				if (decorationSetName.equalsIgnoreCase(child.getAttribute("name"))) {
					set = new DecorationSet();
					set.load(child);
					break;
				}
			}
		} catch (Exception e) {
			throw new LoadException(e);
		}
		return set;
	}

	public void save(DecorationSet... sets) throws IOException {

		XmlWriter writer = new XmlWriter(new FileWriter(DECORATION_FILE));

		writer.element("bank");

		int i = 0;
		for (DecorationSet set : sets)
			set.save(writer, i++);

		writer.pop();

		writer.close();
	}
}
