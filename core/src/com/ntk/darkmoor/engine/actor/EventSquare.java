package com.ntk.darkmoor.engine.actor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.ntk.commons.StringUtils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.ntk.darkmoor.engine.Compass.CardinalPoint;
import com.ntk.darkmoor.engine.GameMessage;
import com.ntk.darkmoor.engine.Hero;
import com.ntk.darkmoor.engine.SavingThrowType;
import com.ntk.darkmoor.engine.ScriptedDialog;
import com.ntk.darkmoor.engine.Square;
import com.ntk.darkmoor.engine.script.gui.ScriptChoice;
import com.ntk.darkmoor.stub.GameScreen;

public class EventSquare extends SquareActor {

	public static final String TAG = "eventsquare";

	private boolean mustFace;
	private int remaining;
	private Color messageColor;
	private CardinalPoint direction;
	private String text;
	private String soundName;
	private boolean loopSound;
	private boolean displayBorder;
	private int intelligence;
	private String message;
	private String pictureName;
	private List<ScriptChoice> choices;

	public EventSquare(Square square) {
		super(square);

		choices = new ArrayList<ScriptChoice>();
		messageColor = Color.WHITE;
		remaining = 1;
	}

	@Override
	public String toString() {
		return "Event";
	}

	@Override
	public boolean onTeamEnter() {
		// No more usage possible
		if (remaining == 0)
			return false;

		Hero hero = null;

		// Check if a hero detect the event
		for (Hero h : GameScreen.getTeam().getHeroes()) {
			// if (hero.SavingThrow(SavingThrowType.Will) > Dice.GetD20(1))
			if (h != null && h.savingThrow(SavingThrowType.Will) > intelligence) {
				hero = h;
				break;
			}
		}

		// No one is able to detect the event
		if (hero == null)
			return false;

		// Display message
		if (!StringUtils.isEmpty(message))
			GameMessage.addMessage(hero.getName() + ": " + message, messageColor);

		// Create the scripted dialog if there's a picture to show
		if (!StringUtils.isEmpty(pictureName))
			GameScreen.setDialog(new ScriptedDialog(getSquare(), this));

		// Decrement usage
		if (remaining > 0)
			remaining--;

		return true;

	}

	public boolean load(XmlReader.Element node) {
		if (node == null)
			return false;

		for (int i = 0; i < node.getChildCount(); i++) {
			XmlReader.Element xml = node.getChild(i);
			
			if (StringUtils.equals("choice", xml.getName())) {
				ScriptChoice choice = new ScriptChoice("");
				choice.load(xml);
				choices.add(choice);

			} else if (StringUtils.equals("messagecolor", xml.getName())) {
				// TODO: test if it's correct
				Color.rgb888ToColor(messageColor, Integer.parseInt(xml.getAttribute("value")));

			} else if (StringUtils.equals("mustface", xml.getName())) {
				mustFace = Boolean.parseBoolean(xml.getAttribute("value"));

			} else if (StringUtils.equals("direction", xml.getName())) {
				direction = CardinalPoint.valueOf(xml.getAttribute("value"));

			} else if (StringUtils.equals("soundname", xml.getName())) {
				soundName = xml.getAttribute("value");

			} else if (StringUtils.equals("loopsound", xml.getName())) {
				loopSound = Boolean.parseBoolean(xml.getAttribute("value"));

			} else if (StringUtils.equals("displayborder", xml.getName())) {
				displayBorder = Boolean.parseBoolean(xml.getAttribute("value"));

			} else if (StringUtils.equals("message", xml.getName())) {
				message = xml.getAttribute("value");

			} else if (StringUtils.equals("picturename", xml.getName())) {
				pictureName = xml.getAttribute("value");

			} else if (StringUtils.equals("intelligence", xml.getName())) {
				intelligence = Integer.parseInt(xml.getAttribute("value"));

			} else if (StringUtils.equals("remaining", xml.getName())) {
				remaining = Integer.parseInt(xml.getAttribute("value"));

			} else if (StringUtils.equals("text", xml.getName())) {
				text = xml.getText();

			} else {
				super.load(xml);
			}
		}

		return true;
	}

	public boolean save(XmlWriter writer) throws IOException {
		if (writer == null)
			return false;

		writer.element(TAG);

		super.save(writer);

		// TODO: in the original it was ToArgb() but here we use rgba8888!
		writer.element("messagecolor").attribute("value", Color.rgba8888(messageColor)).pop();

		writer.element("mustface").attribute("value", mustFace).pop();
		writer.element("direction").attribute("value", direction.toString()).pop();
		writer.element("remaining").attribute("value", remaining).pop();
		writer.element("soundname").attribute("value", soundName).pop();
		writer.element("loopsound").attribute("value", loopSound).pop();
		writer.element("displayborder").attribute("value", displayBorder).pop();
		writer.element("intelligence").attribute("value", intelligence).pop();
		writer.element("message").attribute("value", message).pop();
		writer.element("text").attribute("value", text).pop();
		writer.element("picturename").attribute("value", pictureName).pop();

		for (ScriptChoice choice : choices) {
			choice.save(writer);
		}

		writer.pop();

		return true;
	}

	public boolean isMustFace() {
		return mustFace;
	}

	public void setMustFace(boolean mustFace) {
		this.mustFace = mustFace;
	}

	public int getRemaining() {
		return remaining;
	}

	public void setRemaining(int remaining) {
		this.remaining = remaining;
	}

	public Color getMessageColor() {
		return messageColor;
	}

	public void setMessageColor(Color messageColor) {
		this.messageColor = messageColor;
	}

	public CardinalPoint getDirection() {
		return direction;
	}

	public void setDirection(CardinalPoint direction) {
		this.direction = direction;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getSoundName() {
		return soundName;
	}

	public void setSoundName(String soundName) {
		this.soundName = soundName;
	}

	public boolean isLoopSound() {
		return loopSound;
	}

	public void setLoopSound(boolean loopSound) {
		this.loopSound = loopSound;
	}

	public boolean isDisplayBorder() {
		return displayBorder;
	}

	public void setDisplayBorder(boolean displayBorder) {
		this.displayBorder = displayBorder;
	}

	public int getIntelligence() {
		return intelligence;
	}

	public void setIntelligence(int intelligence) {
		this.intelligence = intelligence;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getPictureName() {
		return pictureName;
	}

	public void setPictureName(String pictureName) {
		this.pictureName = pictureName;
	}

	public List<ScriptChoice> getChoices() {
		return choices;
	}

	public void setChoices(List<ScriptChoice> choices) {
		this.choices = choices;
	}

}
