package com.ntk.darkmoor.engine;

import java.io.IOException;

import org.ntk.commons.StringUtils;

import com.badlogic.gdx.utils.XmlWriter;
import com.badlogic.gdx.utils.XmlReader.Element;

public class SwitchCount {
	private int count;
	private int target;
	private boolean resetOnTrigger;

	public SwitchCount() {
		this(1, 0);
	}

	public SwitchCount(int target, int count) {
		this.count = count;
		this.target = target;

	}

	public boolean activate() {
		count++;

		// Reset count
		if (count >= target && resetOnTrigger) {
			count = 0;
			return true;
		}

		return count == target;
	}

	public boolean deactivate() {
		count--;

		if (count < 0)
			count = 0;

		return count == 0;
	}

	public boolean load(Element xml) {
		if (xml == null)
			return false;

		target = Integer.parseInt(xml.getAttribute("target"));
		count = Integer.parseInt(xml.getAttribute("count"));
		resetOnTrigger = Boolean.parseBoolean(xml.getAttribute("reset"));

		return true;
	}

	public boolean save(String name, XmlWriter writer) throws IOException {
		if (writer == null || StringUtils.isEmpty(name))
			return false;

		writer.element(name).attribute("target", target).attribute("count", count).attribute("reset", resetOnTrigger)
				.pop();
		
		return true;
	}
	
	public boolean isUsable() {
		return count >= target;
	}

	public int getCount() {
		return count;
	}

	public int getTarget() {
		return target;
	}

	public boolean isResetOnTrigger() {
		return resetOnTrigger;
	}
}
