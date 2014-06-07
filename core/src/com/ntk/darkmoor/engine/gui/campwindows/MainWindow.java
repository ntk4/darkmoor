package com.ntk.darkmoor.engine.gui.campwindows;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.ntk.darkmoor.engine.CampDialog;
import com.ntk.darkmoor.engine.Hero.HeroClass;
import com.ntk.darkmoor.engine.gui.BaseWindow;
import com.ntk.darkmoor.engine.gui.ScreenButton;

public class MainWindow extends BaseWindow {

	private Skin skin;

	public MainWindow(CampDialog camp, Skin skin) {
		super(camp, "Camp :", skin);
		this.skin = skin;

		ScreenButton button;

		button = new ScreenButton("Rest Party", new Rectangle(16, 40, 320, 28));
		button.addListener(new EventListener() {

			@Override
			public boolean handle(Event event) {
				return restPartySelected(event);
			}

		});
		getButtons().add(button);
		
		button = new ScreenButton("Memorize Spells", new Rectangle(16, 74, 320, 28));
		button.addListener(new EventListener() {

			@Override
			public boolean handle(Event event) {
				return memorizeSpellsSelected(event);
			}

		});
		getButtons().add(button);
		
		button = new ScreenButton("Pray for Spells", new Rectangle(16, 108, 320, 28));
		button.addListener(new EventListener() {

			@Override
			public boolean handle(Event event) {
				return prayForSpellsSelected(event);
			}

		});
		getButtons().add(button);
		
		button = new ScreenButton("Scribe Scrolls", new Rectangle(16, 142, 320, 28));
		button.addListener(new EventListener() {

			@Override
			public boolean handle(Event event) {
				return scribeScrollsSelected(event);
			}

		});
		getButtons().add(button);
		
		button = new ScreenButton("Preferences", new Rectangle(16, 176, 320, 28));
		button.addListener(new EventListener() {

			@Override
			public boolean handle(Event event) {
				return preferencesSelected(event);
			}

		});
		getButtons().add(button);
		
		button = new ScreenButton("Game Options", new Rectangle(16, 210, 320, 28));
		button.addListener(new EventListener() {

			@Override
			public boolean handle(Event event) {
				return gameOptionsSelected(event);
			}

		});
		getButtons().add(button);
		
		button = new ScreenButton("Exit", new Rectangle(256, 244, 80, 28));
		button.addListener(new EventListener() {

			@Override
			public boolean handle(Event event) {
				return exitSelected(event);
			}

		});
		getButtons().add(button);
	}
	
	protected boolean exitSelected(Event event) {
		setClosing(true);
		return false;
	}

	protected boolean gameOptionsSelected(Event event) {
		getCamp().addWindow(new GameOptionsWindow(getCamp(), skin));
		return false;
	}
	
	protected boolean preferencesSelected(Event event) {
		getCamp().addWindow(new PreferencesWindow(getCamp(), skin));
		return false;
	}
	
	protected boolean scribeScrollsSelected(Event event) {
		getCamp().addWindow(new ScribeScrollsWindow(getCamp(), skin));
		return false;
	}
	
	protected boolean prayForSpellsSelected(Event event) {		
		SpellWindow window = new SpellWindow(getCamp(), skin);
		window.setMessage("Select a character<br />from your party<br />who would like to<br />pray for spells.");
		window.setFilter(HeroClass.Cleric.value() | HeroClass.Paladin.value());
		getCamp().addWindow(window);
		return false;
	}
	
	protected boolean memorizeSpellsSelected(Event event) {		
		SpellWindow window = new SpellWindow(getCamp(), skin);
		window.setMessage("Select a character<br />from your party<br />who would like to<br />memorize spells.");
		window.setFilter(HeroClass.Mage.value());
		getCamp().addWindow(window);
		return false;
	}
	
	protected boolean restPartySelected(Event event) {
		getCamp().addWindow(new RestPartyWindow(getCamp(), skin));
		return false;
	}
	
}
