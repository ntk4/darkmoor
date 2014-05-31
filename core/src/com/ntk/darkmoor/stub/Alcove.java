package com.ntk.darkmoor.stub;

import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;

public class Alcove {

	private boolean hideItems;

	public int getDecoration() {
		// TODO Auto-generated method stub
		return 1;
	}

	public boolean isHideItems() {
		return hideItems;
	}

	public void setHideItems(boolean hideItems) {
		this.hideItems = hideItems;
	}

	public DungeonLocation[] getTargets() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean acceptBigItems() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean addItem() {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean removeItem() {
		// TODO Auto-generated method stub
		return true;
	}

	public void save(XmlWriter writer) {
		// TODO Auto-generated method stub
		
	}

	public void load(Element xml) {
		// TODO Auto-generated method stub
		
	}

}
