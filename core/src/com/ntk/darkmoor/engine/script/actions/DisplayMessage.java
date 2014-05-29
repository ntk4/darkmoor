package com.ntk.darkmoor.engine.script.actions;

import java.io.IOException;

import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.ntk.darkmoor.stub.GameMessage;
import com.ntk.darkmoor.stub.GameScreen;

public class DisplayMessage extends ActionBase {

	public static final String TAG = "DisplayMessage";

	private String message;

	public DisplayMessage() {
		name = TAG;
	}

	@Override
	public boolean run() {
		GameMessage.addMessage(GameScreen.getTeam().getSelectedHero().getName() + ": " + message);

		return true;
	}

	@Override
	public String toString() {

		return "Display : " + message;
	}

	@Override
	public boolean load(XmlReader.Element xml) {
		if (xml == null || !TAG.equals(xml.getName()))
			return false;

		for (int i = 0; i < xml.getChildCount(); i++) {
			
			if ("message".equalsIgnoreCase(xml.getChild(i).getName())) {
				message = xml.getChild(i).getText(); // TODO: check if this gives the inner text
			} else {
				super.load(xml.getChild(i));
			}
		}

		return true;
	}

	@Override
	public boolean save(XmlWriter writer) throws IOException {
		if (writer == null)
			return false;

		writer.element(TAG).element("message",message);

		super.save(writer);

		writer.pop().pop(); //TODO: check if two pops are ok here

		return true;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
