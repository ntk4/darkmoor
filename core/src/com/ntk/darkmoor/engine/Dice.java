package com.ntk.darkmoor.engine;

import java.io.IOException;

import org.ntk.commons.StringUtils;

import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.ntk.darkmoor.config.GameMechanics;

public class Dice implements Cloneable {

	public enum DCType {
		/** Notice something large in plain sight */
		VeryEasy(0),
		/** Climb a knotted rope */
		Easy(10),
		/** Hear an approaching guard */
		Average(15),
		/** Rig a wagon wheel to fall off */
		Tough(15),
		/** Swim in stormy water */
		Challenging(20),
		/** Open an average lock */
		Formidable(25),
		/** Leap across a 30-foot chasm */
		Heroic(30),
		/** Track a squad of orcs across hard ground after 24 hours of rainfall */
		NearlyImpossible(40);

		private int value;

		private DCType(int value) {
			this.value = value;
		}

		public int value() {
			return value;
		}
	}

	private int diceThrows;
	private int faces;
	private int modifier;

	public Dice(int diceThrows, int faces, int modifier) {
		this.diceThrows = diceThrows;
		this.faces = faces;
		this.modifier = modifier;
	}

	public Dice() {
		this(0, 1, 0);
	}

	public boolean load(Element xml) {
		if (xml == null)
			return false;

		if (xml.getAttribute("faces") != null)
			faces = Integer.parseInt(xml.getAttribute("faces"));

		if (xml.getAttribute("throws") != null)
			diceThrows = Integer.parseInt(xml.getAttribute("throws"));

		if (xml.getAttribute("modifier") != null)
			modifier = Integer.parseInt(xml.getAttribute("modifier"));

		return true;
	}

	public boolean save(String name, XmlWriter writer) throws IOException {
		if (writer == null || StringUtils.isEmpty(name))
			return false;

		writer.element(name).attribute("throws", diceThrows).attribute("faces", faces).attribute("modifier", modifier)
				.pop();

		return true;
	}

	public static int getD20(int count) {
		int res = 0;

		for (int i = 0; i < count; i++) {
			res += GameMechanics.getRandom().nextInt(20) + 1;
		}

		return res;
	}

	public int roll() {
		return roll(diceThrows);
	}

	private int roll(int rolls) {
		if (faces == 0)
			return 0;

		int val = 0;

		for (int i = 0; i < rolls; i++)
			val += GameMechanics.getRandom().nextInt(faces) + 1;

		return val + modifier;
	}

	public static int savingThrow(SavingThrowType type) {
		// TODO: ntk: not implemented?
		return 0;
	}

	public void reset() {
		modifier = 0;
		diceThrows = 1;
		faces = 1;
	}

	public Dice clone(Dice dice) {
		if (dice == null)
			return null;

		faces = dice.faces;
		diceThrows = dice.diceThrows;
		modifier = dice.modifier;
		return this;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return clone(this);
	}

	@Override
	public String toString() {
		return String.format("%dd%d + %d (%d~%d)", diceThrows, faces, modifier, getMinimum(), getMaximum());
	}

	private int getMaximum() {
		return modifier + diceThrows;
	}

	private int getMinimum() {
		return modifier + (faces * diceThrows);
	}

	public int getDiceThrows() {
		return diceThrows;
	}

	public void setDiceThrows(int diceThrows) {
		this.diceThrows = diceThrows;
	}

	public int getFaces() {
		return faces;
	}

	public void setFaces(int faces) {
		this.faces = faces;
	}

	public int getModifier() {
		return modifier;
	}

	public void setModifier(int modifier) {
		this.modifier = modifier;
	}

}
