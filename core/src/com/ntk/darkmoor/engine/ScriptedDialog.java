package com.ntk.darkmoor.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.ntk.darkmoor.engine.actor.EventSquare;
import com.ntk.darkmoor.engine.gui.GUI;
import com.ntk.darkmoor.engine.script.gui.GUIScriptButton;
import com.ntk.darkmoor.engine.script.gui.ScriptChoice;
import com.ntk.darkmoor.stub.GameTime;

public class ScriptedDialog extends DialogBase {

	private EventSquare event;
	private Square square;
	private Texture picture;
	private int maxButtonCount = 3;
	private Texture border;
	private ScriptChoice[] choices;
	private GUIScriptButton[] buttons;
	private boolean quit;

	public ScriptedDialog(Square square, EventSquare evt) {
		if (square == null)
			throw new IllegalArgumentException("Square is null");

		if (evt == null)
			throw new IllegalArgumentException("EventSquare is null");

		event = evt;
		this.square = square;

		picture = new Texture(event.getPictureName());

		if (event.isDisplayBorder()) {
			border = new Texture("border.png");
		}

		// HACK: Hard coded maximum button for ScriptDialog
		choices = new ScriptChoice[maxButtonCount];
		buttons = new GUIScriptButton[maxButtonCount];
		for (int i = 0; i < maxButtonCount; i++) {
			buttons[i] = new GUIScriptButton();
			buttons[i].addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float f1, float f2) {
					buttonClick((GUIScriptButton) event.getListenerActor());
				}
			});
		}
	}

	@Override
	public void dispose() {
		if (picture != null)
			picture.dispose();
		picture = null;

		if (border != null)
			border.dispose();
		border = null;
	}

	@Override
	public void update(GameTime time) {
		// Debug
		if (Gdx.input.isButtonPressed(Buttons.MIDDLE))
			exit();

		// Rebuild button list
		int id = 0;
		choices[0] = null;
		choices[1] = null;
		choices[2] = null;
		for (ScriptChoice choice : event.getChoices()) {
			// choice disabled
			if (!choice.isEnabled())
				continue;

			// Add choice
			choices[id++] = choice;

			// Maximum reached
			if (id >= maxButtonCount)
				break;
		}

		// Compute button rectangles
		setChoice(choices[0], choices[1], choices[2]);

		// Update each choice button
		for (id = 0; id < maxButtonCount; id++) {
			if (choices[id] == null)
				break;

			buttons[id].update(time);
		}

	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		// Border
		if (event.isDisplayBorder())
			batch.draw(border, 0, 0);// , Color.WHITE);

		// Picture
		batch.draw(picture, 16, 16);// , Color.WHITE);

		// Text
		GUI.drawSimpleBevel(batch, DisplayCoordinates.getScriptedDialog());
		GUI.getDialogFont().setColor(GameColors.White);
		GUI.getDialogFont().draw(batch, event.getText(), 4, 250);

		// Choices
		for (int id = 0; id < maxButtonCount; id++) {
			if (choices[id] == null || !choices[id].isEnabled())
				continue;

			buttons[id].draw(batch);
		}

	}

	void buttonClick(GUIScriptButton button) {

		if (button.getTag() != null && button.getTag() instanceof ScriptChoice) {
			ScriptChoice choice = (ScriptChoice) button.getTag();
			choice.run();

			// Time to quit
			if (quit)
				return;
		}
	}

	public void SetChoices(int id, ScriptChoice choice) {
		if (id < 1 || id > 3)
			return;

		choices[id] = choice;

		buttons[id].setRectangle(DisplayCoordinates.getScriptedDialogChoices()[0]);
		buttons[id].setText(choice.getName());
		buttons[id].setTag(choice);
	}

	public void SetChoices(ScriptChoice choice) {
		choices[0] = choice;
		choices[1] = null;
		choices[2] = null;

		buttons[0].setRectangle(DisplayCoordinates.getScriptedDialogChoices()[0]);
		buttons[0].setText(choice.getName());
		buttons[0].setTag(choice);
	}

	public void SetChoices(ScriptChoice choice1, ScriptChoice choice2) {
		choices[0] = choice1;
		choices[1] = choice2;
		choices[2] = null;

		buttons[0].setRectangle(DisplayCoordinates.getScriptedDialogChoices()[3]);
		buttons[0].setText(choice1.getName());
		buttons[0].setTag(choice1);

		buttons[1].setRectangle(DisplayCoordinates.getScriptedDialogChoices()[4]);
		buttons[1].setText(choice2.getName());
		buttons[1].setTag(choice2);
	}

	private void setChoice(ScriptChoice choice1, ScriptChoice choice2, ScriptChoice choice3) {
		choices[0] = choice1;
		choices[1] = choice2;
		choices[2] = choice3;

		if (choice1 != null) {
			buttons[0].setRectangle(DisplayCoordinates.getScriptedDialogChoices()[6]);
			buttons[0].setText(choice1.getName());
			buttons[0].setTag(choice1);
		}

		if (choice2 != null) {
			buttons[1].setRectangle(DisplayCoordinates.getScriptedDialogChoices()[7]);
			buttons[1].setText(choice2.getName());
			buttons[1].setTag(choice2);
		}

		if (choice3 != null) {
			buttons[2].setRectangle(DisplayCoordinates.getScriptedDialogChoices()[8]);
			buttons[2].setText(choice3.getName());
			buttons[2].setTag(choice3);
		}
	}

	public boolean setPicture(String name) {
		if (picture != null)
			picture.dispose();

		picture = new Texture(name);

		return picture != null;
	}

	public void setPicture(Texture handle) {
		if (picture != null)
			picture.dispose();

		picture = handle;
	}

	public EventSquare getEvent() {
		return event;
	}

	public Square getSquare() {
		return square;
	}

	public int getMaxButtonCount() {
		return maxButtonCount;
	}

	public Texture getBorder() {
		return border;
	}

	public ScriptChoice[] getChoices() {
		return choices;
	}

	public void setChoices(ScriptChoice[] choices) {
		this.choices = choices;
	}

	public GUIScriptButton[] getButtons() {
		return buttons;
	}

	public boolean isQuit() {
		return quit;
	}
	public Texture getPicture() {
		return picture;
	}

}
