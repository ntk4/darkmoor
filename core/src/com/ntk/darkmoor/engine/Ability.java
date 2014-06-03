package com.ntk.darkmoor.engine;

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

	public void save(String string, XmlWriter writer) {
		// TODO Auto-generated method stub
		
	}

	public void load(Element node) {
		// TODO Auto-generated method stub
		
	}
}
