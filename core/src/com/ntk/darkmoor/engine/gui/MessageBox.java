package com.ntk.darkmoor.engine.gui;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.ntk.darkmoor.engine.CampDialog;
import com.ntk.darkmoor.engine.GameColors;
import com.ntk.darkmoor.stub.GameTime;

public class MessageBox extends Dialog {

	public enum MessageBoxButtons {
		OK(0), OKCancel(1), AbortRetryIgnore(2), YesNoCancel(3), YesNo(4), RetryCancel(5);

		private int value;

		private MessageBoxButtons(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}

	public enum DialogResult {

		// / Nothing is returned from the dialog box. This means that the modal dialog continues running.
		None(0),

		// / The dialog box return value is OK
		OK(1),

		// / The dialog box return value is Cancel
		Cancel(2),

		// / The dialog box return value is Abort
		Abort(3),

		// / The dialog box return value is Retry
		Retry(4),

		// / The dialog box return value is Ignore
		Ignore(5),

		// / The dialog box return value is Yes
		Yes(6),

		// / The dialog box return value is No
		No(7);
		private int value;

		private DialogResult(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}

	private String text;
	private DialogResult dialogResult;
	private Rectangle rectangle;
	private List<ScreenButton> buttons;
	private boolean closing;
	
	private EventListener selectedListener;

	public MessageBox(String text, Skin skin) {
		this(text, skin, MessageBoxButtons.OK);
	}

	public MessageBox(String text, Skin skin, MessageBoxButtons mbButtons) {
		super(text, skin);

		this.text = text;
		dialogResult = DialogResult.None;

		// Background
		this.rectangle = new Rectangle(16, 40, 320, 112);

		this.buttons = new ArrayList<ScreenButton>();
		ScreenButton button = null;
		switch (mbButtons) {
		case OK:
			break;
		case OKCancel:
			break;
		case AbortRetryIgnore:
			break;
		case YesNoCancel:
			break;
		case YesNo: {
			button = new ScreenButton("Yes", new Rectangle(16, 74, 64, 28));
			button.setSelectedListener(new EventListener() {

				@Override
				public boolean handle(Event event) {
					return yesSelected(this, event);
				}

			});
			buttons.add(button);

			button = new ScreenButton("No", new Rectangle(240, 74, 64, 28));
			button.setSelectedListener(new EventListener() {

				@Override
				public boolean handle(Event event) {
					return noSelected(this, event);
				}

			});
			buttons.add(button);
		}
			break;
		case RetryCancel:
			break;
		}
	}

	private boolean yesSelected(Object sender, Event e) {
		dialogResult = DialogResult.Yes;
		closing = true;
		onSelectEntry();
		return true;
	}

	// / <summary>
	// / No selected
	// / </summary>
	// / <param name="sender"></param>
	// / <param name="e"></param>
	private boolean noSelected(Object sender, Event e) {
		dialogResult = DialogResult.No;
		closing = true;
		onSelectEntry();
		return true;
	}

	/**
	 * Converts the client coordinates of a specified point to screen coordinates.
	 * 
	 * @param offset
	 * @param rectangle
	 * @return
	 */
	private Rectangle clientToScreen(Vector2 offset, Rectangle rectangle) {
		return new Rectangle(offset.x + rectangle.x, offset.y + rectangle.y, rectangle.width, rectangle.height);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);

		if (batch == null)
			return;

		// Draw bevel background
		GUI.drawDoubleBevel(batch, rectangle, GameColors.Main, GameColors.Light, GameColors.Dark, false);

		// Draw message
		Vector2 point = new Vector2();
		rectangle.getPosition(point);
		point.add(6, 6);

		// TODO: ntk: check if it's equivalent to:
		// batch.drawString(GUI.getMenuFont(), point, Color.WHITE, text);
		GUI.getMenuFont().setColor(Color.WHITE);
		GUI.getMenuFont().draw(batch, text, point.x, point.y);

		// Draw buttons
		for (ScreenButton button : buttons) {
			Rectangle rect = clientToScreen(rectangle.getPosition(point), button.getRectangle());
			GUI.drawDoubleBevel(batch, rect, GameColors.Main, GameColors.Light, GameColors.Dark, false);

			// Text
			point = rect.getPosition(point);
			point.add(6, 6);

			// TODO: ntk: check if it's equivalent to:
			// batch.DrawString(GUI.MenuFont, point, button.TextColor, button.Text);
			GUI.getMenuFont().setColor(button.getTextColor());
			GUI.getMenuFont().draw(batch, button.getText(), point.x, point.y);

		}
	}

	public void update(GameTime time) {
		if (closing)
			return;

		// Update buttons
		Vector2 mousePos = new Vector2(Gdx.input.getX(), Gdx.input.getY());
		for (ScreenButton button : buttons) {
			
			Vector2 location = new Vector2();
			Rectangle rect = clientToScreen(rectangle.getPosition(location), button.getRectangle());
			
			if (rect.contains(mousePos)) {
				
				Color buttonTextColor = Color.WHITE;
				Color.rgb888ToColor(buttonTextColor, Color.rgb888(255, 85, 85));
				button.setTextColor(buttonTextColor);
				
				// TODO: ntk: here it was: Mouse.IsNewButtonDown! maybe we need to cache button downs
				if (Gdx.input.isButtonPressed(Buttons.LEFT))
					button.onSelectEntry();
				
			} else {
				button.setTextColor(Color.WHITE);
			}
		}
	}

	public void onSelectEntry() {
		if (selectedListener != null) {
			Event e = new Event();
			e.setTarget(this);
			selectedListener.handle(e);
		}
	}

	public boolean isClosing() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setCamp(CampDialog camp) {
		// TODO Auto-generated method stub

	}

	protected String getText() {
		return text;
	}

	protected void setText(String text) {
		this.text = text;
	}

	protected Rectangle getRectangle() {
		return rectangle;
	}

	protected void setRectangle(Rectangle rectangle) {
		this.rectangle = rectangle;
	}

	protected List<ScreenButton> getButtons() {
		return buttons;
	}

	protected void setButtons(List<ScreenButton> buttons) {
		this.buttons = buttons;
	}

	protected EventListener getSelectedListener() {
		return selectedListener;
	}

	protected void setSelectedListener(EventListener selectedListener) {
		this.selectedListener = selectedListener;
	}

	protected void setClosing(boolean closing) {
		this.closing = closing;
	}

	public DialogResult getDialogResult() {
		return dialogResult;
	}

}
