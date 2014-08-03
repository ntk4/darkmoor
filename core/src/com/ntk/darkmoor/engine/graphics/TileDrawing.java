package com.ntk.darkmoor.engine.graphics;

import com.badlogic.gdx.math.Vector2;
import com.ntk.darkmoor.GameScreen;
import com.ntk.darkmoor.engine.Compass.CardinalPoint;
import com.ntk.darkmoor.resource.GraphicAssets;
import com.ntk.darkmoor.resource.TextureMetadata;

public class TileDrawing {
	private int ID;
	private Vector2 location;
	public SpriteEffects effect;
	public CardinalPoint side;
	private float xScale;
	private float yScale;
	private int width;
	private int height;

	public TileDrawing(int id, Vector2 location, CardinalPoint side) {
		this(id, location, side, SpriteEffects.NONE);
	}

	public TileDrawing(int id, Vector2 location, CardinalPoint side, SpriteEffects effect) {
		this.ID = id;
		this.location = location;
		this.effect = effect;
		this.side = side;
	}

	/**
	 * initializes the dimensions of the texture according to TextureSet.xml. However the dimensions there don't see to
	 * help. So this is a method optionally called by the caller, e.g. MazeGroup
	 */
	public void init() {
		TextureMetadata metadata = GraphicAssets.getDefault()
				.getTextureSet(GameScreen.getTeam().getMaze().getWallTilesetName()).getMetadata().get(this.ID);
		if (metadata != null) {
			this.width = (int) metadata.getRectangle().getWidth() << 1;
			this.height = (int) (metadata.getRectangle().getHeight() * 1.8);
		}
	}

	public TileDrawing(int id, Vector2 location, CardinalPoint side, SpriteEffects effect, float xScale, float yScale) {
		this(id, location, side, effect);
		this.xScale = xScale;
		this.yScale = yScale;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public Vector2 getLocation() {
		return location;
	}

	public void setLocation(Vector2 location) {
		this.location = location;
	}

	public SpriteEffects getEffect() {
		return effect;
	}

	public void setEffect(SpriteEffects effect) {
		this.effect = effect;
	}

	public CardinalPoint getSide() {
		return side;
	}

	public void setSide(CardinalPoint side) {
		this.side = side;
	}

	public float getXScale() {
		return xScale;
	}

	public void setXScale(float xScale) {
		this.xScale = xScale;
	}

	public float getYScale() {
		return yScale;
	}

	public void setYScale(float yScale) {
		this.yScale = yScale;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}