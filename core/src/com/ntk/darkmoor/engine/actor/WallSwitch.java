package com.ntk.darkmoor.engine.actor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.ntk.commons.StringUtils;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.ntk.darkmoor.GameScreen;
import com.ntk.darkmoor.engine.Compass;
import com.ntk.darkmoor.engine.Compass.CardinalPoint;
import com.ntk.darkmoor.engine.Decoration;
import com.ntk.darkmoor.engine.DecorationSet;
import com.ntk.darkmoor.engine.DisplayCoordinates;
import com.ntk.darkmoor.engine.DungeonLocation;
import com.ntk.darkmoor.engine.GameColors;
import com.ntk.darkmoor.engine.GameMessage;
import com.ntk.darkmoor.engine.Square;
import com.ntk.darkmoor.engine.ViewField;
import com.ntk.darkmoor.engine.ViewField.ViewFieldPosition;
import com.ntk.darkmoor.engine.graphics.TileDrawing;
import com.ntk.darkmoor.engine.script.WallSwitchScript;
import com.ntk.darkmoor.stub.MouseButtons;

public class WallSwitch extends SquareActor {

	public static final String TAG = "wallswitch";
	private ArrayList<WallSwitchScript> scripts;
	private boolean reusable;
	private boolean wasUsed;
	private int activatedDecoration;
	private int deactivatedDecoration;
	private String neededItem;
	private boolean consumeItem;
	private CardinalPoint side;
	private int lockLevel;

	public WallSwitch(Square block) {
		super(block);

		activatedDecoration = -1;
		deactivatedDecoration = -1;
		scripts = new ArrayList<WallSwitchScript>();
		reusable = true;
		wasUsed = false;
	}

	@Override
	public void dispose() {
		for (WallSwitchScript script : scripts)
			script.dispose();
		scripts = null;

		super.dispose();
	}

	@Override
	public boolean onClick(Vector2 location, CardinalPoint side, MouseButtons button) {
		// No wall side or no decoration
		if (side != this.side || getSquare().getMaze().getDecoration() == null)
			return false;

		// Not in the decoration zone
		if (!getSquare().getMaze().getDecoration()
				.isPointInside(isActivated() ? activatedDecoration : deactivatedDecoration, location))
			return false;

		// Switch already used and not reusable
		if ((wasUsed && !reusable) || !isEnabled()) {
			GameMessage.addMessage("It's already unlocked.", GameColors.Red);
			return true;
		}

		// Does an item is required ?
		if (!StringUtils.isEmpty(neededItem)) {

			// No item in hand or not the good item
			if (GameScreen.getTeam().getItemInHand() == null
					|| !StringUtils.equals(GameScreen.getTeam().getItemInHand().getName(), neededItem)) {
				GameMessage.addMessage("You need a key to open this lock");
				return true;
			}

			// Picklock
			if ("PickLock".equalsIgnoreCase(GameScreen.getTeam().getItemInHand().getName())) {
				// TODO: already unlocked => "It's already unlocked"
				if (pickLock()) {
					GameMessage.addMessage("You pick the lock.", GameColors.Green);
				} else {
					GameMessage.addMessage("You failed to pick the lock", GameColors.Yellow);
				}

				return true;
			}

			// Consume item
			if (consumeItem)
				GameScreen.getTeam().setItemInHand(null);
		}

		toggle();

		return true;
	}

	void run() {

		// Execute each scripts
		for (WallSwitchScript script : scripts)
			script.run();
	}

	@Override
	public void activate() {
		super.activate();
		run();
		wasUsed = true;
	}

	@Override
	public void deactivate() {
		super.deactivate();
		run();
		wasUsed = true;
	}

	private boolean pickLock() {
		// TODO: ntk: was false in the original. Not implemented??
		return false;
	}

	@Override
	public Image draw(ViewField field, ViewFieldPosition position, CardinalPoint direction) {
		// Foreach wall side
		for (TileDrawing td : DisplayCoordinates.getWalls(position)) {
			// Not the good side
			if (Compass.getDirectionFromView(direction, td.side) != this.side)
				continue;

			DecorationSet decoset = field.getMaze().getDecoration();
			if (decoset == null)
				return null;

			Decoration deco = decoset.getDecoration(isActivated() ? activatedDecoration : deactivatedDecoration);
			if (deco == null)
				return null;

			deco.drawDecoration(decoset, position, Compass.isSideFacing(direction, side));
		}
		return null;
	}

