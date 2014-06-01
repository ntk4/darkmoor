package com.ntk.darkmoor.engine.actor;

import java.io.IOException;

import org.ntk.commons.StringUtils;

import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.ntk.darkmoor.engine.Compass;
import com.ntk.darkmoor.engine.Compass.CardinalPoint;
import com.ntk.darkmoor.engine.Compass.CompassRotation;
import com.ntk.darkmoor.stub.GameScreen;
import com.ntk.darkmoor.stub.Monster;
import com.ntk.darkmoor.stub.Square;
import com.ntk.darkmoor.stub.Team;

public class ForceField extends SquareActor {

	public enum ForceFieldType {
		/** Turn */
		Spin,
		/** Move to a direction */
		Move,
		/** Can't go through */
		Block,
		/** Face to a given direction */
		FaceTo,
	}

	private static final String TAG = "forcefield";
	private ForceFieldType type;
	private CompassRotation spin;
	private CardinalPoint direction;
	private boolean affectTeam;
	private boolean affectMonsters;
	private boolean affectItems;
	private boolean acceptItems;

	public ForceField(Square square) {
		super(square);

		type = ForceFieldType.Spin;
		spin = CompassRotation.ROTATE_180;
		direction = CardinalPoint.NORTH;

		affectTeam = true;
		affectMonsters = true;
		affectItems = true;

		acceptItems = true;
		setCanPassThrough(true);
		setBlocking(false);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("ForceField (");

		switch (type) {
		case Spin:
			sb.append("Spin " + spin);
			break;
		case Move:
			sb.append("Move " + direction);
			break;
		case Block:
			sb.append("Block");
			break;
		case FaceTo:
			sb.append("Face To " + direction);
			break;
		}

		sb.append(". Affect :");
		if (affectTeam)
			sb.append("Team ");
		if (affectMonsters)
			sb.append("monsters ");
		if (affectItems)
			sb.append("items ");

		sb.append(")");
		return sb.toString();
	}

	public boolean load(XmlReader.Element node) {
		if (node == null)
			return false;

		for (int i = 0; i < node.getChildCount(); i++) {
			XmlReader.Element xml = node.getChild(i);

			if (StringUtils.equals("type", xml.getName())) {
				type = ForceFieldType.valueOf(xml.getAttribute("value"));

			} else if (StringUtils.equals("spin", xml.getName())) {
				spin = CompassRotation.valueOf(xml.getAttribute("value"));

			} else if (StringUtils.equals("direction", xml.getName())) {
				direction = CardinalPoint.valueOf(xml.getAttribute("value"));

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
		writer.element("spin").attribute("value", spin.toString()).pop();
		writer.element("direction").attribute("value", direction.toString()).pop();

		writer.pop();

		return true;
	}

	@Override
	public boolean onTeamEnter() {
		if (!affectTeam)
			return false;

		Team team = GameScreen.getTeam();

		switch (type) {
		case Spin:
			team.getLocation().setDirection(Compass.rotate(team.getLocation().getDirection(), spin));

			break;

		case Move:
			team.offset(direction, 1);
			break;

		case FaceTo:
			team.getLocation().setDirection(direction);
			break;
		default:
		}

		return true;

	}

	@Override
	public boolean onMonsterEnter(Monster monster) {
		if (!affectMonsters || monster == null)
			return false;

		switch (type) {
		case Spin:

			monster.getLocation().setDirection(Compass.rotate(monster.getLocation().getDirection(), spin));

			break;

		case Move:

			switch (direction) {
			case NORTH:
				monster.getLocation().getCoordinates().add(0, -1);
				break;
			case SOUTH:
				monster.getLocation().getCoordinates().add(0, 1);
				break;
			case WEST:
				monster.getLocation().getCoordinates().add(-1, 0);
				break;
			case EAST:
				monster.getLocation().getCoordinates().add(1, 0);
				break;
			}
			break;

		case FaceTo:
			monster.getLocation().setDirection(direction);
			break;
		default:
		}

		return true;
	}

	public ForceFieldType getType() {
		return type;
	}

	public void setType(ForceFieldType type) {
		this.type = type;
	}

	public CompassRotation getSpin() {
		return spin;
	}

	public void setSpin(CompassRotation spin) {
		this.spin = spin;
	}

	public CardinalPoint getDirection() {
		return direction;
	}

	public void setDirection(CardinalPoint direction) {
		this.direction = direction;
	}

	public boolean isAffectTeam() {
		return affectTeam;
	}

	public void setAffectTeam(boolean affectTeam) {
		this.affectTeam = affectTeam;
	}

	public boolean isAffectMonsters() {
		return affectMonsters;
	}

	public void setAffectMonsters(boolean affectMonsters) {
		this.affectMonsters = affectMonsters;
	}

	public boolean isAffectItems() {
		return affectItems;
	}

	public void setAffectItems(boolean affectItems) {
		this.affectItems = affectItems;
	}

	public boolean isAcceptItems() {
		return acceptItems;
	}

	public void setAcceptItems(boolean acceptItems) {
		this.acceptItems = acceptItems;
	}
}
