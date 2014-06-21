package com.ntk.darkmoor.resource;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.ntk.darkmoor.exception.LoadException;

public class GraphicAssets {

	private String fileName;
	private static Map<String, GraphicAssets> instances;
	
	private Map<String, TextureSet> textureCache;

	public static GraphicAssets getAssets(String fileName) {
		if (instances == null)
			instances = new HashMap<String, GraphicAssets>();

		if (instances.get(fileName) == null) {
			instances.put(fileName, new GraphicAssets(fileName));
		}
		return instances.get(fileName);
	}

	private GraphicAssets(String fileName) {
		this.fileName = fileName;
		textureCache = new HashMap<String, TextureSet>(1);
	}
	
	public TextureSet getTextureSet(String textureSetName) throws LoadException {
		TextureSet cachedTextureSet = textureCache.get(textureSetName);
		if (cachedTextureSet != null )
			return cachedTextureSet;
		cachedTextureSet = load(textureSetName);
		textureCache.put(textureSetName, cachedTextureSet);
		return cachedTextureSet;
	}

	private TextureSet load(String textureSetName) throws LoadException {	
		
		Element root = ResourceUtility.extractRootElement(Resources.getResourcePath() + fileName);

		TextureSet set = null;
		try {

			if (root == null) {
				throw new LoadException("Could not read root element of file " + fileName);
			}

			Array<Element> children = root.getChildrenByName("tileset");

			for (Element child : children) {
				if (textureSetName.equalsIgnoreCase(child.getAttribute("name"))) {
					set = new TextureSet(child.getChildCount());
					set.load(child);
					break;
				}
			}
		} catch (Exception e) {
			throw new LoadException(e);
		}
		return set;
	}

	public void save(TextureSet... sets) throws IOException {

		XmlWriter writer = new XmlWriter(new FileWriter(fileName));

		writer.element("bank");

		for (TextureSet set : sets)
			set.save(writer);

		writer.pop();

		writer.close();
	}
}
