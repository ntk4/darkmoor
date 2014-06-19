package com.ntk.darkmoor.engine.gui.campwindows;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.ntk.darkmoor.engine.CampDialog;
import com.ntk.darkmoor.engine.gui.BaseWindow;
import com.ntk.darkmoor.engine.gui.ScreenButton;

public class ScribeScrollsWindow extends BaseWindow {

	public ScribeScrollsWindow(CampDialog camp, Skin skin, Stage stage) {
		super(camp, "Scribe Scrolls :", skin, stage);
		
		ScreenButton button;
		
		button = new ScreenButton("Exit", new Rectangle(256, 244, 80, 28));
		button.addListener(new EventListener() {

			@Override
			public boolean handle(Event event) {
				return exitSelected(event);
			}

		});
		//getButtons().add(button);
		
		//TODO: ntk: implement the scribe logic and GUI
	}

	protected boolean exitSelected(Event event) {
		setClosing(true);
		return true;
	}

}
