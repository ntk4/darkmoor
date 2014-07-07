package com.ntk.darkmoor.config;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.ntk.commons.StringUtils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.ntk.darkmoor.exception.SaveException;
import com.ntk.darkmoor.resource.ResourceUtility;

public class SaveGame {

	public final String TAG = "savegame";
	private String fileName;
	private SaveGameSlot[] slots;
	private String dungeonName;

	public SaveGame(String filename) {
		this.fileName = filename;
		slots = new SaveGameSlot[Settings.getLastLoadedInstance().getSaveSlots()];
	}

	public SaveGameSlot getSlot(int id) {
		if (id >= 0 && id < slots.length) {
			return slots[id];
		}
		return null;
	}
	
	public boolean isSlotEmpty(int id) {
		SaveGameSlot slot = getSlot(id);
		return (slot != null && slot.getTeam() == null);
	}

	public boolean load() {
		if (ResourceUtility.isStandaloneMode()) {
			if (!new File(fileName).exists())
				Log.error("SaveGame.load: unable to find save file '%s'!", fileName);
		} else if (!Gdx.files.internal(fileName).exists()) {
			Log.error("SaveGame.load: unable to find save file '%s'!", fileName);
		}

		for (int i = 0; i < slots.length; i++)
			slots[i] = new SaveGameSlot();

		Element xml = ResourceUtility.extractRootElement(fileName);

		if (xml == null)
			return false;

		String name = null;
		for (int i = 0; i < xml.getChildCount(); i++) {
			Element slot = xml.getChild(i);
			name = slot.getName();

			if ("slot".equalsIgnoreCase(name)) {
				int id = Integer.parseInt(slot.getAttribute("id"));
				slots[id].setName(slot.getAttribute("name"));

				for (int j = 0; j < slot.getChildCount(); j++) {
					Element child = slot.getChild(j);
					String childname = child.getName();

					if ("team".equalsIgnoreCase(childname)) {
						slots[id].setTeam(child);
					} else if ("dungeon".equalsIgnoreCase(childname)) {
						slots[id].setDungeon(child);
					}
				}
			} else if ("dungeon".equalsIgnoreCase(name)) {
				dungeonName = slot.getAttribute("name");
			}
		}

		return true;
	}

	public boolean save() throws IOException {

		if (StringUtils.isEmpty(fileName)) {
			throw new SaveException("[SaveGame.load] File name not set");
		}

		XmlWriter writer = new XmlWriter(new FileWriter(fileName));
		writer.element(TAG).element("dungeon").attribute("name", dungeonName).pop();

		for (int id = 0; id < slots.length; id++) {
			if (slots[id] == null)
				continue;

			writer.element("slot").attribute("id", id).attribute("name", slots[id].getName());

			if (slots[id].getTeam() != null)
				writer.write(slots[id].getTeam().toString());

			if (slots[id].getDungeon() != null)
				writer.write(slots[id].getDungeon().toString());

			writer.pop();
		}

		writer.pop().pop();
		writer.close();

		return true;
	}

	public String getFileName() {
		return fileName;
	}

	public SaveGameSlot[] getSlots() {
		return slots;
	}

	public String getDungeonName() {
		return dungeonName;
	}

}
