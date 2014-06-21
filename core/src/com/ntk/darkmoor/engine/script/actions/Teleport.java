package com.ntk.darkmoor.engine.script.actions;

import java.io.IOException;

import org.ntk.commons.StringUtils;

import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.ntk.darkmoor.GameScreen;

public class Teleport extends ActionBase {

	public static final String TAG = "Teleport";
	
	private boolean changeDirection;

	public Teleport() {
		name = TAG;
	}

	@Override
	public boolean run() {
		if (target == null)
			return false;

		if (GameScreen.getTeam().teleport(target)) {
			if (changeDirection)
				GameScreen.getTeam().setDirection(target.getDirection());
			return true;
		}

		return false;
	}

	@Override
	public boolean load(XmlReader.Element xml) {
		if (xml == null || !TAG.equals(xml.getName()))
			return false;

		for (int i = 0; i < xml.getChildCount(); i++) {
			XmlReader.Element node = xml.getChild(i);

			// TODO: missing code: if node.nodeType == XmlNodeType.Comment then continue;

			if (StringUtils.equals(node.getName(), "changedirection")) {
				changeDirection = Boolean.parseBoolean(node.getAttribute("value"));

			} else {
				super.load(node);
			}
		}

		return true;
	}

	@Override
	public boolean save(XmlWriter writer) throws IOException {
		if (writer == null)
			return false;

		writer.element(TAG);
		
		writer.element("changedirection").attribute("value", String.valueOf(changeDirection)).pop();

		super.save(writer);

		writer.pop();

		return true;
	}

	public boolean isChangeDirection() {
		return changeDirection;
	}

	public void setChangeDirection(boolean changeDirection) {
		this.changeDirection = changeDirection;
	}
}
