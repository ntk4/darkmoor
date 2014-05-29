package com.ntk.darkmoor.engine.script.actions;

import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;

public class JoinCharacter extends ActionBase {
	
	public static final String TAG = "JoinCharacter";

	public JoinCharacter() {
		name = TAG;
	}

	@Override
	public boolean run() {
		return false;
	}

	@Override
	public String toString() {
		return "Joins character ";
	}
	
	public boolean load(XmlReader.Element xml) {
		return true;
	}

	public boolean Save(XmlWriter writer) {
		return true;
	}
}
