package com.ntk.darkmoor.engine.script.actions;

import java.io.IOException;

import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.ntk.darkmoor.config.Log;
import com.ntk.darkmoor.engine.DungeonLocation;

public class ActionBase {

	protected DungeonLocation target;

	protected String name;

	public ActionBase() {

	}

	public void dispose() {

	}

	/**
	 * Runs the script
	 * 
	 * @return true on success
	 */
	public boolean run() {
		return false;
	}

	//TODO: XmlReader.Element is only an approximation of the XmlNode, test the mechanism thoroughly
	public boolean load(XmlReader.Element node) {
		if (node == null)
			return false;

		if ("target".equalsIgnoreCase(node.getName())) {
			if (target == null)
				target = new DungeonLocation();

			target.load(node);
		} else {
			Log.debug("[ActionBase] Load() : Unknown node \"" + node.getName() + "\" found.");
		}
		return true;
	}

	public boolean save(XmlWriter writer) throws IOException {
		if (writer == null)
			return false;

		if (target != null)
			target.save("target", writer);

		return true;
	}

	public DungeonLocation getTarget() {
		return target;
	}

	public void setTarget(DungeonLocation target) {
		this.target = target;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
