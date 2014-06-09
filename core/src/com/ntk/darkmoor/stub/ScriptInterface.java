package com.ntk.darkmoor.stub;

import java.io.IOException;

import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.ntk.darkmoor.engine.interfaces.ISpell;

public class ScriptInterface<T> {

	public static final String TAG = "script";
	private String  interfaceName;

	public boolean load(Element node) {
		if (node == null)
			return false;
		
		return true;
	}

	public boolean save(String name, XmlWriter writer) throws IOException {
		if (writer == null)
			return false;
		
		writer.element(TAG).attribute("script", name).attribute("interface", interfaceName).pop();
		
		return true;
		
	}

	public ISpell getInstance() {
		// TODO Auto-generated method stub
		return null;
	}

}
