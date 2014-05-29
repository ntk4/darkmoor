package com.ntk.darkmoor.engine.script.actions;

import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;

public class ChangeText extends ActionBase {
	
	public static final String TAG = "ChangeText";

	public ChangeText() {
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
