package com.ntk.darkmoor.engine;

import java.io.IOException;

import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;

/**
 * A hit that strikes a vital area and therefore deals double damage or more. http://nwn.wikia.com/wiki/Critical_hit
 * 
 * @author Nick
 * 
 */
public class CriticalHit {

	private int minimum;
	private int maximum;
	private int multiplier;

	public CriticalHit() {
		minimum = 20;
		maximum = 20;
		multiplier = 2;
	}

	public boolean isCriticalHit(int value) {
		return value >= minimum && value <= maximum;
	}

	@Override
	public String toString() {
		return String.format("%d-%d(x%d)", minimum, maximum, multiplier);
	}

	public boolean load(XmlReader.Element node) {
		if (node == null)
			return false;

		if (node.getAttribute("minimum") != null)
			minimum = Integer.parseInt(node.getAttribute("minimum"));

		if (node.getAttribute("maximum") != null)
			minimum = Integer.parseInt(node.getAttribute("maximum"));

		if (node.getAttribute("multiplier") != null)
			minimum = Integer.parseInt(node.getAttribute("multiplier"));

		return true;
	}

	public boolean save(XmlWriter writer)  throws IOException{
		if (writer == null)
			return false;

		// TODO: ntk: how can we assess the state of the writer?
		// boolean writeheader = false;
		// if (writer.WriteState != WriteState.Element)
		// writeheader = true;

		try {
			writer.element("criticalhit");
		} catch (Exception e) {

		}
		writer.attribute("minimum", minimum);
		writer.attribute("maximum", maximum);
		writer.attribute("multiplier", multiplier);
		try {
			writer.pop();
		} catch (Exception e) {

		}
		
		return true;
	}

	public int getMinimum() {
		return minimum;
	}

	public void setMinimum(int minimum) {
		this.minimum = minimum;
	}

	public int getMaximum() {
		return maximum;
	}

	public void setMaximum(int maximum) {
		this.maximum = maximum;
	}

	public int getMultiplier() {
		return multiplier;
	}

	public void setMultiplier(int multiplier) {
		this.multiplier = multiplier;
	}
}
