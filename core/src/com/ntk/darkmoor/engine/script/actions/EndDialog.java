package com.ntk.darkmoor.engine.script.actions;

import java.io.IOException;

import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.ntk.darkmoor.stub.GameScreen;

public class EndDialog extends ActionBase {

	public static final String TAG = "EndDialog";

	public EndDialog() {
		name = TAG;
	}

	@Override
	public boolean run() {
		if (target == null)
			return false;

		GameScreen.getDialog().exit();

		return true;
	}

	@Override
	public boolean load(XmlReader.Element xml) {
		
		if (xml == null || !TAG.equals(xml.getName()))
			return false;
		
		return true;
	}

	@Override
	public boolean save(XmlWriter writer) throws IOException {
		if (writer == null)
			return false;

		writer.element(TAG).pop();

		return true;
	}
}
