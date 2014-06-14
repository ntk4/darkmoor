package com.ntk.darkmoor.resource;

import java.io.IOException;

import org.ntk.commons.StringUtils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.ntk.darkmoor.exception.LoadException;

public class TextureSet {

	public static final String TAG = "tileset";
	private String name;
	private String textureAtlasFile;
	private TextureAtlas atlas;
	private Array<TextureMetadata> metadata;

	public TextureSet(int capacity) {
		metadata = new Array<TextureMetadata>(capacity);
	}

	private void loadAtlas() {
		Texture texture = null;

		// the file handle can be retrieved in a game, in JUnit it doesn't work
		if (Gdx.files == null)
			return;

		FileHandle fh = Gdx.files.internal("data/" + textureAtlasFile.toLowerCase());
		if (fh != null) {
			atlas = new TextureAtlas(fh);
			texture = new Texture(fh);

			for (TextureMetadata textureInfo : metadata) {

				atlas.addRegion(name,
						new TextureRegion(texture, textureInfo.getRectangle().x, textureInfo.getRectangle().y,
								textureInfo.getRectangle().width, textureInfo.getRectangle().height));
			}
		}
	}

	public boolean load(Element xml) {
		if (xml == null || !TAG.equalsIgnoreCase(xml.getName()))
			return false;

		this.name = xml.getAttribute("name");
		String name = null;

		for (int i = 0; i < xml.getChildCount(); i++) {
			Element child = xml.getChild(i);
			name = child.getName();

			if (TextureMetadata.TAG.equalsIgnoreCase(name)) {

				TextureMetadata textureMetadata = new TextureMetadata();
				if (textureMetadata.load(child)) {
					metadata.add(textureMetadata);
				} else {
					throw new LoadException("Could not load texture metadata node: " + child.toString());
				}

			} else if ("texture".equalsIgnoreCase(name)) {
				this.textureAtlasFile = child.getAttribute("file");
			}
		}

		if (!StringUtils.isEmpty(textureAtlasFile)) {
			loadAtlas();
		}

		return true;
	}

	public boolean save(XmlWriter writer) throws IOException {
		if (writer == null)
			return false;

		writer.element(TAG).attribute("name", name);
		writer.element("texture").attribute("file", textureAtlasFile).pop();

		for (TextureMetadata texture : metadata) {
			texture.save(writer);
		}

		writer.pop();
		return true;
	}

	public static String getTag() {
		return TAG;
	}

	public String getName() {
		return name;
	}

	public String getTextureAtlasFile() {
		return textureAtlasFile;
	}

	public Array<TextureMetadata> getMetadata() {
		return metadata;
	}
}