	@Override
	public DungeonLocation[] getTargets() {
		List<DungeonLocation> list = new ArrayList<DungeonLocation>();

		for (WallSwitchScript script : scripts)
			if (script.getAction() != null && script.getAction().getTarget() != null)
				list.add(script.getAction().getTarget());

		// the array is used only to convert the list type in the toArray method
		return list.toArray(new DungeonLocation[] {});
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("WallSwitch : Facing " + side.toString() + " - ");

		if (wasUsed)
			sb.append("used - ");

		sb.append(scripts.size() + " script(s) - ");

		if (!StringUtils.isEmpty(neededItem))
			sb.append("Need \"" + neededItem + "\" - ");

		if (reusable)
			sb.append("reusable - ");

		return sb.toString();
	}

	public boolean load(XmlReader.Element node) {
		if (node == null)
			return false;

		for (int i = 0; i < node.getChildCount(); i++) {
			XmlReader.Element xml = node.getChild(i);

			if (StringUtils.equals("decoration", xml.getName())) {
				activatedDecoration = Integer.parseInt(xml.getAttribute("activated"));
				deactivatedDecoration = Integer.parseInt(xml.getAttribute("deactivated"));

			} else if (StringUtils.equals("consumeitem", xml.getName())) {
				consumeItem = Boolean.parseBoolean(xml.getText());

			} else if (StringUtils.equals("reusable", xml.getName())) {
				reusable = Boolean.parseBoolean(xml.getText());

			} else if (StringUtils.equals("side", xml.getName())) {
				side = CardinalPoint.valueOf(xml.getText());

			} else if (StringUtils.equals("activateitem", xml.getName())) {
				neededItem = xml.getText();

			} else if (StringUtils.equals("picklock", xml.getName())) {
				lockLevel = Integer.parseInt(xml.getText());

			} else if (StringUtils.equals("scripts", xml.getName())) {
				for (int j = 0; j < xml.getChildCount(); j++) {
					WallSwitchScript script = new WallSwitchScript();
					script.load(xml.getChild(j));
					scripts.add(script);
				}

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

		writer.element("decoration").attribute("activated", activatedDecoration)
				.attribute("deactivated", deactivatedDecoration).pop();

		writer.element("side", side).pop();
		writer.element("reusable", reusable).pop();
		writer.element("activateitem", neededItem).pop();
		writer.element("consumeitem", consumeItem).pop();
		writer.element("picklock", lockLevel).pop();

		if (!scripts.isEmpty()) {
			writer.element("scripts");
			for (WallSwitchScript script : scripts) {
				script.save(writer);
			}
			writer.pop();
		}

		super.save(writer);

		writer.pop();

		return true;
	}

	public DecorationSet getDecoration() {
		if (getSquare() == null)
			return null;

		return getSquare().getMaze().getDecoration();
	}

	public boolean isReusable() {
		return reusable;
	}

	public void setReusable(boolean reusable) {
		this.reusable = reusable;
	}

	public boolean isWasUsed() {
		return wasUsed;
	}

	public void setWasUsed(boolean wasUsed) {
		this.wasUsed = wasUsed;
	}

	public ArrayList<WallSwitchScript> getScripts() {
		return scripts;
	}

	public void setScripts(ArrayList<WallSwitchScript> scripts) {
		this.scripts = scripts;
	}

	public int getActivatedDecoration() {
		return activatedDecoration;
	}

	public void setActivatedDecoration(int activatedDecoration) {
		this.activatedDecoration = activatedDecoration;
	}

	public int getDeactivatedDecoration() {
		return deactivatedDecoration;
	}

	public void setDeactivatedDecoration(int deactivatedDecoration) {
		this.deactivatedDecoration = deactivatedDecoration;
	}

	public String getNeededItem() {
		return neededItem;
	}

	public void setNeededItem(String neededItem) {
		this.neededItem = neededItem;
	}

	public boolean isConsumeItem() {
		return consumeItem;
	}

	public void setConsumeItem(boolean consumeItem) {
		this.consumeItem = consumeItem;
	}

	public CardinalPoint getSide() {
		return side;
	}

	public void setSide(CardinalPoint side) {
		this.side = side;
	}

	public int getLockLevel() {
		return lockLevel;
	}

	public void setLockLevel(int lockLevel) {
		this.lockLevel = lockLevel;
	}

}
