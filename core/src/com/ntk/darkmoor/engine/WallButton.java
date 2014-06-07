package com.ntk.darkmoor.engine;

import java.io.IOException;
import java.util.ArrayList;

import org.ntk.commons.StringUtils;

import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;

public class WallButton {

	public static final String TAG= "wallbutton";
	
	private ArrayList<Item> acceptedItems;
	private boolean disposed;
	private String name;
	private boolean hidden;

	public WallButton() {
		acceptedItems = new ArrayList<Item>();
		disposed = false;
	}

	public boolean init() {
		return true;
	}
	
	public void dispose()
	{
	}
	
	public boolean load(Element xml) {
		if (xml == null || !"item".equalsIgnoreCase(xml.getName()))
			return false;

		name = xml.getAttribute("name");
		// ntk: type already loaded as item?
		
		return true;
	}

	public boolean save(String name, XmlWriter writer) throws IOException {
		if (writer == null || StringUtils.isEmpty(name))
			return false;

		writer.element("item").attribute("name", name);
		writer.element("type").pop(); // empty?
		writer.pop();
		
		return true;
	}

	public ArrayList<Item> getAcceptedItems() {
		return acceptedItems;
	}

	public boolean isDisposed() {
		return disposed;
	}

	public String getName() {
		return name;
	}

	public boolean isHidden() {
		return hidden;
	}
}
