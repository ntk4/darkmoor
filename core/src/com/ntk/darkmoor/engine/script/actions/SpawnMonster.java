package com.ntk.darkmoor.engine.script.actions;

import java.io.IOException;

import org.ntk.commons.StringUtils;

import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.ntk.darkmoor.engine.Monster;
import com.ntk.darkmoor.resource.Resources;

public class SpawnMonster extends ActionBase {

	public static final String TAG = "SpawnMonster";

	private String monsterName;

	public SpawnMonster() {
		name = TAG;
	}

	@Override
	public boolean run() {
		if (target == null || StringUtils.isEmpty(monsterName))
			return false;
		// Spawn the monster
		Monster monster = Resources.createAsset(Monster.class, monsterName);
		if (monster == null)
			return false;

		monster.teleport(target);
		monster.onSpawn();

		return true;
	}

	@Override
	public String toString() {
		return String.format("Spawn monster \"%s\" at %s", monsterName, (target != null ? target.toStringShort() : ""));
	}

	@Override
	public boolean load(XmlReader.Element xml) {
		if (xml == null || !TAG.equals(xml.getName()))
			return false;

		if (!StringUtils.isEmpty(xml.getAttribute("name"))) {
			monsterName = xml.getAttribute("name");
		}

		if (xml.getChildCount() > 0)
			super.load(xml.getChild(0));

		return true;
	}

	@Override
	public boolean save(XmlWriter writer) throws IOException {
		if (writer == null)
			return false;

		writer.element(TAG).attribute("name", monsterName);

		super.save(writer);

		writer.pop();

		return true;
	}

	public String getMonsterName() {
		return monsterName;
	}

	public void setMonsterName(String monsterName) {
		this.monsterName = monsterName;
	}
}
