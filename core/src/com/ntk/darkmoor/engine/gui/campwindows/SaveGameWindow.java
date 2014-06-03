package com.ntk.darkmoor.engine.gui.campwindows;

import org.ntk.commons.StringUtils;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.ntk.darkmoor.config.Settings;
import com.ntk.darkmoor.engine.CampDialog;
import com.ntk.darkmoor.engine.gui.BaseWindow;
import com.ntk.darkmoor.engine.gui.MessageBox;
import com.ntk.darkmoor.engine.gui.MessageBox.DialogResult;
import com.ntk.darkmoor.engine.gui.MessageBox.MessageBoxButtons;
import com.ntk.darkmoor.engine.gui.ScreenButton;
import com.ntk.darkmoor.stub.SaveGameSlot;

public class SaveGameWindow extends BaseWindow {

	private int selectedSlot;

	private Skin skin;

	public SaveGameWindow(CampDialog camp, Skin skin) {
		super(camp, "Save Game", skin);
		this.skin = skin;
		Settings.getSavedGame().load();

		ScreenButton button;
		for (int id = 0; id < Settings.getLastLoadedInstance().getSaveSlots(); id++) {
			SaveGameSlot slot = Settings.getSavedGame().getSlot(id);

			button = new ScreenButton(slot != null ? slot.getName() : "", new Rectangle(16, 40 + id * 34, 320, 28));
			button.addListener(new EventListener() {

				@Override
				public boolean handle(Event event) {
					return slotSelected(event);
				}

			});
			button.setTag(slot == null ? -1 : id);
			getButtons().add(button);
		}

		button = new ScreenButton("Cancel", new Rectangle(230, 244, 106, 28));
		button.addListener(new EventListener() {

			@Override
			public boolean handle(Event event) {
				return cancelSelected(event);
			}

		});
		getButtons().add(button);

		selectedSlot = -1;
	}

	protected boolean cancelSelected(Event event) {
		setClosing(true);
		return false;
	}

	protected boolean slotSelected(Event event) {
		// TODO: ntk: do we have a non-null tagret here?
		ScreenButton button = (ScreenButton) event.getTarget();

		selectedSlot = ((Integer) button.getTag());

		// Slot not empty, ask confirmation
		if (!StringUtils.isEmpty(button.getText())) {
			setMessageBox(new MessageBox("Are you sure you<br />wish to SAVE<br />the game ?", skin,
					MessageBoxButtons.YesNo));
			getMessageBox().addListener(new EventListener() {

				@Override
				public boolean handle(Event event) {
					return messageBoxSelected(event);
				}

			});

			return true;
		}

		// If ingame, then load the savegame
		if (getCamp() != null) {
			Settings.getSavedGame().getSlot(selectedSlot);
			getCamp().exit();
		}
		return true;
	}

	protected boolean messageBoxSelected(Event event) {
		if (((MessageBox) event.getTarget()).getDialogResult() == DialogResult.Yes && getCamp() != null) {
			Settings.getSavedGame().getSlot(selectedSlot);
			getCamp().exit();
		}
		return true;
	}

	public int getSelectedSlot() {
		return selectedSlot;
	}

}
