package com.ntk.darkmoor.engine;

import java.util.ArrayDeque;
import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.ntk.darkmoor.engine.gui.BaseWindow;
import com.ntk.darkmoor.engine.gui.ScreenButton;
import com.ntk.darkmoor.engine.gui.campwindows.MainWindow;
import com.ntk.darkmoor.stub.GameScreenBase;
import com.ntk.darkmoor.stub.Mouse;

public class CampDialog extends DialogBase {

	private GameScreenBase game;
	private Rectangle rectangle;
	private ArrayList<ScreenButton> buttons;
	private ArrayDeque<BaseWindow> windows;

	public CampDialog(GameScreenBase game, Skin skin, Stage stage) {
		this.game = game;
		windows = new ArrayDeque<BaseWindow>(); 
		buttons = new ArrayList<ScreenButton>();
		rectangle = new Rectangle(0, 0, 352, 288);
		addWindow(new MainWindow(this, skin, stage));
		Mouse.setTile(0);
	}

	public void exit() {
		Team team = GameScreenBase.getTeam();

		windows.clear();

		// Restore item in hand cursor
		if (team.getItemInHand() != null)
			Mouse.setTile(team.getItemInHand().getTextureID());

		super.exit();
	}

	public void addWindow(BaseWindow window) {
		if (window == null)
			return;

		windows.push(window);

	}

	@Override
	public void update(float delta) {
		if (windows.size() > 0) {
			// Remove closing windows
			while (windows.size() > 0 && windows.peek().isClosing())
				windows.pop();

			if (windows.isEmpty())
				exit();
			else
				windows.peek().update(delta);
		}
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		if (windows.size() > 0) {
			windows.peek().draw(batch, parentAlpha);
		}
	}

	public Rectangle getRectangle() {
		return rectangle;
	}

	public void setRectangle(Rectangle rectangle) {
		this.rectangle = rectangle;
	}

	public ArrayList<ScreenButton> getButtons() {
		return buttons;
	}

	protected void setButtons(ArrayList<ScreenButton> buttons) {
		this.buttons = buttons;
	}

	public ArrayDeque<BaseWindow> getWindows() {
		return windows;
	}

	protected void setWindows(ArrayDeque<BaseWindow> windows) {
		this.windows = windows;
	}

	public GameScreenBase getGame() {
		return game;
	}

}
