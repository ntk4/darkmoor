package com.ntk.darkmoor.engine;

/**
 * An ability partially describes an entity and affects some of his or her actions.
 * http://www.dandwiki.com/wiki/SRD:Ability_Scores
 * 
 * @author Nick
 * 
 */
public class Ability {

	private int value;
	
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
}
