package com.ntk.darkmoor.engine;

import java.io.IOException;

import org.ntk.commons.StringUtils;

import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;

public class HitPoint {

	private int current;
	private int max;

	public HitPoint(int max) {
		this(max, max);
	}

	public HitPoint() {
		this(0, 0);
	}

	public HitPoint(int current, int max) {
		this.max = max;
		this.current = current;

		if (this.current > this.max)
			this.current = this.max;
	}

	public void add(int amount) {
		current += amount;
	}

	@Override
	public String toString() {
		return String.format("%d / %d", current, max);
	}

	public boolean save(XmlWriter writer) throws IOException {
		if (writer == null)
			return false;

		writer.element("hitpoint").attribute("current", current).attribute("max", max).pop();

		return true;
	}

	public boolean load(Element node) {
		if (node == null)
			return false;

		if (!StringUtils.equals("hitpoint", node.getName()))
			return false;

		max = Integer.parseInt(node.getAttribute("max"));
		current = Integer.parseInt(node.getAttribute("current"));

		return true;
	}

	public int getCurrent() {
		return current;
	}

	public void setCurrent(int current) {
		this.current = current;
		current = Math.min(current, max);
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public int getPeril() {
		return max / 2;
	}

	public float getRatio() {
		return ((float) current / (float) max);
	}

}
