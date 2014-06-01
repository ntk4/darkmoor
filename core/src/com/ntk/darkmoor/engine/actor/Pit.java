package com.ntk.darkmoor.engine.actor;

import java.io.IOException;

import org.ntk.commons.StringUtils;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.ntk.darkmoor.engine.Compass.CardinalPoint;
import com.ntk.darkmoor.engine.graphics.TileDrawing;
import com.ntk.darkmoor.stub.DecorationSet;
import com.ntk.darkmoor.stub.Dice;
import com.ntk.darkmoor.stub.DisplayCoordinates;
import com.ntk.darkmoor.stub.DungeonLocation;
import com.ntk.darkmoor.stub.Square;
import com.ntk.darkmoor.stub.ViewField;
import com.ntk.darkmoor.stub.ViewFieldPosition;

public class Pit extends SquareActor {

	private static final String TAG = "pit";
	private Dice damage;
	private boolean hidden;
	private DecorationSet decoration;
	private DungeonLocation target;
	private int difficulty;
	private boolean illusion;
	private boolean monsterTrigger;
	

	public Pit(Square block) {
		super(block);

		if (block == null)
			throw new IllegalArgumentException("block");

		damage = new Dice();
		setAcceptItems(true);
		setCanPassThrough(true);
		setBlocking(false);
	}

	@Override
	public void draw(SpriteBatch batch, ViewField field, ViewFieldPosition position, CardinalPoint direction) {
		if (decoration == null || hidden)
			return;

		TileDrawing td = DisplayCoordinates.getPit(position);
		if (td == null)
			return;

		if (isActivated())
			// batch.FillRectangle(new Rectangle(td.Location, new Size(50, 50)), Color.Red);
			decoration.draw(batch, field.getMaze().getFloorPitDeco(), position);
		// TODO (ntk: commented also in the original code)
		// if (td != null && !IsHidden)
		// batch.DrawTile(TileSet, td.ID, td.Location, Color.White, 0.0f, td.Effect, 0.0f);
	}
	
	@Override
	public DungeonLocation[] getTargets()
	{
		DungeonLocation[] targets = new DungeonLocation[] { this.target };

		return targets;
	}
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();

		sb.append("Pit (");
		if (target != null)
			sb.append(target);

		if (damage != null)
			sb.append(". Difficulty : " + damage);
		
		sb.append(")");

		return sb.toString();
	}
	
	public boolean load(XmlReader.Element node) {
		if (node == null)
			return false;

		for (int i = 0; i < node.getChildCount(); i++) {
			XmlReader.Element xml = node.getChild(i);

			if (StringUtils.equals("target", xml.getName())) {
				target = new DungeonLocation(xml);

			} else if (StringUtils.equals("damage", xml.getName())) {
				damage.load(xml);

			} else if (StringUtils.equals("hidden", xml.getName())) {
				hidden = Boolean.valueOf(xml.getAttribute("value"));

			} else if (StringUtils.equals("difficulty", xml.getName())) {
				difficulty = Integer.parseInt(xml.getAttribute("value"));

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

		if (target != null) 
			target.save("target", writer);
		
		writer.element("hidden").attribute("value", hidden).pop();
		writer.element("difficulty").attribute("value", difficulty).pop();
		
		damage.save("damage", writer);

		writer.pop();

		return true;
	}

	public Dice getDamage() {
		return damage;
	}

	public void setDamage(Dice damage) {
		this.damage = damage;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public DecorationSet getDecoration() {
		return decoration;
	}

	public void setDecoration(DecorationSet decoration) {
		this.decoration = decoration;
	}

	public DungeonLocation getTarget() {
		return target;
	}

	public void setTarget(DungeonLocation target) {
		this.target = target;
	}

	public int getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}

	public boolean isIllusion() {
		return illusion;
	}

	public void setIllusion(boolean illusion) {
		this.illusion = illusion;
	}

	public boolean isMonsterTrigger() {
		return monsterTrigger;
	}

	public void setMonsterTrigger(boolean monsterTrigger) {
		this.monsterTrigger = monsterTrigger;
	}
}
