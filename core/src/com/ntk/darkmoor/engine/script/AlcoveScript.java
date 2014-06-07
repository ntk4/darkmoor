package com.ntk.darkmoor.engine.script;

import java.io.IOException;

import org.ntk.commons.StringUtils;

import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.ntk.darkmoor.engine.Team;
import com.ntk.darkmoor.stub.GameScreen;

public class AlcoveScript extends ScriptBase {

	private static final String TAG = "AlcoveScript";

	private String itemName;
	
	private boolean consumeItem;

	public AlcoveScript() {
		
	}
	
	public boolean run() {

		Team team = GameScreen.getTeam();
		if (team.getItemInHand() == null || !StringUtils.equals(team.getItemInHand().getName(), itemName)) {
			return false;
		}
		
		if (consumeItem) {
			team.setItemInHand(null);
		}

		return super.run();
	}
	

	public boolean load(XmlReader.Element xml) {
		if (xml == null)
			return false;

		if (StringUtils.equals("consume", xml.getName())) {
			consumeItem = Boolean.parseBoolean(xml.getText());//TODO: check if it works
		} else if (StringUtils.equals("item", xml.getName())) {
			itemName = xml.getText();//TODO: check if it works
		} else {
			super.load(xml);
		}

		return true;
	}
	
	public boolean save(XmlWriter writer) throws IOException {
		if (writer == null)
			return false;
		
		writer.element(TAG);
		
		//TODO: resulting xml has to be checked..
		
		if (consumeItem)
			writer.element("consume", "true").pop();
		
		if (!StringUtils.isEmpty(itemName))
			writer.element("item",itemName).pop();
		
		super.save(writer);

		writer.pop();
		
		return true;
	}
	
}
