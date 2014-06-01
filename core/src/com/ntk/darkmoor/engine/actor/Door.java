package com.ntk.darkmoor.engine.actor;

import java.io.IOException;

import org.ntk.commons.StringUtils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.ntk.darkmoor.engine.Compass.CardinalPoint;
import com.ntk.darkmoor.engine.graphics.TileDrawing;
import com.ntk.darkmoor.resource.Resources;
import com.ntk.darkmoor.stub.AudioSample;
import com.ntk.darkmoor.stub.Display;
import com.ntk.darkmoor.stub.DisplayCoordinates;
import com.ntk.darkmoor.stub.GameMessage;
import com.ntk.darkmoor.stub.GameTime;
import com.ntk.darkmoor.stub.Item;
import com.ntk.darkmoor.stub.MouseButtons;
import com.ntk.darkmoor.stub.Square;
import com.ntk.darkmoor.stub.SwitchCount;
import com.ntk.darkmoor.stub.TileSet;
import com.ntk.darkmoor.stub.ViewField;
import com.ntk.darkmoor.stub.ViewFieldPosition;

public class Door extends SquareActor {

	public enum DoorType {
		Grid, Iron, Monster, Azure, Crimson, Temple, Forest, Silver, Mantis;
	}

	public enum DoorState {
		Closed, Closing, Opening, Opened, Broken, Stuck;
	}

	private static final String TAG = "door";

	private Rectangle button;
	private SwitchCount count;
	private AudioSample openSound;
	private AudioSample closeSound;
	private boolean acceptItems;
	private boolean breakable;
	private int speed; // in milliseconds, instead of TimeSpan in the original
	private DoorType type;
	private DoorState state;
	private int VPosition;
	private boolean hasButton;
	private boolean smallItemPassThrough;
	private boolean pickLock;
	private int strength;

	public Door(Square square) {
		super(square);

		// zone of the button to open/close the door
		button = new Rectangle(252, 90, 20, 28);
		count = new SwitchCount();

		// Sounds
		openSound = Resources.lockSharedAsset(AudioSample.class, "door open");
		closeSound = Resources.lockSharedAsset(AudioSample.class, "door close");

		acceptItems = false;
		speed = 1000;

		if (getSquare() != null || getSquare().getMaze() != null)
			type = getSquare().getMaze().getDefaultDoorType();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(state + " " + type + " door ");
		if (breakable)
			sb.append("(breakable) ");
		sb.append("(key ....) ");
		return sb.toString();
	}

	@Override
	public void draw(SpriteBatch batch, ViewField field, ViewFieldPosition position, CardinalPoint view) {
		if (getTileSet() == null)
			return;

		TileDrawing td = null;
		TileSet wall = getSquare().getMaze().getWallTileset();

		// TODO: Under the door, draw sides
		if (field.getBlock(ViewFieldPosition.L).isWall() && position == ViewFieldPosition.Team) {
			// td = DisplayCoordinates.GetDoor(ViewFieldPosition.Team);
			// if (td != null)
			// batch.DrawTile(overlay, td.ID, td.Location, Color.White, 0.0f, td.Effect, 0.0f);
			if (field.getMaze().getDecoration() != null) {
				field.getMaze().getDecoration().draw(batch, field.getMaze().getDoorDeco(), position);
			}
		}

		// Draw the door
		else if (((field.getMaze().isDoorNorthSouth(getSquare().getLocation()) && (view == CardinalPoint.North || view == CardinalPoint.South)) || (!field
				.getMaze().isDoorNorthSouth(getSquare().getLocation()) && (view == CardinalPoint.East || view == CardinalPoint.West)))
				&& position != ViewFieldPosition.Team) {
			td = DisplayCoordinates.getDoor(position);
			if (td != null) {
				// TODO: uncomment the next line (ONLY 1 line) after finding what drawTile corresponds to, in GDX
				// batch.drawTile(wall, td.getID(), td.getLocation(), Color.WHITE, 0.0f, td.getEffect(), 0.0f);

				// block.Door.Draw(batch, td.Location, position, view);

				switch (type) {
				case Grid:
					drawSimpleDoor(batch, 1, td.getLocation(), position);
					break;
				case Forest:
					drawSimpleDoor(batch, 6, td.getLocation(), position);
					break;
				case Iron:
					drawSimpleDoor(batch, 0, td.getLocation(), position);
					break;
				case Monster:
					drawSimpleDoor(batch, 2, td.getLocation(), position);
					break;
				case Azure:
					drawSimpleDoor(batch, 8, td.getLocation(), position);
					break;
				case Crimson:
					drawSimpleDoor(batch, 9, td.getLocation(), position);
					break;
				case Temple:
					drawSimpleDoor(batch, 10, td.getLocation(), position);
					break;
				case Silver:
					drawSimpleDoor(batch, 11, td.getLocation(), position);
					break;
				case Mantis:
					drawSimpleDoor(batch, 12, td.getLocation(), position);
					break;
				}
			}
		}

	}

