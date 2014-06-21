package com.ntk.darkmoor.engine.gui.campwindows;

import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.ntk.darkmoor.GameScreen;
import com.ntk.darkmoor.engine.CampDialog;
import com.ntk.darkmoor.engine.GameColors;
import com.ntk.darkmoor.engine.Hero;
import com.ntk.darkmoor.engine.Hero.HeroClass;
import com.ntk.darkmoor.engine.Team;
import com.ntk.darkmoor.engine.gui.BaseWindow;
import com.ntk.darkmoor.engine.gui.GUI;
import com.ntk.darkmoor.engine.gui.ScreenButton;
import com.ntk.darkmoor.resource.TextureSet;
import com.ntk.darkmoor.stub.GameScreenBase;

public class SpellWindow extends BaseWindow {

	private ScreenButton[] levels;
	private Color rectangleColor;
	private Hero hero;
	private CharSequence message;
	private Set<HeroClass> filter;
	private TextureSet textureSetInterface;
	private int spellLevel;

	public SpellWindow(CampDialog camp, GameScreenBase parent) {
		super(camp, "", parent);

		ScreenButton button = new ScreenButton("Exit", new Rectangle(256, 244, 80, 28));
		button.addListener(new EventListener() {

			@Override
			public boolean handle(Event event) {
				return exitSelected(event);
			}

		});
		//getButtons().add(button);

		levels = new ScreenButton[6];
		for (int i = 0; i < 6; i++) {
			levels[i] = new ScreenButton(String.valueOf(i + 1), new Rectangle(22 + i * 54, 32, 40, 36));
			levels[i].addListener(new EventListener() {

				@Override
				public boolean handle(Event event) {
					return levelSelected(event);
				}

			});
			levels[i].setReactOnMouseOver(false);
			levels[i].setVisible(false);
			//getButtons().add(levels[i]);
		}

		rectangleColor = Color.WHITE;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);

		Team team = GameScreen.getTeam();

		GUI.getMenuFont().setColor(rectangleColor);
		// Display message
		if (hero == null) {
			GUI.getMenuFont().draw(batch, message, 26, 58);
		} else {
			GUI.getMenuFont().draw(batch, "0 of 0 remaining.", 16, 76);
		}

		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 2; x++) {
				Hero currentHero = team.getHeroes()[y * 2 + x];
				if (currentHero == null)
					continue;

				if (currentHero == this.hero) {
					// float col = (float) Math.sin(1.0f); //ntk: not used?
					GUI.drawRectangle(new Rectangle(366 + x * 144, 2 + y * 104, 130, 104), Color.WHITE);
					GUI.drawRectangle(new Rectangle(367 + x * 144, 4 + y * 104, 128, 101), Color.WHITE);
					// } else if (!currentHero.checkClass(filter)) {
					// Ghost name
					// TODO: ntk: find out how to draw tilesets and uncomment this
					// batch.DrawTile(textureSetInterface, 31, new Vector2(368 + 144 * x, y * 104 + 4));
				}
			}
		}
	}

	@Override
	public void update(float delta) {
		super.update(delta);

		// int col = (int) (Math.Sin(time.ElapsedGameTime.Milliseconds) * 255.0f);
		// RectangleColor = Color.FromArgb(255, col, col, col);

		// Select a new hero
		if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
			for (int y = 0; y < 3; y++) {
				for (int x = 0; x < 2; x++) {
					if (updateHero(y, x))
						break;
				}
			}
		}
	}

	/**
	 * 
	 * @param y
	 * @param x
	 * @return if update was handled or should continue
	 */
	private boolean updateHero(int y, int x) {
		Hero currentHero = GameScreen.getTeam().getHeroes()[y * 2 + x];
		if (hero == null)
			return true;

		if (new Rectangle(368 + x * 144, 4 + y * 104, 126, 100).contains(Gdx.input.getX(), Gdx.input.getY())) {
			heroSelected();
			this.hero = currentHero;
			return true;
		}
		return false;
	}

	private void heroSelected() {
		// Buttons already present
		if (hero != null)
			return;

		setTitle("Spells Available :");

		ScreenButton button;
		button = new ScreenButton("Clear", new Rectangle(16, 244, 96, 28));
		button.addListener(new EventListener() {

			@Override
			public boolean handle(Event event) {
				return clearSelected(event);
			}

		});
		//getButtons().add(button);

		for (int i = 0; i < 6; i++)
			levels[i].setVisible(true);

		levels[0].setTextColor(GameColors.Red);
		spellLevel = 1;
	}

	protected boolean clearSelected(Event event) {
		// TODO: nothing happens here? can't be correct
		return true;
	}

	protected boolean levelSelected(Event event) {
		for (int i = 0; i < 6; i++)
			levels[i].setTextColor(Color.WHITE);

		ScreenButton button = ((ScreenButton) event.getTarget());
		button.setTextColor(GameColors.Red);
		return true;
	}

	protected boolean exitSelected(Event event) {
		setClosing(true);
		return true;
	}

	public int getCount() {
		int count = 0;
		for (Hero hero : GameScreen.getTeam().getHeroes()) {
			// Hero applies
			for (HeroClass heroClass : hero.getClasses()) {
				if (hero != null && hero.checkClass(heroClass)) {
					count++;
					break; // next hero
				}
			}
		}

		return count;
	}

	public ScreenButton[] getLevels() {
		return levels;
	}

	public void setLevels(ScreenButton[] levels) {
		this.levels = levels;
	}

	public Color getRectangleColor() {
		return rectangleColor;
	}

	public void setRectangleColor(Color rectangleColor) {
		this.rectangleColor = rectangleColor;
	}

	public Object getHero() {
		return hero;
	}

	public void setHero(Hero hero) {
		this.hero = hero;
	}

	public CharSequence getMessage() {
		return message;
	}

	public void setMessage(CharSequence message) {
		this.message = message;
	}

	public Set<HeroClass> getFilter() {
		return filter;
	}

	public void setFilter(Set<HeroClass> filter) {
		this.filter = filter;
	}

	public TextureSet getTextureSetInterface() {
		return textureSetInterface;
	}

	public void setTextureSetInterface(TextureSet textureSetInterface) {
		this.textureSetInterface = textureSetInterface;
	}

	public int getSpellLevel() {
		return spellLevel;
	}

	public void setSpellLevel(int spellLevel) {
		this.spellLevel = spellLevel;
	}

}
