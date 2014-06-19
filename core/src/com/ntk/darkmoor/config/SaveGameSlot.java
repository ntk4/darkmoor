package com.ntk.darkmoor.config;

import org.ntk.commons.StringUtils;

import com.badlogic.gdx.utils.XmlReader.Element;

public class SaveGameSlot {

	private Element team;
	private Element dungeon;
	private String name;
	
	public String getName() {
		return name;
	}
	
	public Element getTeam() {
		return team;
	}

	public void setTeam(Element team) {
		this.team = team;
	}

	public Element getDungeon() {
		return dungeon;
	}

	public void setDungeon(Element dungeon) {
		this.dungeon = dungeon;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String toString() {
		if (StringUtils.isEmpty(name)) {
			return "Empty";
		}
		return name;
	}

}
