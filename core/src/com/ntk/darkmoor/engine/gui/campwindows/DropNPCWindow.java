package com.ntk.darkmoor.engine.gui.campwindows;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.ntk.darkmoor.engine.gui.BaseWindow;
import com.ntk.darkmoor.engine.gui.GUI;
import com.ntk.darkmoor.engine.gui.MessageBox;
import com.ntk.darkmoor.engine.gui.MessageBox.DialogResult;
import com.ntk.darkmoor.engine.gui.MessageBox.MessageBoxButtons;
import com.ntk.darkmoor.engine.gui.ScreenButton;
import com.ntk.darkmoor.resource.Resources;
import com.ntk.darkmoor.stub.CampDialog;
import com.ntk.darkmoor.stub.GameScreen;
import com.ntk.darkmoor.stub.GameTime;
import com.ntk.darkmoor.stub.Hero;
import com.ntk.darkmoor.stub.TileSet;

public class DropNPCWindow extends BaseWindow {

	private static final String WINDOW_TITLE = "Drop Character";

	private TileSet tileSetInterface;

	private Color rectangleColor;

	private String message;

	private Hero hero;

	private Skin skin;

	public DropNPCWindow(CampDialog camp, Skin skin) {
		super(camp, WINDOW_TITLE, skin);

		if (GameScreen.getTeam().getHeroCount() <= 4) {
			setClosing(true);
			return;
		}

		tileSetInterface = Resources.lockSharedAsset(TileSet.class, "Interface");

		// Adds buttons
		ScreenButton button;
		button = new ScreenButton("Exit", new Rectangle(256, 244, 80, 28));
		button.addListener(new EventListener() {

			@Override
			public boolean handle(Event event) {
				return exitSelected(event);
			}

		});
		getButtons().add(button);

		rectangleColor = Color.WHITE;
		message = "Select a character<br />from your party<br />who would like to<br />drop.";
		this.skin = skin;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);

		// Display message
		if (hero == null) {
			GUI.getMenuFont().draw(batch, message, 26, 58);
		} else {// TODO: ntk: why was it commented?
				// batch.DrawString(Camp.Font, new Point(16, 76), Color.White, "0 of 0 remaining.");
		}

		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 2; x++) {
				Hero hero = GameScreen.getTeam().getHero(y * 2 + x);
				if (hero == null)
					continue;

				// TODO: ntk: col not used?? what does it represent?
				// float col = (float) Math.sin(1.0f);
				GUI.drawRectangle(new Rectangle(366 + x * 144, 2 + y * 104, 130, 104), Color.WHITE);
				GUI.drawRectangle(new Rectangle(367 + x * 144, 4 + y * 104, 128, 101), Color.WHITE);
			}
		}
	}

	@Override
	public void update(GameTime time) {
		super.update(time);

		// int col = (int) (Math.Sin(time.ElapsedGameTime.Milliseconds) * 255.0f);
		// RectangleColor = Color.FromArgb(255, col, col, col);

		// Select a new hero
		if (Gdx.input.isButtonPressed(Buttons.LEFT) && hero == null) {
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
		Hero hero = GameScreen.getTeam().getHero(y * 2 + x);
		if (hero == null)
			return true;

		if (new Rectangle(368 + x * 144, 4 + y * 104, 126, 100).contains(Gdx.input.getX(), Gdx.input.getY())) {
			this.hero = hero;
			setMessageBox(new MessageBox("Are you sure you<br />wish to DROP<br />" + hero.getName() + " ?", skin,
					MessageBoxButtons.YesNo));
			getMessageBox().addListener(new EventListener() {

				@Override
				public boolean handle(Event event) {
					return dropAnswer(event);
				}

			});
			return true;
		}
		return false;
	}

	protected boolean exitSelected(Event event) {
		setClosing(true);
		return true;
	}

	protected boolean dropAnswer(Event event) {
		if (((MessageBox) event.getTarget()).getDialogResult() == DialogResult.Yes) {
			GameScreen.getTeam().dropHero(hero);
			getCamp().exit();
		}

		hero = null;
		return true;
	}

	public TileSet getTileSetInterface() {
		return tileSetInterface;
	}

	public void setTileSetInterface(TileSet tileSetInterface) {
		this.tileSetInterface = tileSetInterface;
	}

	public Color getRectangleColor() {
		return rectangleColor;
	}

	public void setRectangleColor(Color rectangleColor) {
		this.rectangleColor = rectangleColor;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Hero getHero() {
		return hero;
	}

	public Skin getSkin() {
		return skin;
	}

	public void setSkin(Skin skin) {
		this.skin = skin;
	}

}
