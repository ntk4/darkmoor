package com.ntk.darkmoor.engine;

import java.io.IOException;

import org.ntk.commons.StringUtils;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.ntk.darkmoor.config.Log;
import com.ntk.darkmoor.engine.ViewField.ViewFieldPosition;

public class Decoration {

	private static final int LOC_INIT_VAL = -999;

	public static final String TAG = "decoration";

	private Vector2[] location;
	private int[] textureIds;
	private int onBashId;
	private int onHackId;
	private int onClickId;
	private boolean[] swap;
	private boolean blocking;
	private Vector2 itemLocation;

	private boolean forceDisplay;

	private boolean hideItems;

	public Decoration() {
		textureIds = new int[16];
		location = new Vector2[] {//@formatter:off
	        new Vector2(LOC_INIT_VAL, LOC_INIT_VAL), new Vector2(LOC_INIT_VAL, LOC_INIT_VAL), 
	        new Vector2(LOC_INIT_VAL, LOC_INIT_VAL), new Vector2(LOC_INIT_VAL, LOC_INIT_VAL), 
	        new Vector2(LOC_INIT_VAL, LOC_INIT_VAL), new Vector2(LOC_INIT_VAL, LOC_INIT_VAL),
			new Vector2(LOC_INIT_VAL, LOC_INIT_VAL), new Vector2(LOC_INIT_VAL, LOC_INIT_VAL), 
			new Vector2(LOC_INIT_VAL, LOC_INIT_VAL), new Vector2(LOC_INIT_VAL, LOC_INIT_VAL), 
			new Vector2(LOC_INIT_VAL, LOC_INIT_VAL), new Vector2(LOC_INIT_VAL, LOC_INIT_VAL),
			new Vector2(LOC_INIT_VAL, LOC_INIT_VAL), new Vector2(LOC_INIT_VAL, LOC_INIT_VAL), 
			new Vector2(LOC_INIT_VAL, LOC_INIT_VAL), new Vector2(LOC_INIT_VAL, LOC_INIT_VAL), 
		};
		//@formatter:on
		onBashId = -1;
		onHackId = -1;
		onClickId = -1;

		for (int i = 0; i < textureIds.length; i++)
			textureIds[i] = -1;
		swap = new boolean[16];
	}

	public int getTextureId(ViewFieldPosition position) {
		return textureIds[position.value()];
	}

	public void setTextureId(ViewFieldPosition position, int id) {
		textureIds[position.value()] = id;
	}

	public Vector2 getLocation(ViewFieldPosition position) {
		return location[position.value()];
	}

	public void setLocation(ViewFieldPosition position, Vector2 newlocation) {
		location[position.value()] = newlocation;
	}

	public boolean getSwap(ViewFieldPosition position) {
		return swap[position.value()];
	}

	public void setSwap(ViewFieldPosition position, boolean newswap) {
		swap[position.value()] = newswap;
	}

	public void Clear() {
		for (ViewFieldPosition pos : ViewFieldPosition.values()) {
			setTextureId(pos, -1);
			setLocation(pos, new Vector2());
			setSwap(pos, false);
		}

		blocking = false;
	}

	public void drawDecoration(SpriteBatch batch, DecorationSet set, ViewFieldPosition position, boolean alignView) {
		if (batch == null || set == null)
			return;

		// Location of the decoration on the screen
		Vector2 location = getLocation(position);

		// Texture id
		int textureid = getTextureId(position);

		// Offset the decoration if facing to the view point
		if (alignView) {
			location = prepareLocation(position);
			textureid = prepareTexture(position);
		}

		// TODO: ntk: uncomment when I find how to use Gdx for this
		// Draws the decoration
		// batch.drawTexture(set.getTextureSet(), textureid, location, Color.WHITE, 0.0f,
		// getSwap(position) ? SpriteEffects.FLIP_HORIZONTALLY : SpriteEffects.NONE, 0.0f);

	}

	public Vector2 prepareLocation(ViewFieldPosition position) {
		Vector2 location = new Vector2();

		switch (position) {
		case A:
			location = getLocation(ViewFieldPosition.C);
			location.x += -96 * 2;
			break;
		case B:
			location = getLocation(ViewFieldPosition.C);
			location.x += -96;
			break;
		case D:
			location = getLocation(ViewFieldPosition.C);
			location.x += 96;
			break;
		case E:
			location = getLocation(ViewFieldPosition.C);
			location.x += 96 * 2;
			break;

		case G:
			location = getLocation(ViewFieldPosition.H);
			location.x += -160;
			break;
		case I:
			location = getLocation(ViewFieldPosition.H);
			location.x += 160;
			break;

		case K:
			location = getLocation(ViewFieldPosition.L);
			location.x -= 256;
			break;
		case M:
			location = getLocation(ViewFieldPosition.L);
			location.x += 256;
			break;

		default:
			location = getLocation(position);
			break;
		}

		return location;
	}

