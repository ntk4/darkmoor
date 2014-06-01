package com.ntk.darkmoor.engine.gui.campwindows;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.ntk.darkmoor.engine.gui.BaseWindow;
import com.ntk.darkmoor.engine.gui.ScreenButton;
import com.ntk.darkmoor.stub.CampDialog;

public class ScribeScrollsWindow extends BaseWindow {

	public ScribeScrollsWindow(CampDialog camp, Skin skin) {
		super(camp, "Scribe Scrolls :", skin);
		
		ScreenButton button;
		
		button = new ScreenButton("Exit", new Rectangle(256, 244, 80, 28));
		button.addListener(new EventListener() {

			@Override
			public boolean handle(Event event) {
				return exitSelected(event);
			}

		});
		getButtons().add(button);
		
		//TODO: ntk: implement the scribe logic and GUI
	}

	protected boolean exitSelected(Event event) {
		setClosing(true);
		return true;
	}

}
