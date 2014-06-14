package com.ntk.darkmoor.resource;

import java.io.IOException;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;

public class TextureMetadata {

	public static final String TAG = "tile";
	private int id;
	private Rectangle rectangle;
	private Vector2 hotspot;
	private Rectangle collisionBox;

	public TextureMetadata() {
		rectangle = new Rectangle();
		hotspot = new Vector2();
		collisionBox = new Rectangle();
	}

	public boolean load(Element xml) {
		if (xml == null || !TAG.equalsIgnoreCase(xml.getName()))
			return false;

		this.id = Integer.parseInt(xml.getAttribute("id"));
		String name = null;

		for (int i = 0; i < xml.getChildCount(); i++) {
			Element child = xml.getChild(i);
			name = child.getName();

			if ("rectangle".equalsIgnoreCase(name)) {
				rectangle.x = Integer.parseInt(child.getAttribute("x"));
				rectangle.y = Integer.parseInt(child.getAttribute("y"));
				rectangle.width = Integer.parseInt(child.getAttribute("width"));
				rectangle.height = Integer.parseInt(child.getAttribute("height"));

			} else if ("hotspot".equalsIgnoreCase(name)) {
				hotspot.x = Integer.parseInt(child.getAttribute("x"));
				hotspot.y = Integer.parseInt(child.getAttribute("y"));

			} else if ("collisionbox".equalsIgnoreCase(name)) {
				collisionBox.x = Integer.parseInt(child.getAttribute("x"));
				collisionBox.y = Integer.parseInt(child.getAttribute("y"));
				collisionBox.width = Integer.parseInt(child.getAttribute("width"));
				collisionBox.height = Integer.parseInt(child.getAttribute("height"));
			}
		}

		return true;
	}

	public boolean save(XmlWriter writer) throws IOException {
		if (writer == null)
			return false;

		writer.element(TAG).attribute("id", id);

		writer.element("rectangle").attribute("x", (int)rectangle.x).attribute("y", (int)rectangle.y)
				.attribute("width", (int)rectangle.width).attribute("height", (int)rectangle.height).pop();
		writer.element("hotspot").attribute("x", (int)hotspot.x).attribute("y", (int)hotspot.y).pop();
		writer.element("collisionbox").attribute("x", (int)collisionBox.x).attribute("y", (int)collisionBox.y)
				.attribute("width", (int)collisionBox.width).attribute("height", (int)collisionBox.height).pop();

		writer.pop();

		return true;
	}

	public int getId() {
		return id;
	}

	public Rectangle getRectangle() {
		return rectangle;
	}

	public Vector2 getHotspot() {
		return hotspot;
	}

	public Rectangle getCollisionBox() {
		return collisionBox;
	}

}
