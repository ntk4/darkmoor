package com.ntk.darkmoor.engine.actor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.ntk.commons.StringUtils;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.ntk.darkmoor.engine.Compass.CardinalPoint;
import com.ntk.darkmoor.engine.DecorationSet;
import com.ntk.darkmoor.engine.DisplayCoordinates;
import com.ntk.darkmoor.engine.DungeonLocation;
import com.ntk.darkmoor.engine.Item;
import com.ntk.darkmoor.engine.Monster;
import com.ntk.darkmoor.engine.Square;
import com.ntk.darkmoor.engine.ViewField;
import com.ntk.darkmoor.engine.ViewField.ViewFieldPosition;
import com.ntk.darkmoor.engine.graphics.TileDrawing;
import com.ntk.darkmoor.engine.script.PressurePlateScript;
import com.ntk.darkmoor.engine.script.PressurePlateScript.PressurcePlateCondition;

public class PressurePlate extends SquareActor {

	public static final String TAG = "pressureplate";
	
	private ArrayList<PressurePlateScript> scripts;
	private boolean reusable;
	private boolean wasUsed;
	private boolean hidden;
	private int decorationID;

	public PressurePlate(Square block) {
		super(block);

		scripts = new ArrayList<PressurePlateScript>();
		setAcceptItems(true);
		setCanPassThrough(true);
		setBlocking(false);
		reusable = true;
		wasUsed = false;
	}

	@Override
	public void draw(ViewField field, ViewFieldPosition position, CardinalPoint direction) {
		if (getDecoration() == null || hidden)
			return;

		TileDrawing td = DisplayCoordinates.getFloorPlate(position);
		if (td == null)
			return;

		getDecoration().draw(decorationID, position);
	}

	private void runScript(PressurcePlateCondition condition) {
		// Not activated
		if (!isEnabled())
			return;

		// Already used
		if (!reusable && wasUsed)
			return;

		for (PressurePlateScript script : scripts) {
			if ((script.getCondition().getWeight() & condition.getWeight()) == condition.getWeight())
				script.run();
		}

		wasUsed = true;
	}

	@Override
	public DungeonLocation[] getTargets() {
		List<DungeonLocation> list = new ArrayList<DungeonLocation>();

		for (PressurePlateScript script : scripts)
			if (script.getAction() != null && script.getAction().getTarget() != null)
				list.add(script.getAction().getTarget());

		// the array is used only to convert the list type in the toArray method
		return list.toArray(new DungeonLocation[] {});
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("Pressure plate (");

		if (hidden)
			sb.append("Hidden. ");

		sb.append(")");
		return sb.toString();
	}

	public boolean load(XmlReader.Element node) {
		if (node == null)
			return false;

		for (int i = 0; i < node.getChildCount(); i++) {
			XmlReader.Element xml = node.getChild(i);

			if (StringUtils.equals("decoration", xml.getName())) {
				decorationID = Integer.parseInt(xml.getAttribute("activated"));
				Integer.parseInt(xml.getAttribute("deactivated"));

			} else if (StringUtils.equals("reusable", xml.getName())) {
				reusable = Boolean.parseBoolean(xml.getText());

			} else if (StringUtils.equals("ishidden", xml.getName())) {
				hidden = Boolean.parseBoolean(xml.getText());

			} else if (StringUtils.equals("scripts", xml.getName())) {
				for (int j = 0; j < xml.getChildCount(); j++) {
					PressurePlateScript script = new PressurePlateScript();
					script.load(xml.getChild(j));
					scripts.add(script);
				}

			} else if (StringUtils.equals("used", xml.getName())) {
				wasUsed = Boolean.parseBoolean(xml.getText());

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

		writer.element("ishidden", hidden).pop();
		writer.element("reusable", reusable).pop();
		writer.element("used", wasUsed).pop();

		writer.element("decoration").attribute("activated", decorationID).attribute("deactivated", decorationID).pop();

		if (!scripts.isEmpty()) {
			writer.element("scripts");
			for (PressurePlateScript script : scripts) {
				script.save(writer);
			}
			writer.pop();
		}

		writer.pop();

		return true;
	}

	@Override
	public boolean onTeamEnter() {
		runScript(PressurcePlateCondition.OnTeamEnter);

		return false;
	}
	
	@Override
	public boolean onMonsterEnter(Monster monster)
	{
		runScript(PressurcePlateCondition.OnMonsterEnter);
		return false;
	}

	@Override
	public boolean onTeamLeave()
	{
		runScript(PressurcePlateCondition.OnTeamLeave);
		return false;
	}
	
	@Override
	public boolean onMonsterLeave(Monster monster)
	{
		runScript(PressurcePlateCondition.OnMonsterLeave);
		return false;
	}
	
	@Override
	public boolean onItemCollected(Item item)
	{
		runScript(PressurcePlateCondition.OnItemRemoved);
		return false;
	}
	
	@Override
	public boolean onItemDropped(Item item)
	{	
		runScript(PressurcePlateCondition.OnItemAdded);

		return false;
	}
	
	public DecorationSet getDecoration() {
		if (getSquare() == null)
			return null;

		return getSquare().getMaze().getDecoration();
	}

	public ArrayList<PressurePlateScript> getScripts() {
		return scripts;
	}

	public void setScripts(ArrayList<PressurePlateScript> scripts) {
		this.scripts = scripts;
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

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public int getDecorationID() {
		return decorationID;
	}

	public void setDecorationID(int decorationID) {
		this.decorationID = decorationID;
	}
}
