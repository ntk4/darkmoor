package com.ntk.darkmoor.engine.script.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.ntk.commons.StringUtils;

import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.ntk.darkmoor.config.Log;
import com.ntk.darkmoor.engine.script.actions.ActionBase;
import com.ntk.darkmoor.engine.script.actions.ActivateTarget;
import com.ntk.darkmoor.engine.script.actions.ChangePicture;
import com.ntk.darkmoor.engine.script.actions.ChangeText;
import com.ntk.darkmoor.engine.script.actions.DeactivateTarget;
import com.ntk.darkmoor.engine.script.actions.DisableChoice;
import com.ntk.darkmoor.engine.script.actions.DisableTarget;
import com.ntk.darkmoor.engine.script.actions.EnableChoice;
import com.ntk.darkmoor.engine.script.actions.EndChoice;
import com.ntk.darkmoor.engine.script.actions.EndDialog;
import com.ntk.darkmoor.engine.script.actions.GiveExperience;
import com.ntk.darkmoor.engine.script.actions.GiveItem;
import com.ntk.darkmoor.engine.script.actions.Healing;
import com.ntk.darkmoor.engine.script.actions.JoinCharacter;
import com.ntk.darkmoor.engine.script.actions.PlaySound;
import com.ntk.darkmoor.engine.script.actions.Teleport;
import com.ntk.darkmoor.engine.script.actions.ToggleTarget;

public class ScriptChoice {

	private List<ActionBase> actions;
	private String name;
	private boolean enabled;
	private String text;

	public ScriptChoice(String name) {
		actions = new ArrayList<ActionBase>();

		this.name = name;
		this.enabled = true;
	}

	public void run() {
		for (ActionBase action : actions) {
			action.run();
		}
	}

	@Override
	public String toString() {
		return name + " - " + text + " <" + actions.size() + "> action(s)";
	}

	public boolean load(XmlReader.Element xml) {
		if (xml == null || !"choice".equalsIgnoreCase(xml.getName()))
			return false;

		this.name = xml.getAttribute("name");

		for (int i = 0; i < xml.getChildCount(); i++) {
			XmlReader.Element node = xml.getChild(i);

			// TODO: missing code: if node.nodeType == XmlNodeType.Comment then continue;

			if (StringUtils.equals(node.getName(), "actions")) {
				loadActions(node);

			} else {
				Log.debug(String.format("[ScriptChoice] Load() : Unknown node \"%s\"", node.getName()));
			}
		}

		return true;
	}

	public boolean save(XmlWriter writer) throws IOException {
		if (writer == null)
			return false;

		writer.element("choice").attribute("name", name);

		writer.element("actions");
		for (ActionBase action : actions) {
			action.save(writer);
		}
		writer.pop().pop();

		return true;
	}

	public void loadActions(XmlReader.Element xml) {
		if (xml == null || !"actions".equalsIgnoreCase(xml.getName()))
			return;

		for (int i = 0; i < xml.getChildCount(); i++) {
			XmlReader.Element node = xml.getChild(i);

			// TODO: missing code: if node.nodeType == XmlNodeType.Comment then continue;

			String lowerName = node.getName().toLowerCase();

			ActionBase script = null;

			if ("Teleport".equalsIgnoreCase(lowerName)) {
				script = new Teleport();
			} else if (StringUtils.equals("giveexperience", lowerName)) {
				script = new GiveExperience();
			} else if (StringUtils.equals("activate", lowerName)) {
				script = new ActivateTarget();
			} else if (StringUtils.equals("changepicture", lowerName)) {
				script = new ChangePicture();
			} else if (StringUtils.equals("changetext", lowerName)) {
				script = new ChangeText();
			} else if (StringUtils.equals("deactivate", lowerName)) {
				script = new DeactivateTarget();
			} else if (StringUtils.equals("disablechoice", lowerName)) {
				script = new DisableChoice();
			} else if (StringUtils.equals("disable", lowerName)) {
				script = new DisableTarget();
			} else if (StringUtils.equals("enablechoice", lowerName)) {
				script = new EnableChoice();
			} else if (StringUtils.equals("endchoice", lowerName)) {
				script = new EndChoice();
			} else if (StringUtils.equals("enddialog", lowerName)) {
				script = new EndDialog();
			} else if (StringUtils.equals("giveitem", lowerName)) {
				script = new GiveItem();
			} else if (StringUtils.equals("healing", lowerName)) {
				script = new Healing();
			} else if (StringUtils.equals("joincharacter", lowerName)) {
				script = new JoinCharacter();
			} else if (StringUtils.equals("playsound", lowerName)) {
				script = new PlaySound();
			} else if (StringUtils.equals("toggle", lowerName)) {
				script = new ToggleTarget();
			} else {
				Log.debug(String.format("[ScriptChoice] LoadActions() : Unknown node \"%s\"", node.getName()));
			}
			
			if (script != null) {
				script.load(node);
				actions.add(script);
			}
		}

	}

	public List<ActionBase> getActions() {
		return actions;
	}

	public void setActions(List<ActionBase> actions) {
		this.actions = actions;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
