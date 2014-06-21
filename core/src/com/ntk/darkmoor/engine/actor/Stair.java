package com.ntk.darkmoor.engine.actor;

import java.io.IOException;

import org.ntk.commons.StringUtils;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.ntk.darkmoor.GameScreen;
import com.ntk.darkmoor.engine.Compass.CardinalPoint;
import com.ntk.darkmoor.engine.DungeonLocation;
import com.ntk.darkmoor.engine.Monster;
import com.ntk.darkmoor.engine.Square;
import com.ntk.darkmoor.engine.Square.SquareType;
import com.ntk.darkmoor.engine.Team;
import com.ntk.darkmoor.engine.ViewField;
import com.ntk.darkmoor.engine.ViewField.ViewFieldPosition;
import com.ntk.darkmoor.resource.TextureSet;

public class Stair extends SquareActor {

	public enum StairType {
		/** Going up */
		Up,
		/** Going down */
		Down
	}

	public static final String TAG = "stair";
	private StairType type;
	private DungeonLocation target;

	public Stair(Square block) {
		super(block);
		
		if (block == null)
			throw new IllegalArgumentException("block");

		block.setType(SquareType.Ground);
		setAcceptItems(false);
		setCanPassThrough(false);
		setBlocking(false);
	}

	@Override
	public void draw(SpriteBatch batch, ViewField field, ViewFieldPosition position, CardinalPoint direction) {
		if (getTextureSet() == null)
			return;

		// Upstair or downstair ?
		int delta = (type == StairType.Up ? 0 : 13);

		// TODO: ntk: the following loop should be uncommented when drawTile is mapped to a GDX method
		// for (TileDrawing tmp : DisplayCoordinates.getStairs(position))
		// batch.drawTile(getTextureSet(), tmp.getID()+ delta, tmp.getLocation(), Color.WHITE, 0.0f, tmp.getEffect(), 0.0f);

	}

	@Override
	public DungeonLocation[] getTargets() {
		DungeonLocation[] targets = new DungeonLocation[] { target };

		return targets;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("Stairs going " + type + " (target " + target + ")");

		return sb.toString();
	}

	public boolean load(XmlReader.Element node) {
		if (node == null)
			return false;

		for (int i = 0; i < node.getChildCount(); i++) {
			XmlReader.Element xml = node.getChild(i);

			if (StringUtils.equals("target", xml.getName())) {
				target = new DungeonLocation();
				target.load(xml);

			} else if (StringUtils.equals("type", xml.getName())) {
				type = StairType.valueOf(xml.getAttribute("value"));

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

		writer.element("type").attribute("value", type.toString()).pop();

		if (target != null)
			target.save("target", writer);

		writer.pop();

		return true;
	}

	@Override
	public boolean onTeamEnter() {
		Team team = GameScreen.getTeam();

		if (team.teleport(target))
			team.setDirection(target.getDirection());

		return true;
	}

	@Override
	public boolean onMonsterEnter(Monster monster) {
		if (monster == null)
			return false;

		monster.teleport(target);
		monster.getLocation().setDirection(target.getDirection());

		return true;
	}

	public TextureSet getTextureSet() {
		if (getSquare() == null)
			return null;
		
		return getSquare().getMaze().getWallTileset();
	}

	public StairType getType() {
		return type;
	}

	public void setType(StairType type) {
		this.type = type;
	}

	public DungeonLocation getTarget() {
		return target;
	}

	public void setTarget(DungeonLocation target) {
		this.target = target;
	}
}
