package com.ntk.darkmoor.engine.script.actions;

import java.io.IOException;

import org.ntk.commons.StringUtils;

import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.ntk.darkmoor.GameScreen;

public class GiveExperience extends ActionBase {

	public static final String TAG = "GiveExperience";

	private int amount;

	public GiveExperience() {
		name = TAG;
	}

	@Override
	public boolean run() {
		if (target == null)
			return false;

		if (amount > 0) {
			GameScreen.getTeam().addExperience(amount);
		}

		return true;
	}

	@Override
	public String toString() {
		return "Give " + amount + " XP";
	}

	@Override
	public boolean load(XmlReader.Element xml) {
		if (xml == null || !TAG.equals(xml.getName()))
			return false;

		String value = xml.getAttribute("value");
		if (!StringUtils.isEmpty(value))
			amount = Integer.parseInt(value);

		return true;
	}

	@Override
	public boolean save(XmlWriter writer) throws IOException {
		if (writer == null)
			return false;

		writer.element(TAG).attribute("value", String.valueOf(amount)).pop();

		return true;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}
}