	@Override
	public void update(GameTime time) {
		// Opening
		if (state == DoorState.Opening) {
			VPosition--;

			if (VPosition <= -30) {
				state = DoorState.Opened;
			}
		}

		// Closing
		else if (state == DoorState.Closing) {
			VPosition++;

			if (VPosition >= 0) {
				state = DoorState.Closed;
			}
		}

	}

	@Override
	public boolean onClick(Vector2 location, CardinalPoint side, MouseButtons button) {

		// Button
		if (hasButton && this.button.contains(location)) {
			if (state == DoorState.Closed || state == DoorState.Closing)
				open();
			else if (state == DoorState.Opened || state == DoorState.Opening)
				close();

			return true;
		} else {
			// Try to force the door
			if (state != DoorState.Opened) {
				GameMessage.addMessage("No one is able to pry this door open.");
				return true;
			}
		}

		return false;
	}

	private void open() {
		state = DoorState.Opening;
	}

	private void close() {
		state = DoorState.Closing;
	}

	public boolean canItemsPassThrough(Item item) {
		if (item == null)
			return false;

		if (state == DoorState.Opened)
			return true;

		if (!item.isBig() && smallItemPassThrough)
			return true;

		return false;
	}

	/**
	 * 
	 * @param batch
	 * @param tileset
	 * @param id
	 * @param location
	 * @param scissor
	 * @param scale
	 * @param color
	 *            normally Gdx Color. Changed to int because it's helpful for the next method
	 */
	void internalDraw(SpriteBatch batch, TileSet tileset, int id, Vector2 location, Rectangle scissor, Vector2 scale,
			int color) {
		if (batch == null)
			return;

		// ntk: why end here??
		batch.end();

		// ntk: what does the PushScissor method do?
		Display.pushScissor(scissor);

		batch.begin();
		// TODO: uncomment the next line (ONLY 1 line) after finding what drawTile corresponds to, in GDX
		// batch.drawTile(tileset, id, location, color, 0.0f, scale, SpriteEffects.NONE, 0.0f);
		batch.end();

		Display.popScissor();

		batch.begin();
	}

	private void drawSimpleDoor(SpriteBatch batch, int tileid, Vector2 location, ViewFieldPosition distance) {
		Vector2 scale = new Vector2();
		int color = Color.WHITE.toIntBits(); // ntk: not sure handling color as int is ok here
		Vector2 button = new Vector2();

		switch (distance) {
		case K:
		case L:
		case M: {
			location.add(56, 16); // TODO: it was Offset
			scale = new Vector2(1, 1); // TODO: check if this corresponds to Vector2.One
			button = new Vector2(252, 90);
		}
			break;

		case F:
		case G:
		case H:
		case I:
		case J: {
			location.add(32, 10); // TODO: it was Offset
			scale = new Vector2(0.66f, 0.66f);
			color = Color.rgb888(130, 130, 130);
			button = new Vector2(230, 86);
		}
			break;

		case A:
		case B:
		case C:
		case D:
		case E: {
			location.add(12, 6);
			scale = new Vector2(0.50f, 0.50f);
			color = Color.rgb888(40f, 40f, 40f);
			button = new Vector2(210, 84);
		}
			break;

		}

		internalDraw(batch, getTileSet(), tileid, new Vector2(location.x, location.y + VPosition * 5), new Rectangle(
				location.x, location.y, 144, 150), scale, color);

		// TODO: uncomment the next 2 lines after finding what drawTile corresponds to, in GDX
		// if (hasButton)
		// batch.drawTile(getTileSet(), 15, button, color, 0.0f, scale, SpriteEffects.NONE, 0.0f);

	}

	void DrawUpDownDoor(SpriteBatch batch, int tileid, Vector2 location, ViewFieldPosition distance) {
		Vector2 scale = new Vector2();
		int color = Color.WHITE.toIntBits();
		Rectangle clip = new Rectangle();
		int[] offset = new int[2];
		Vector2 button = new Vector2();

		switch (distance) {
		case K:
		case L:
		case M: {
			location.add(56, 14);
			scale = new Vector2(1, 1);
			clip = new Rectangle(location.x, location.y, 144, 142);
			offset[0] = VPosition * 5;
			offset[1] = 86 + VPosition * -2;
			button = new Vector2(252, 90);
		}
			break;

		case F:
		case G:
		case H:
		case I:
		case J: {
			location.add(28, 8);
			scale = new Vector2(0.66f, 0.66f);
			color = Color.rgb888(130, 130, 130);
			clip = new Rectangle(location.x, location.y, 104, 96);
			offset[0] = VPosition * 3;
			offset[1] = 56 - VPosition;
			button = new Vector2(230, 86);
		}
			break;

		case A:
		case B:
		case C:
		case D:
		case E: {
			location.add(14, 4);
			scale = new Vector2(0.5f, 0.5f);
			color = Color.rgb888(40, 40, 40);
			clip = new Rectangle(location.x, location.y, 68, 60);
			offset[0] = VPosition * 2;
			offset[1] = 36 - VPosition;
			button = new Vector2(210, 84);
		}
			break;

		}

		// Upper part
		internalDraw(batch, getTileSet(), tileid, new Vector2(location.x, location.y + offset[0]), clip, scale, color);

		// Lower part
		internalDraw(batch, getTileSet(), tileid + 1, new Vector2(location.x, location.y + offset[1]), clip, scale,
				color);

		// TODO: uncomment the next 2 lines after finding what drawTile corresponds to, in GDX
		// Button
		// if (hasButton)
		// batch.DrawTile(getTileSet(), 13, button, color, 0.0f, scale, SpriteEffects.NONE, 0.0f);

	}

