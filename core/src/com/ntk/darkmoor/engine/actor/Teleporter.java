package com.ntk.darkmoor.engine.actor;

import java.io.IOException;

import org.ntk.commons.StringUtils;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.ntk.darkmoor.engine.Compass.CardinalPoint;
import com.ntk.darkmoor.engine.DisplayCoordinates;
import com.ntk.darkmoor.engine.DungeonLocation;
import com.ntk.darkmoor.engine.Monster;
import com.ntk.darkmoor.engine.Square;
import com.ntk.darkmoor.engine.ViewField;
import com.ntk.darkmoor.engine.ViewField.ViewFieldPosition;
import com.ntk.darkmoor.engine.graphics.TileDrawing;
import com.ntk.darkmoor.resource.Resources;
import com.ntk.darkmoor.stub.GameScreenBase;

public class Teleporter extends SquareActor {

	public static final String TAG = "teleporter";

	private String soundName;
	private boolean useSound;
	private boolean reusable;
	private boolean visible;
	private boolean teleportMonsters;
	private boolean teleportTeam;
	private boolean teleportItems;
	private DungeonLocation target;
	private Animation anim;

	public Teleporter(Square block) {
		super(block);

		if (block == null)
			throw new IllegalArgumentException("block");

		setAcceptItems(true);
		setCanPassThrough(true);
		setBlocking(false);

		teleportTeam = true;
		teleportMonsters = true;
		teleportItems = true;
		visible = true;

		anim = Resources.createAsset(Animation.class, "Teleporter");
		// TODO: ntk: uncomment the play() call when mapped to Gdx method
		// anim.play();
	}

	@Override
	public void dispose() {
		// TODO: ntk: find a way to dispose the animation assets, Gdx.Animation is not disposable
		// if (anim != null)
		// anim.dispose();

		anim = null;
	}

	@Override
	public void draw(SpriteBatch batch, ViewField field, ViewFieldPosition position, CardinalPoint direction) {
		if (!visible)
			return;

		TileDrawing td = DisplayCoordinates.getTeleporter(position);
		if (td == null)
			return;

		// TODO: ntk: uncomment call to draw when the best way to draw in Gdx is found
		// anim.draw(batch, td.getLocation(), 0.0f, SpriteEffects.NONE,
		// DisplayCoordinates.getDistantColor(position),
		// DisplayCoordinates.getMonsterScaleFactor(position));
	}

	@Override
	public DungeonLocation[] getTargets() {

		return new DungeonLocation[] { target };
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("Teleporter (target " + target + ")");

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

			} else if (StringUtils.equals("visible", xml.getName())) {
				visible = Boolean.parseBoolean(xml.getText());

			} else if (StringUtils.equals("teleportteam", xml.getName())) {
				teleportTeam = Boolean.parseBoolean(xml.getText());

			} else if (StringUtils.equals("teleportmonsters", xml.getName())) {
				teleportMonsters = Boolean.parseBoolean(xml.getText());

			} else if (StringUtils.equals("teleportitems", xml.getName())) {
				teleportItems = Boolean.parseBoolean(xml.getText());

			} else if (StringUtils.equals("reusable", xml.getName())) {
				reusable = Boolean.parseBoolean(xml.getText());

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

		writer.element("teleportteam", teleportTeam).pop();
		writer.element("teleportmonsters", teleportMonsters).pop();
		writer.element("teleportitems", teleportItems).pop();
		writer.element("visible", visible).pop();
		writer.element("reusable", reusable).pop();

		writer.pop();

		return true;
	}

	@Override
	public boolean onTeamEnter() {
		if (!teleportTeam || target == null || !isActivated())
			return false;

		// One shot ?
		setActivated(reusable);

		return GameScreenBase.getTeam().teleport(target);
	}

	@Override
	public boolean onMonsterEnter(Monster monster) {
		if (!teleportMonsters || monster == null ||target == null)
			return false;

		monster.teleport(target);

		return true;
	}

	public String getSoundName() {
		return soundName;
	}

	public void setSoundName(String soundName) {
		this.soundName = soundName;
	}

	public boolean isUseSound() {
		return useSound;
	}

	public void setUseSound(boolean useSound) {
		this.useSound = useSound;
	}

	public boolean isReusable() {
		return reusable;
	}

	public void setReusable(boolean reusable) {
		this.reusable = reusable;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isTeleportMonsters() {
		return teleportMonsters;
	}

	public void setTeleportMonsters(boolean teleportMonsters) {
		this.teleportMonsters = teleportMonsters;
	}

	public boolean isTeleportTeam() {
		return teleportTeam;
	}

	public void setTeleportTeam(boolean teleportTeam) {
		this.teleportTeam = teleportTeam;
	}

	public boolean isTeleportItems() {
		return teleportItems;
	}

	public void setTeleportItems(boolean teleportItems) {
		this.teleportItems = teleportItems;
	}

	public DungeonLocation getTarget() {
		return target;
	}

	public void setTarget(DungeonLocation target) {
		this.target = target;
	}

	public Animation getAnim() {
		return anim;
	}

	public void setAnim(Animation anim) {
		this.anim = anim;
	}

}
