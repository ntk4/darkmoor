package com.ntk.darkmoor.engine.script.actions;

import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;

public class GiveItem extends ActionBase {
	
	public static final String TAG = "GiveItem";

	private String itemName;
	
	
	public GiveItem() {
		name = TAG;
	}

	@Override
	public boolean run() {
		return false;
	}

	@Override
	public String toString() {
		return "Give : " + itemName;
	}
	
	public boolean load(XmlReader.Element xml) {
		return true;
	}

	public boolean Save(XmlWriter writer) {
		return true;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
}
