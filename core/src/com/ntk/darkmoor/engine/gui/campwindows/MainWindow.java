package com.ntk.darkmoor.engine.gui.campwindows;

import java.util.HashSet;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.ntk.darkmoor.engine.CampDialog;
import com.ntk.darkmoor.engine.Hero.HeroClass;
import com.ntk.darkmoor.engine.gui.BaseWindow;
import com.ntk.darkmoor.engine.gui.ScreenButton;
import com.ntk.darkmoor.stub.GameScreenBase;

public class MainWindow extends BaseWindow {

	private Skin skin;

	public MainWindow(CampDialog camp, GameScreenBase parent) {
		super(camp, "Camp :", parent);

		ScreenButton button;

		button = new ScreenButton("Rest Party", new Rectangle(16, 40, 320, 28));
		button.addListener(new EventListener() {

			@Override
			public boolean handle(Event event) {
				return restPartySelected(event);
			}

		});
		// //getButtons().add(button);

		button = new ScreenButton("Memorize Spells", new Rectangle(16, 74, 320, 28));
		button.addListener(new EventListener() {

			@Override
			public boolean handle(Event event) {
				return memorizeSpellsSelected(event);
			}

		});
		// getButtons().add(button);

		button = new ScreenButton("Pray for Spells", new Rectangle(16, 108, 320, 28));
		button.addListener(new EventListener() {

			@Override
			public boolean handle(Event event) {
				return prayForSpellsSelected(event);
			}

		});
		// getButtons().add(button);

		button = new ScreenButton("Scribe Scrolls", new Rectangle(16, 142, 320, 28));
		button.addListener(new EventListener() {

			@Override
			public boolean handle(Event event) {
				return scribeScrollsSelected(event);
			}

		});
		// getButtons().add(button);

		button = new ScreenButton("Preferences", new Rectangle(16, 176, 320, 28));
		button.addListener(new EventListener() {

			@Override
			public boolean handle(Event event) {
				return preferencesSelected(event);
			}

		});
		// getButtons().add(button);

		button = new ScreenButton("Game Options", new Rectangle(16, 210, 320, 28));
		button.addListener(new EventListener() {

			@Override
			public boolean handle(Event event) {
				return gameOptionsSelected(event);
			}

		});
		// getButtons().add(button);

		button = new ScreenButton("Exit", new Rectangle(256, 244, 80, 28));
		button.addListener(new EventListener() {

			@Override
			public boolean handle(Event event) {
				return exitSelected(event);
			}

		});
		// getButtons().add(button);
	}

	protected boolean exitSelected(Event event) {
		setClosing(true);
		return false;
	}

	protected boolean gameOptionsSelected(Event event) {
		getCamp().addWindow(new GameOptionsWindow(getCamp(), parent));
		return false;
	}

	protected boolean preferencesSelected(Event event) {
		getCamp().addWindow(new PreferencesWindow(getCamp(), parent));
		return false;
	}

	protected boolean scribeScrollsSelected(Event event) {
		getCamp().addWindow(new ScribeScrollsWindow(getCamp(), parent));
		return false;
	}

	protected boolean prayForSpellsSelected(Event event) {
		SpellWindow window = new SpellWindow(getCamp(), parent);
		window.setMessage("Select a character<br />from your party<br />who would like to<br />pray for spells.");
		HashSet<HeroClass> filter = new HashSet<HeroClass>();
		filter.add(HeroClass.Cleric);
		filter.add(HeroClass.Paladin);
		window.setFilter(filter);
		getCamp().addWindow(window);
		return false;
	}

	protected boolean memorizeSpellsSelected(Event event) {
		SpellWindow window = new SpellWindow(getCamp(), parent);
		window.setMessage("Select a character<br />from your party<br />who would like to<br />memorize spells.");
		HashSet<HeroClass> filter = new HashSet<HeroClass>();
		filter.add(HeroClass.Mage);
		window.setFilter(filter);
		getCamp().addWindow(window);
		return false;
	}

	protected boolean restPartySelected(Event event) {
		getCamp().addWindow(new RestPartyWindow(getCamp(), parent));
		return false;
	}

}