	public int prepareTexture(ViewFieldPosition position) {
		ViewFieldPosition[] pos = new ViewFieldPosition[] {//@formatter:off
			ViewFieldPosition.C,	// A
			ViewFieldPosition.C,	// B
			ViewFieldPosition.C,	// C
			ViewFieldPosition.C,	// D
			ViewFieldPosition.C,	// E

			ViewFieldPosition.H,	// F
			ViewFieldPosition.H,	// G
			ViewFieldPosition.H,	// H
			ViewFieldPosition.H,	// I
			ViewFieldPosition.H,	// J

			ViewFieldPosition.L,	// K
			ViewFieldPosition.L,	// L
			ViewFieldPosition.L,	// M

			ViewFieldPosition.N,	// N
			ViewFieldPosition.Team,	// Team
			ViewFieldPosition.O,	// O
		};//@formatter:on

		return getTextureId(pos[position.value()]);
	}

	public boolean load(XmlReader.Element node) {
		if (node == null || !StringUtils.equals(node.getName(), TAG))
			return false;

		blocking = Boolean.parseBoolean(node.getAttribute("isblocking"));
		forceDisplay = Boolean.parseBoolean(node.getAttribute("forcedisplay"));
		onHackId = Integer.parseInt(node.getAttribute("onhack"));
		onBashId = Integer.parseInt(node.getAttribute("onbash"));
		onClickId = Integer.parseInt(node.getAttribute("onclick"));
		hideItems = Boolean.parseBoolean(node.getAttribute("hideitems"));

		for (int i = 0; i < node.getChildCount(); i++) {
			XmlReader.Element child = node.getChild(i);

			if ("item".equals(child.getName())) {
				itemLocation = new Vector2(Integer.parseInt(child.getAttribute("x")), Integer.parseInt(child
						.getAttribute("y")));
			} else {
				try {
					ViewFieldPosition pos = ViewFieldPosition.valueOf(child.getName());
					textureIds[pos.value()] = Integer.parseInt(child.getAttribute("id"));

					if (textureIds[pos.value()] != -1) {
						location[pos.value()].x = Integer.parseInt(child.getAttribute("x"));
						location[pos.value()].y = Integer.parseInt(child.getAttribute("y"));
						swap[pos.value()] = Boolean.parseBoolean(child.getAttribute("swap"));
					}
				} catch (Exception e) {
					Log.error("[Decoration]load : error while loading : " + e.getMessage());
				}
			}
		}
		return true;
	}

	public boolean save(XmlWriter writer, int id) throws IOException {
		if (writer == null)
			return false;

		writer.element("decoration").attribute("id", id).attribute("isblocking", blocking)
				.attribute("forcedisplay", forceDisplay).attribute("onhack", onHackId).attribute("onbash", onBashId)
				.attribute("onclick", onClickId).attribute("hideitems", hideItems);

		writer.element("item").attribute("x", itemLocation.x).attribute("y", itemLocation.y).pop();

		for (ViewFieldPosition vfp : ViewFieldPosition.values()) {
			writer.element(vfp.toString());
			writer.attribute("x", location[vfp.value()].x);
			writer.attribute("y", location[vfp.value()].y);
			writer.attribute("y", swap[vfp.value()]);
			writer.pop();
		}

		writer.pop();

		return true;
	}

	public Vector2[] getLocation() {
		return location;
	}

	public void setLocation(Vector2[] location) {
		this.location = location;
	}

	public int[] getTextureIds() {
		return textureIds;
	}

	public void setTextureIds(int[] textureIds) {
		this.textureIds = textureIds;
	}

	public int getOnBashId() {
		return onBashId;
	}

	public void setOnBashId(int onBashId) {
		this.onBashId = onBashId;
	}

	public int getOnHackId() {
		return onHackId;
	}

	public void setOnHackId(int onHackId) {
		this.onHackId = onHackId;
	}

	public int getOnClickId() {
		return onClickId;
	}

	public void setOnClickId(int onClickId) {
		this.onClickId = onClickId;
	}

	public boolean[] getSwap() {
		return swap;
	}

	public void setSwap(boolean[] swap) {
		this.swap = swap;
	}

	public boolean isBlocking() {
		return blocking;
	}

	public void setBlocking(boolean blocking) {
		this.blocking = blocking;
	}

	public Vector2 getItemLocation() {
		return itemLocation;
	}

	public void setItemLocation(Vector2 itemLocation) {
		this.itemLocation = itemLocation;
	}

	public boolean isForceDisplay() {
		return forceDisplay;
	}

	public void setForceDisplay(boolean forceDisplay) {
		this.forceDisplay = forceDisplay;
	}

	public boolean isHideItems() {
		return hideItems;
	}

	public void setHideItems(boolean hideItems) {
		this.hideItems = hideItems;
	}
}
