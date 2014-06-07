package com.ntk.darkmoor.engine;

import java.io.IOException;

import org.ntk.commons.StringUtils;

import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;

/**
 * An ability partially describes an entity and affects some of his or her actions.
 * http://www.dandwiki.com/wiki/SRD:Ability_Scores
 * 
 * @author Nick
 * 
 */
public class Ability {

	public int value;
	
	public Ability() {
		this(0);
	}

	public Ability(int value) {
		this.value = value;
	}

	public String toString() {
		return String.format("%d (mod %d)", value, getModifier());
	}

	public int getModifier() {
		return (value - 10) / 2;
	}

	public boolean save(String name, XmlWriter writer) throws IOException {
		if (writer == null || StringUtils.isEmpty(name))
			return false;
		
		writer.element(name).attribute("value", value).pop();
		
		return true;
	}

	public boolean load(Element node) {
		if (node == null)
			return false;
		
		if (node.getAttribute("value") != null) {
			value = Integer.parseInt(node.getAttribute("value"));
		}
		
		return true;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}
