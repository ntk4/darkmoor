package com.ntk.darkmoor.engine.actor;

import java.io.IOException;

import org.ntk.commons.StringUtils;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.ntk.darkmoor.config.Log;
import com.ntk.darkmoor.engine.Compass.CardinalPoint;
import com.ntk.darkmoor.engine.DungeonLocation;
import com.ntk.darkmoor.engine.Item;
import com.ntk.darkmoor.engine.Monster;
import com.ntk.darkmoor.engine.Square;
import com.ntk.darkmoor.engine.ViewField;
import com.ntk.darkmoor.engine.ViewField.ViewFieldPosition;
import com.ntk.darkmoor.stub.GameTime;
import com.ntk.darkmoor.stub.MouseButtons;

public abstract class SquareActor {
	private boolean enabled;
	private Square square;
	private boolean activated;
	private boolean canPassThrough;
	private boolean blocking;
	private boolean acceptItems;
	

	public SquareActor(Square square) {
		this.setSquare(square);
		this.setEnabled(true);
	}

	public void dispose() {

	}

	public void draw(ViewField field, ViewFieldPosition position, CardinalPoint direction) {
	}

	public void update(GameTime time) {
	}

	public DungeonLocation[] getTargets() {
		return new DungeonLocation[] {};
	}

	// TRIGGERS
	/**
	 * 
	 * @param location
	 * @param side
	 * @param button
	 * @return true if event is handled
	 */
	public boolean onClick(Vector2 location, CardinalPoint side, MouseButtons button) {
		return false;
	}

	/**
	 * Fired when the team enters the square
	 * 
	 * @return true if event is handled
	 */
	public boolean onTeamEnter() {
		return false;
	}

	/**
	 * Fired when the team leaves the square
	 * 
	 * @return true if event is handled
	 */
	public boolean onTeamLeave() {
		return false;
	}

	/**
	 * Fired when the team stands on a square
	 * 
	 * @return true if event is handled
	 */
	public boolean onTeamStand() {
		return false;
	}

	/**
	 * Fired when a monster enters the square
	 * 
	 * @param monster
	 * @return true if event is handled
	 */
	public boolean onMonsterEnter(Monster monster) {
		return false;
	}

	/**
	 * Fired when a monster leaves the square
	 * 
	 * @param monster
	 * @return true if event is handled
	 */
	public boolean onMonsterLeave(Monster monster) {
		return false;
	}

	/**
	 * Fired when a monster stands on a square
	 * 
	 * @param monster
	 * @return true if event is handled
	 */
	public boolean onMonsterStand(Monster monster) {
		return false;
	}

	/**
	 * Fired when an item is added to the square
	 * 
	 * @param item
	 * @return true if event is handled
	 */
	public boolean onItemDropped(Item item) {
		return false;
	}

	/**
	 * Fired when an item is removed from the square
	 * 
	 * @param item
	 * @return true if event is handled
	 */
	public boolean onItemCollected(Item item) {
		return false;
	}

	// ACTIONS
	public void enable() {
		enabled = true;
	}

	public void disable() {
		enabled = false;
	}

	public void activate() {
		activated = true;
	}

	public void deactivate() {
		activated = false;
	}

	public void toggle() {
		if (activated)
			deactivate();
		else
			activate();
	}

	public void exchange() {

	}

	public void setTo() {

	}

	public void playSound() {

	}

	public void stopSound() {

	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean load(XmlReader.Element xml) {
		if (xml == null)
			return false;

		if (StringUtils.equals("isactivated", xml.getName())) {
			activated = Boolean.parseBoolean(xml.getText());// TODO: check if it works
		} else if (StringUtils.equals("isenabled", xml.getName())) {
			enabled = Boolean.parseBoolean(xml.getText());// TODO: check if it works
		} else {
			Log.debug("[SquareActor] Load() : Unknown node \"" + xml.getName() + "\" found @ "
					+ square.getLocation().toString() + ".");
		}

		return true;
	}

	public boolean save(XmlWriter writer) throws IOException {
		if (writer == null)
			return false;

		writer.element("isactivated", String.valueOf(activated));
		writer.element("isenabled", String.valueOf(enabled));
		//TODO: does it need any pop here?

		return true;
	}

	public Square getSquare() {
		return square;
	}

	public void setSquare(Square square) {
		this.square = square;
	}

	public boolean isCanPassThrough() {
		return canPassThrough;
	}

	public void setCanPassThrough(boolean canPassThrough) {
		this.canPassThrough = canPassThrough;
	}

	public boolean isBlocking() {
		return blocking;
	}

	public void setBlocking(boolean isBlocking) {
		this.blocking = isBlocking;
	}

	public boolean isAcceptItems() {
		return acceptItems;
	}

	public void setAcceptItems(boolean acceptItems) {
		this.acceptItems = acceptItems;
	}

	public boolean isActivated() {
		return activated;
	}

	public void setActivated(boolean activated) {
		this.activated = activated;
	}
}
