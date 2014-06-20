package com.ntk.darkmoor.engine.gui.campwindows;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.ntk.darkmoor.DarkmoorGame;
import com.ntk.darkmoor.engine.CampDialog;
import com.ntk.darkmoor.engine.gui.BaseWindow;
import com.ntk.darkmoor.engine.gui.MessageBox;
import com.ntk.darkmoor.engine.gui.MessageBox.DialogResult;
import com.ntk.darkmoor.engine.gui.MessageBox.MessageBoxButtons;
import com.ntk.darkmoor.engine.gui.ScreenButton;
import com.ntk.darkmoor.stub.GameScreenBase;

public class GameOptionsWindow extends BaseWindow {

	private static final String WINDOW_TITLE = "Game Options:";
	private Skin skin;

	public GameOptionsWindow(CampDialog camp, GameScreenBase parent) {
		super(camp, WINDOW_TITLE, parent);

		ScreenButton button;

		button = new ScreenButton("Load Game", new Rectangle(16, 40, 320, 28));
		button.addListener(new EventListener() {

			@Override
			public boolean handle(Event event) {
				return loadSelected(event);
			}

		});
//		//getButtons().add(button);

		button = new ScreenButton("Save Game", new Rectangle(16, 74, 320, 28));
		button.addListener(new EventListener() {

			@Override
			public boolean handle(Event event) {
				return saveSelected(event);
			}

		});
//		//getButtons().add(button);

		button = new ScreenButton("Drop Character", new Rectangle(16, 108, 320, 28));
		button.addListener(new EventListener() {

			@Override
			public boolean handle(Event event) {
				return dropHeroSelected(event);
			}

		});
//		//getButtons().add(button);

		button = new ScreenButton("Quit Game", new Rectangle(16, 142, 320, 28));
		button.addListener(new EventListener() {

			@Override
			public boolean handle(Event event) {
				return quitSelected(event);
			}

		});
//		//getButtons().add(button);

		button = new ScreenButton("Exit", new Rectangle(256, 244, 80, 28));
		button.addListener(new EventListener() {

			@Override
			public boolean handle(Event event) {
				return exitSelected(event);
			}

		});
		//getButtons().add(button);
	}

	protected boolean exitSelected(Event event) {
		setClosing(true);
		return false;
	}

	protected boolean quitSelected(Event event) {
		setMessageBox(new MessageBox("Are you sure you<br />wish to EXIT the<br />game ?", skin,
				MessageBoxButtons.YesNo));
		getMessageBox().addListener(new EventListener() {

			@Override
			public boolean handle(Event event) {
				return quitAnswer(event);
			}

		});
		return false;
	}

	protected boolean quitAnswer(Event event) {
		if (((MessageBox) event.getTarget()).getDialogResult() == DialogResult.Yes)
			DarkmoorGame.exit();
		return true;
	}

	protected boolean dropHeroSelected(Event event) {
		getCamp().addWindow(new DropNPCWindow(getCamp(), parent));
		return false;
	}

	protected boolean saveSelected(Event event) {
		getCamp().addWindow(new SaveGameWindow(getCamp(), parent));
		return false;
	}

	protected boolean loadSelected(Event event) {
		getCamp().addWindow(new LoadGameWindow(getCamp(), parent));
		return false;
	}

	void saveAnswer(Event e) {
		if (((MessageBox) e.getTarget()).getDialogResult() == DialogResult.Yes) {
			// GameScreen.Team.SaveParty();
			getCamp().exit();
		}
	}

}
