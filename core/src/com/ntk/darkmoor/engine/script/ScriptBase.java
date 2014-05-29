package com.ntk.darkmoor.engine.script;

import java.io.IOException;

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
import com.ntk.darkmoor.engine.script.actions.DisplayMessage;
import com.ntk.darkmoor.engine.script.actions.EnableChoice;
import com.ntk.darkmoor.engine.script.actions.EnableTarget;
import com.ntk.darkmoor.engine.script.actions.EndChoice;
import com.ntk.darkmoor.engine.script.actions.EndDialog;
import com.ntk.darkmoor.engine.script.actions.GiveExperience;
import com.ntk.darkmoor.engine.script.actions.GiveItem;
import com.ntk.darkmoor.engine.script.actions.Healing;
import com.ntk.darkmoor.engine.script.actions.JoinCharacter;
import com.ntk.darkmoor.engine.script.actions.PlaySound;
import com.ntk.darkmoor.engine.script.actions.SetTo;
import com.ntk.darkmoor.engine.script.actions.SpawnMonster;
import com.ntk.darkmoor.engine.script.actions.Teleport;
import com.ntk.darkmoor.engine.script.actions.ToggleTarget;
import com.ntk.darkmoor.stub.Dungeon;

public abstract class ScriptBase {

	private ActionBase action;

	private Dungeon dungeon;

	public ScriptBase() {

	}

	public void dispose() {
		if (action != null) {
			action.dispose();
		}
		action = null;
	}

	public boolean run() {

		if (action != null) {
			return action.run();
		}

		return false;
	}

	public boolean load(XmlReader.Element xml) {
		if (xml == null)
			return false;

		action = null;

		if (StringUtils.equals(SpawnMonster.TAG, xml.getName())) {
			action = new SpawnMonster();
		} else if (StringUtils.equals(EnableTarget.TAG, xml.getName())) {
			action = new EnableTarget();
		} else if (StringUtils.equals(DisableTarget.TAG, xml.getName())) {
			action = new DisableTarget();
		} else if (StringUtils.equals(ActivateTarget.TAG, xml.getName())) {
			action = new ActivateTarget();
		} else if (StringUtils.equals(DeactivateTarget.TAG, xml.getName())) {
			action = new DeactivateTarget();
		} else if (StringUtils.equals(ChangePicture.TAG, xml.getName())) {
			action = new ChangePicture();
		} else if (StringUtils.equals(ChangeText.TAG, xml.getName())) {
			action = new ChangeText();
		} else if (StringUtils.equals(DisableChoice.TAG, xml.getName())) {
			action = new DisableChoice();
		} else if (StringUtils.equals(EnableChoice.TAG, xml.getName())) {
			action = new EnableChoice();
		} else if (StringUtils.equals(EndChoice.TAG, xml.getName())) {
			action = new EndChoice();
		} else if (StringUtils.equals(EndDialog.TAG, xml.getName())) {
			action = new EndDialog();
		} else if (StringUtils.equals(GiveExperience.TAG, xml.getName())) {
			action = new GiveExperience();
		} else if (StringUtils.equals(GiveItem.TAG, xml.getName())) {
			action = new GiveItem();
		} else if (StringUtils.equals(Healing.TAG, xml.getName())) {
			action = new Healing();
		} else if (StringUtils.equals(JoinCharacter.TAG, xml.getName())) {
			action = new JoinCharacter();
		} else if (StringUtils.equals(PlaySound.TAG, xml.getName())) {
			action = new PlaySound();
		} else if (StringUtils.equals(SetTo.TAG, xml.getName())) {
			action = new SetTo();
		} else if (StringUtils.equals(Teleport.TAG, xml.getName())) {
			action = new Teleport();
		} else if (StringUtils.equals(ToggleTarget.TAG, xml.getName())) {
			action = new ToggleTarget();
		} else if (StringUtils.equals(DisplayMessage.TAG, xml.getName())) {
			action = new DisplayMessage();
		} else {
			Log.debug("[ScriptBase] Load() : Unknown node \"" + xml.getName() + "\" found.");
			return false;
		}

		if (action == null)
			return false;

		action.load(xml);

		return true;
	}
	
	public boolean save(XmlWriter writer) throws IOException {
		if (writer == null)
			return false;
		
		if (action != null)
			action.save(writer);

		return true;
	}

	public ActionBase getAction() {
		return action;
	}

	public void setAction(ActionBase action) {
		this.action = action;
	}

	public Dungeon getDungeon() {
		return dungeon;
	}

	public void setDungeon(Dungeon dungeon) {
		this.dungeon = dungeon;
	}
}
