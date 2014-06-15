package com.ntk.darkmoor.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.ntk.darkmoor.exception.LoadException;

public class GraphicAssets {

	private String fileName;
	private static Map<String,GraphicAssets> instances;

	public static GraphicAssets getAssets(String fileName) {
		if (instances == null)
			instances = new HashMap<String, GraphicAssets>();
		
		if (instances.get(fileName) == null) {
			instances.put(fileName,new GraphicAssets(fileName));
		}
		return instances.get(fileName);
	}

	private GraphicAssets(String fileName) {
		this.fileName = fileName;
	}

	public TextureSet load(String textureSetName) throws LoadException {

		Element root = extractRootElement();

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

	private Element extractRootElement() {
		XmlReader reader = new XmlReader();
		InputStream inputStream = null;
		Element root = null;

		if (new File(fileName).exists()) {
			try {
				inputStream = new FileInputStream(fileName);
				root = reader.parse(inputStream);
			} catch (FileNotFoundException e) {
				throw new LoadException("[GraphicAssets]: file '" + fileName + "' could not be opened");
			} catch (IOException e) {
				throw new LoadException("[GraphicAssets]: external file '" + fileName + "' could not be parsed");
			}
		} else {
			FileHandle fh = Gdx.files.internal(fileName);
			try {
				root = reader.parse(fh);
			} catch (IOException e) {
				throw new LoadException("[GraphicAssets]: internal file '" + fileName + "' could not be parsed");
			}
		}
		return root;
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
