package com.ntk.darkmoor.engine.script.actions;

import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;

public class EnableChoice extends ActionBase {
	
	public static final String TAG = "EnableChoice";

	public EnableChoice() {
		name = TAG;
	}

	@Override
	public boolean run() {
		return false;
	}

	public boolean load(XmlReader.Element xml) {
		return true;
	}

	public boolean Save(XmlWriter writer) {
		return true;
	}
}
