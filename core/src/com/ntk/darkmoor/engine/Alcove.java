package com.ntk.darkmoor.engine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.ntk.commons.StringUtils;

import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.ntk.darkmoor.engine.script.AlcoveScript;
import com.ntk.darkmoor.stub.DungeonLocation;

/**
 * Alcove object
 * 
 * @author Nick
 * 
 */
public class Alcove {

	private int value;
	private int decoration;
	private boolean acceptBigItems;
	private boolean hideItems;
	private ArrayList<AlcoveScript> onAddedItemScripts;
	private ArrayList<AlcoveScript> onRemovedItemScripts;

	public Alcove() {
		decoration = -1;
		hideItems = false;
		acceptBigItems = false;
		onAddedItemScripts = new ArrayList<AlcoveScript>();
		onRemovedItemScripts = new ArrayList<AlcoveScript>();
	}

	/**
	 * Adds an item to the alcove
	 * 
	 * @return true if the item can be added to the alcove
	 */
	public boolean addItem() {
		for (AlcoveScript script : onAddedItemScripts)
			script.run();

		return true;
	}

	/**
	 * Removes an item to the alcove
	 * 
	 * @return true if the item can be remove to the alcove
	 */
	public boolean removeItem() {
		for (AlcoveScript script : onRemovedItemScripts)
			script.run();

		return true;
	}

	public DungeonLocation[] getTargets() {
		List<DungeonLocation> list = new ArrayList<DungeonLocation>();
		for (AlcoveScript script : onAddedItemScripts)
			if (script.getAction() != null && script.getAction().getTarget() != null)
				list.add(script.getAction().getTarget());

		for (AlcoveScript script : onRemovedItemScripts)
			if (script.getAction() != null && script.getAction().getTarget() != null)
				list.add(script.getAction().getTarget());

		return list.toArray(new DungeonLocation[] {}); // just for the conversion..
	}

	public boolean load(XmlReader.Element node) {
		if (node == null)
			return false;

		decoration = Integer.parseInt(node.getAttribute("deco"));
		hideItems = Boolean.parseBoolean(node.getAttribute("hide"));
		acceptBigItems = Boolean.parseBoolean(node.getAttribute("bigitems"));

		for (int i = 0; i < node.getChildCount(); i++) {
			XmlReader.Element xml = node.getChild(i);

			if (StringUtils.equals("onaddeditem", xml.getName())) {
				for (int j = 0; j < xml.getChildCount(); j++) {
					AlcoveScript script = new AlcoveScript();
					script.load(node.getChild(j));
					onAddedItemScripts.add(script);
				}

			} else if (StringUtils.equals("onremoveditem", xml.getName())) {
				for (int j = 0; j < xml.getChildCount(); j++) {
					AlcoveScript script = new AlcoveScript();
					script.load(node.getChild(j));
					onRemovedItemScripts.add(script);
				}

			}
		}
		return true;
	}

	public boolean save(XmlWriter writer) throws IOException {
		if (writer == null)
			return false;

		writer.attribute("deco", decoration);
		writer.attribute("hide", hideItems);
		writer.attribute("bigitems", acceptBigItems);
		
		if (onAddedItemScripts.size() > 0) {
			writer.element("onaddeditem");
			for (AlcoveScript action: onAddedItemScripts) {
				action.save(writer);
			}
			writer.pop();
		}
		
		if (onRemovedItemScripts.size() > 0) {
			writer.element("onremoveditem");
			for (AlcoveScript action: onRemovedItemScripts) {
				action.save(writer);
			}
			writer.pop();
		}

		return true;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getDecoration() {
		return decoration;
	}

	public void setDecoration(int decoration) {
		this.decoration = decoration;
	}

	public boolean isAcceptBigItems() {
		return acceptBigItems;
	}

	public void setAcceptBigItems(boolean acceptBigItems) {
		this.acceptBigItems = acceptBigItems;
	}

	public boolean isHideItems() {
		return hideItems;
	}

	public void setHideItems(boolean hideItems) {
		this.hideItems = hideItems;
	}

	public ArrayList<AlcoveScript> getOnAddedItemScripts() {
		return onAddedItemScripts;
	}

	public ArrayList<AlcoveScript> getOnRemovedItemScripts() {
		return onRemovedItemScripts;
	}


}
