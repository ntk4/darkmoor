package com.ntk.darkmoor.engine.script.actions;

import java.io.IOException;

import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.ntk.darkmoor.GameScreen;
import com.ntk.darkmoor.engine.Square;

public class EnableTarget extends ActionBase {

	public static final String TAG = "DeactivateTarget";

	public EnableTarget() {
		name = TAG;
	}

	@Override
	public boolean run() {
		if (target == null)
			return false;

		// Get the target
		Square target2 = target.getSquare(GameScreen.getDungeon());
		if (target2 == null)
			return false;

		// Get the actor
		if (target2.getActor() != null && target2.getActor().isActivated())
			target2.getActor().enable();

		return true;
	}

	@Override
	public String toString() {
		String str = "Enables target at ";

		if (target != null)
			str += target.toStringShort();

		return str;
	}

	@Override
	public boolean load(XmlReader.Element xml) {
		if (xml == null || !TAG.equals(xml.getName()))
			return false;

		for (int i = 0; i < xml.getChildCount(); i++) {

			super.load(xml.getChild(i));
		}

		return true;
	}

	@Override
	public boolean save(XmlWriter writer) throws IOException {
		if (writer == null)
			return false;

		writer.element(TAG);

		super.save(writer);

		writer.pop();

		return true;
	}
}
