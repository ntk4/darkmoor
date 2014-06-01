package com.ntk.darkmoor.engine.gui;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.ntk.darkmoor.stub.CampDialog;
import com.ntk.darkmoor.stub.GameColors;
import com.ntk.darkmoor.stub.GameTime;

public class BaseWindow extends Window {

	private List<ScreenButton> buttons;
	private CampDialog camp;
	private boolean closing;
	private MessageBox messageBox;

	public BaseWindow(CampDialog camp, String title, Skin skin) {
		super(title, skin);
		this.camp = camp;
		buttons = new ArrayList<ScreenButton>();
		closing = false;
		setSkin(skin);
	}

	public void close() {
		closing = true;
	}

	public void update(GameTime time) {
		if (messageBox != null && !messageBox.isClosing())
			messageBox = null;

		if (messageBox != null)
			messageBox.update(time);
		else
			updateButtons(time, buttons);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);

		// Draw background
		drawBackground(batch, parentAlpha);

		// Draw buttons
		drawButtons(batch, parentAlpha, buttons);
	}

	private void drawButtons(Batch batch, float parentAlpha, List<ScreenButton> buttons2) {
		// Draw buttons
		for (ScreenButton button : buttons) {
			if (!button.isVisible())
				continue;

			GUI.drawDoubleBevel(batch, button.getRectangle());// , GameColors.Main, GameColors.Light, GameColors.Dark);

			// Text
			// TODO: ntk: check if it's equivalent to the initial:
			// Point point = button.Rectangle.Location;
			Vector2 position = new Vector2();
			button.getRectangle().getPosition(position);

			position.add(6, 6);

			// TODO: ntk: check if it's equivalent to:
			// batch.DrawString(GUI.MenuFont, point, button.TextColor, button.Text);
			GUI.getMenuFont().setColor(button.getTextColor());
			GUI.getMenuFont().draw(batch, button.getText(), position.x, position.y);
		}

		// Message box
		if (messageBox != null) {
			messageBox.setCamp(camp);
			messageBox.draw(batch, parentAlpha);
		}
	}

	private void drawBackground(Batch batch, float parentAlpha) {
		Rectangle rect = new Rectangle(0, 0, 352, 288);
		GUI.drawDoubleBevel(batch, rect, GameColors.main, GameColors.light, GameColors.dark, false);

		// TODO: ntk: check if it's equivalent to:
		// batch.DrawString(GUI.MenuFont, new Point(8, 10), GameColors.Cyan, Title);
		GUI.getMenuFont().setColor(GameColors.CYAN);
		GUI.getMenuFont().draw(batch, getTitle(), 8, 10);

	}

	private void updateButtons(GameTime time, List<ScreenButton> buttons2) {
		// update message box
		if (messageBox != null) {
			messageBox.update(time);

			return;
		}

		// Update buttons
		Vector2 mousePos = new Vector2(Gdx.input.getX(), Gdx.input.getY());
		for (ScreenButton button : buttons) {
			if (!button.isVisible())
				continue;

			if (button.getRectangle().contains(mousePos)) {
				if (button.isReactOnMouseOver())
					button.setTextColor(GameColors.Red);

				// Click on button
				if (Gdx.input.isButtonPressed(Buttons.LEFT))
					button.onSelectEntry();
				
			} else if (button.isReactOnMouseOver()) {
				button.setTextColor(Color.WHITE);
			}
		}
	}

	public List<ScreenButton> getButtons() {
		return buttons;
	}

	protected void setButtons(List<ScreenButton> buttons) {
		this.buttons = buttons;
	}

	public CampDialog getCamp() {
		return camp;
	}

	protected void setCamp(CampDialog camp) {
		this.camp = camp;
	}

	protected boolean isClosing() {
		return closing;
	}

	public void setClosing(boolean closing) {
		this.closing = closing;
	}

	protected MessageBox getMessageBox() {
		return messageBox;
	}

	protected void setMessageBox(MessageBox messageBox) {
		this.messageBox = messageBox;
	}

}
