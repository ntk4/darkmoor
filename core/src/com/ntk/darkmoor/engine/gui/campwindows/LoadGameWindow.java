package com.ntk.darkmoor.engine.gui.campwindows;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.ntk.darkmoor.config.Settings;
import com.ntk.darkmoor.engine.CampDialog;
import com.ntk.darkmoor.engine.gui.BaseWindow;
import com.ntk.darkmoor.stub.SaveGameSlot;

public class LoadGameWindow extends BaseWindow {

	private int selectedSlot;

	public LoadGameWindow(CampDialog camp, Skin skin, Stage stage) {
		super(camp, "Load Game", skin, stage);
		selectedSlot = -1;
		setupWindow();
		setupButtons(skin);
		pack();
	}

	@Override
	protected void setupButtons(Skin uiSkin) {
		buttons = new TextButton[Settings.getLastLoadedInstance().getSaveSlots() + 1];

		for (int id = 0; id < Settings.getLastLoadedInstance().getSaveSlots(); id++) {
			SaveGameSlot slot = null;
			if (Settings.getSavedGame() != null)
				slot = Settings.getSavedGame().getSlot(id);
			final int slotNumber = id;

			buttons[id] = new TextButton(slot != null ? slot.getName() : "Empty slot " + (id + 1), uiSkin,
					"transparent-font64");
			// buttons[id].setBounds(320, id * heightUnit + 10, widthUnit, heightUnit);
			buttons[id].addListener(new InputListener() {
				public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
					slotSelected(slotNumber, event);
					return true;
				}
			});
			add(buttons[id]);
			row().minHeight(150).minWidth(300).fillY().fillX().expandX().expandY();

		}
		buttons[6] = new TextButton("Cancel", uiSkin, "transparent-font64");
		// buttons[6].setBounds(100, 7 * heightUnit + 10, widthUnit, heightUnit);
		buttons[6].addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				LoadGameWindow.this.close();
				stage.getRoot().removeActor(LoadGameWindow.this);
				return true;
			}
		});
		add(buttons[6]);
		row().minHeight(150).minWidth(300).fillY().fillX().expandX().expandY();

	}

	private void setupWindow() {
		// getButtonTable().add(new TextButton("X", uiSkin)).height(getPadTop());
		setPosition(Gdx.graphics.getWidth() / 2 - 150, 100);
		setWidth(Gdx.graphics.getWidth() >> 1);
		setHeight(Gdx.graphics.getHeight() - 100);
		defaults().spaceBottom(10);
		row().fill().expandX();

		// addAction(sequence(fadeIn(5000)));
	}

	protected boolean cancelSelected(Event event) {
		setClosing(true);
		return false;
	}

	protected boolean slotSelected(int slotNumber, Event event) {
		selectedSlot = slotNumber;

		// If ingame, then load the savegame
		if (getCamp() != null) {
			getCamp().getGame().loadGameSlot(selectedSlot);
			getCamp().exit();
		}

		return true;
	}

	public int getSelectedSlot() {
		return selectedSlot;
	}

}
