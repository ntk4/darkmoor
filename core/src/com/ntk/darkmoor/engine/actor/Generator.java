package com.ntk.darkmoor.engine.actor;

import java.io.IOException;

import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.ntk.darkmoor.stub.Square;

public class Generator extends SquareActor {
	public static final String TAG = "generator";
	
	public Generator(Square square) {
		super(square);
	}

	public boolean load(XmlReader.Element node) {
		if (node == null)
			return false;

		for (int i = 0; i < node.getChildCount(); i++) {
			super.load(node.getChild(i));
		}

		return true;
	}

	public boolean save(XmlWriter writer) throws IOException {
		if (writer == null)
			return false;

		writer.element(TAG);

		super.save(writer);
		
		writer.pop();
		
		return true;
	}

}