	public boolean load(XmlReader.Element node) {
		if (node == null)
			return false;

		for (int i = 0; i < node.getChildCount(); i++) {
			XmlReader.Element xml = node.getChild(i);

			if (StringUtils.equals("smallitempassthrough", xml.getName())) {
				smallItemPassThrough = Boolean.parseBoolean(xml.getText());
			} else if (StringUtils.equals("type", xml.getName())) {
				type = DoorType.valueOf(xml.getAttribute("value"));
			} else if (StringUtils.equals("state", xml.getName())) {
				state = DoorState.valueOf(xml.getAttribute("value"));
				if (state == DoorState.Opened)
					VPosition = -30;
			} else if (StringUtils.equals("isbreakable", xml.getName())) {
				breakable = Boolean.parseBoolean(xml.getAttribute("value"));
			} else if (StringUtils.equals("hasbutton", xml.getName())) {
				hasButton = Boolean.parseBoolean(xml.getText());
			} else if (StringUtils.equals("picklock", xml.getName())) {
				pickLock = Boolean.parseBoolean(xml.getAttribute("value"));
			} else if (StringUtils.equals("speed", xml.getName())) {// TODO: check! the xml has seconds
				speed = Integer.parseInt(xml.getAttribute("value")) * 1000;
			} else if (StringUtils.equals("strength", xml.getName())) {// TODO: check! the xml has seconds
				strength = Integer.parseInt(xml.getAttribute("value"));
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

		writer.element("type").attribute("value", type.toString()).pop();
		writer.element("state").attribute("value", state.toString()).pop();
		writer.element("isbreakable").attribute("value", breakable).pop();
		writer.element("hasbutton").attribute("value", hasButton).pop();
		writer.element("picklock").attribute("value", pickLock).pop();
		writer.element("speed").attribute("value", speed).pop();
		writer.element("strength").attribute("value", strength).pop();
		writer.element("smallitempassthrough", smallItemPassThrough);

		super.save(writer);

		writer.pop();

		return true;
	}

	public boolean isDrawable(CardinalPoint point) {
		return true;
	}

	private TileSet getTileSet() {
		if (getSquare() == null || getSquare().getMaze() == null)
			return null;

		return getSquare().getMaze().getDoorTileset();
	}

	public Rectangle getButton() {
		return button;
	}

	public void setButton(Rectangle button) {
		this.button = button;
	}

	public SwitchCount getCount() {
		return count;
	}

	public void setCount(SwitchCount count) {
		this.count = count;
	}

	public AudioSample getOpenSound() {
		return openSound;
	}

	public void setOpenSound(AudioSample openSound) {
		this.openSound = openSound;
	}

	public AudioSample getCloseSound() {
		return closeSound;
	}

	public void setCloseSound(AudioSample closeSound) {
		this.closeSound = closeSound;
	}

	public boolean isAcceptItems() {
		return acceptItems;
	}

	public void setAcceptItems(boolean acceptItems) {
		this.acceptItems = acceptItems;
	}

	public boolean isBreakable() {
		return breakable;
	}

	public void setBreakable(boolean breakable) {
		this.breakable = breakable;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public DoorType getType() {
		return type;
	}

	public void setType(DoorType type) {
		this.type = type;
	}

	public DoorState getState() {
		return state;
	}

	public void setState(DoorState state) {
		this.state = state;
	}

	public int getVPosition() {
		return VPosition;
	}

	public void setVPosition(int vPosition) {
		VPosition = vPosition;
	}

	public boolean isHasButton() {
		return hasButton;
	}

	public void setHasButton(boolean hasButton) {
		this.hasButton = hasButton;
	}

	public boolean isSmallItemPassThrough() {
		return smallItemPassThrough;
	}

	public void setSmallItemPassThrough(boolean smallItemPassThrough) {
		this.smallItemPassThrough = smallItemPassThrough;
	}

	public boolean isPickLock() {
		return pickLock;
	}

	public void setPickLock(boolean pickLock) {
		this.pickLock = pickLock;
	}

	public int getStrength() {
		return strength;
	}

	public void setStrength(int strength) {
		this.strength = strength;
	}

}
