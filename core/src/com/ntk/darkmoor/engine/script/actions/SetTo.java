package com.ntk.darkmoor.engine.script.actions;

import java.io.IOException;

import org.ntk.commons.StringUtils;

import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.ntk.darkmoor.engine.Square;
import com.ntk.darkmoor.stub.GameScreen;

public class SetTo extends ActionBase {

	public static final String TAG = "SetTo";

	private Square square;

	public SetTo() {
		name = TAG;
	}

	@Override
	public boolean run() {
		if (target == null)
			return false;

		return GameScreen.getDungeon().setSquare(target, square);
	}

	@Override
	public String toString() {
		return "Set to";
	}

	@Override
	public boolean load(XmlReader.Element xml) {
		if (xml == null || !TAG.equals(xml.getName()))
			return false;

		for (int i = 0; i < xml.getChildCount(); i++) {
			if (StringUtils.equals(Square.TAG, xml.getChild(i).getName())) {
				square.load(xml.getChild(i)); // TODO: what if square is null?
			} else {
				super.load(xml.getChild(i));
			}
		}

		return true;
	}

	@Override
	public boolean save(XmlWriter writer) throws IOException {
		if (writer == null)
			return false;

		writer.element(TAG);

		if (square != null)
			square.save(writer);

		super.save(writer);

		writer.pop();

		return true;
	}

	public Square getSquare() {
		return square;
	}

	public void setSquare(Square square) {
		this.square = square;
	}
}
