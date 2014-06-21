package com.ntk.darkmoor.engine.actor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.ntk.commons.StringUtils;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.ntk.darkmoor.GameScreen;
import com.ntk.darkmoor.engine.Alcove;
import com.ntk.darkmoor.engine.Compass;
import com.ntk.darkmoor.engine.Compass.CardinalPoint;
import com.ntk.darkmoor.engine.Decoration;
import com.ntk.darkmoor.engine.DisplayCoordinates;
import com.ntk.darkmoor.engine.DungeonLocation;
import com.ntk.darkmoor.engine.Item;
import com.ntk.darkmoor.engine.Square;
import com.ntk.darkmoor.engine.Team;
import com.ntk.darkmoor.engine.ViewField;
import com.ntk.darkmoor.engine.ViewField.ViewFieldPosition;
import com.ntk.darkmoor.stub.MouseButtons;

public class AlcoveActor extends SquareActor {

	public static final String TAG = "alcove";

	private Alcove[] alcoves;

	public AlcoveActor(Square square) {
		super(square);
		setAlcoves(new Alcove[] { new Alcove(), new Alcove(), new Alcove(), new Alcove() });
		setBlocking(true);
	}

	@Override
	public void draw(SpriteBatch batch, ViewField field, ViewFieldPosition position, CardinalPoint direction) {
		if (field.getMaze().geDecoration() == null)
			return;

		// For each wall side, draws the decoration
		for (CardinalPoint side : DisplayCoordinates.getDrawingWallSides()[position.ordinal()]) {
			Alcove alcove = getAlcove(Compass.getDirectionFromView(direction, side));

			// Get the decoration
			Decoration deco = field.getMaze().getDecoration().getDecoration(alcove.getDecoration());
			if (deco == null)
				continue;

			// Draw the decoration
			deco.drawDecoration(batch, field.getMaze().getDecoration(), position, side == CardinalPoint.South);

			// Hide items
			if (alcove.isHideItems() || side != CardinalPoint.South)
				continue;

			// Offset the item locations according to the distance
			Vector2 vect = DisplayCoordinates.getMonsterScaleFactor(position);
			Vector2 loc = deco.prepareLocation(position);
			// TODO: what does offset() do? replace with a valid one for Vector2
			// loc.offset((int) (deco.getItemLocation().x * vect.x), (int) (deco.getItemLocation().y * vect.y));

			// Draw items in the alcove in front of the team
			for (Item item : getSquare().getItemsFromSide(direction, side)) {
				// TODO: draw the tile!
				// batch.drawTile(getSquare().getMaze().getDungeon().getItemTextureSet(), item.getGroundTileID(), loc,
				// DisplayCoordinates.getDistantColor(position), 0.0f,
				// DisplayCoordinates.getMonsterScaleFactor(position), SpriteEffects.NONE, 0.0f);
			}
		}
	}

	private Alcove getAlcove(CardinalPoint side) {
		return alcoves[side.ordinal()];
	}

	@Override
	public DungeonLocation[] getTargets() {
		List<DungeonLocation> list = new ArrayList<DungeonLocation>();

		for (Alcove alcove : alcoves)
			list.addAll(Arrays.asList(alcove.getTargets()));

		return (DungeonLocation[]) list.toArray();
	}

	@Override
	public String toString() {
		return "Alcoves (x)";
	}

	@Override
	public boolean onClick(Vector2 location, CardinalPoint side, MouseButtons button) {
		// No decoration set
		if (getSquare().getMaze().getDecoration() == null)
			return false;

		// No decoration for the alcove
		Alcove alcove = getAlcove(side);
		if (alcove.getDecoration() == -1)
			return false;

		// Point not in decoration
		if (!getSquare().getMaze().getDecoration().isPointInside(alcove.getDecoration(), location))
			return false;

		Team team = GameScreen.getTeam();

		// No item in hand
		if (team.getItemInHand() != null) {
			if (!alcove.isAcceptBigItems() && team.getItemInHand().isBig()) {
				return false;
			}

			// Run scripts
			alcove.addItem();

			getSquare().dropItemFromSide(side, team.getItemInHand());
			team.setItemInHand(null);
		} else {
			// Run scripts
			alcove.removeItem();

			// Set item in hand
			if ((button.ordinal() | MouseButtons.Left.ordinal()) == MouseButtons.Left.ordinal())
				team.setItemInHand(getSquare().collectItemFromSide(side));

			// Add to inventory
			else if ((button.ordinal() | MouseButtons.Right.ordinal()) == MouseButtons.Right.ordinal()
					&& team.getSelectedHero() != null)
				team.getSelectedHero().addToInventory(getSquare().collectItemFromSide(side));
		}

		return true;
	}

	public boolean load(XmlReader.Element node) {
		if (node == null)
			return false;

		for (int i = 0; i < node.getChildCount(); i++) {
			XmlReader.Element xml = node.getChild(i);

			if (StringUtils.equals("side", xml.getName())) {
				CardinalPoint dir = CardinalPoint.valueOf(xml.getAttribute("name"));
				alcoves[dir.ordinal()].load(xml);
//			} else if (StringUtils.equals("isactivated", xml.getName())) {
//				this.setActivated(Boolean.parseBoolean(xml.getText()));
//			} else if (StringUtils.equals("isenabled", xml.getName())) {
//				this.setActivated(Boolean.parseBoolean(xml.getText()));
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

		for (int i = 0; i < alcoves.length; i++) {
			if (alcoves[i].getDecoration() == -1)
				continue;
			writer.element("side").attribute("name", CardinalPoint.valueOf(i).toString());
			alcoves[i].save(writer);
			writer.pop();// TODO: necessary?
		}
		writer.pop();// TODO: necessary?

		return true;
	}

	public Alcove[] getAlcoves() {
		return alcoves;
	}

	public void setAlcoves(Alcove[] alcoves) {
		this.alcoves = alcoves;
	}

}
