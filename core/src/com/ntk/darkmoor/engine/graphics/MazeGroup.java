package com.ntk.darkmoor.engine.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.ntk.darkmoor.DarkmoorGame;
import com.ntk.darkmoor.engine.Compass.CardinalPoint;
import com.ntk.darkmoor.engine.DisplayCoordinates;
import com.ntk.darkmoor.engine.DungeonLocation;
import com.ntk.darkmoor.engine.Maze;
import com.ntk.darkmoor.engine.Square;
import com.ntk.darkmoor.engine.Team;
import com.ntk.darkmoor.engine.ViewField;
import com.ntk.darkmoor.engine.ViewField.ViewFieldPosition;
import com.ntk.darkmoor.engine.actor.SquareActor;
import com.ntk.darkmoor.resource.GraphicAssets;

public class MazeGroup extends GameScreenGroup implements Disposable {

	private static final int MAX_SPRITES = 128;
	public Team team;
	public Skin uiSkin;

	// Scene2d Actors
	private Image[] sprites;

	// temporary objects
	private int imageIndex;

	@Override
	public void initialize() {
		sprites = new Image[MAX_SPRITES];
		for (int i = 0; i < MAX_SPRITES; i++) {
			sprites[i] = new Image();
			addActor(sprites[i]);
		}

		// set background, floor and ceiling
		sprites[imageIndex].setPosition(DisplayCoordinates.TOP_LEFT_X, DisplayCoordinates.TOP_LEFT_Y);
		sprites[imageIndex].setHeight(DisplayCoordinates.PLAYABLE_WINDOW_HEIGHT);
		sprites[imageIndex++].setWidth(DisplayCoordinates.PLAYABLE_WINDOW_WIDTH);
	}

	@Override
	public void update() {
		DungeonLocation location = team.getLocation();
		imageIndex = 1;

		ViewField pov = new ViewField(team.getMaze(), location);

		// The background is assumed to be x-flipped when party.x & party.y & party.direction = 1.
		// I.e. all kind of moves and rotations from the current position will result in the background being x-flipped.
		boolean flipbackdrop = ((int) (location.getCoordinates().x + location.getCoordinates().y + location
				.getDirection().value()) & 1) == 0;
		SpriteEffects effect = flipbackdrop ? SpriteEffects.FLIP_HORIZONTALLY : SpriteEffects.NONE;

		sprites[0].setDrawable(new SpriteDrawable(GraphicAssets.getDefault()
				.getTextureSet(team.getMaze().getWallTilesetName())
				.getSprite(0, effect == SpriteEffects.FLIP_HORIZONTALLY)));

		//@formatter:off
		// maze block draw order
		// A E B D C
		// F J G I H
		//   K M L
		//   N ^ O
		//@formatter:on

		// row 3
		updateSquare(pov, ViewFieldPosition.A, location.getDirection());
		updateSquare(pov, ViewFieldPosition.E, location.getDirection());
		updateSquare(pov, ViewFieldPosition.B, location.getDirection());
		updateSquare(pov, ViewFieldPosition.D, location.getDirection());
		updateSquare(pov, ViewFieldPosition.C, location.getDirection());

		// row 2
		updateSquare(pov, ViewFieldPosition.F, location.getDirection());
		updateSquare(pov, ViewFieldPosition.J, location.getDirection());
		updateSquare(pov, ViewFieldPosition.G, location.getDirection());
		updateSquare(pov, ViewFieldPosition.I, location.getDirection());
		updateSquare(pov, ViewFieldPosition.H, location.getDirection());

		// row 1
		updateSquare(pov, ViewFieldPosition.K, location.getDirection());
		updateSquare(pov, ViewFieldPosition.M, location.getDirection());
		updateSquare(pov, ViewFieldPosition.L, location.getDirection());

		// row 0
		updateSquare(pov, ViewFieldPosition.N, location.getDirection());
		updateSquare(pov, ViewFieldPosition.Team, location.getDirection());
		updateSquare(pov, ViewFieldPosition.O, location.getDirection());

		for (int i = imageIndex; i < MAX_SPRITES; i++) {
			sprites[i].setDrawable(null);
		}
	}

	private void updateSquare(ViewField field, ViewFieldPosition position, CardinalPoint view) {

		if (field == null)
			return;

		Maze maze = field.getMaze();
		Square square = field.getBlock(position);
		if (square == null)
			return;

		// Vector2 point;
		// Decoration deco = null;
		// List<List<Item>> list = square.getItems(view);

		// Walls
		if (square.isWall()) 
			drawWallSquare(position, maze);
		
		// Actor
		if (square.getActor() != null) 
			drawActor(square.getActor(), field, position, view);
//			square.getActor().draw(batch, field, position, view);
	}

	private void drawActor(SquareActor actor, ViewField field, ViewFieldPosition position, CardinalPoint view) {

	}

	private void drawWallSquare(ViewFieldPosition position, Maze maze) {
		// Walls
		
		// TileDrawing[] drawings = new TileDrawing[] {
		// new TileDrawing(1, new Vector2(96, DisplayCoordinates.TOP_LEFT_Y+105),
		// CardinalPoint.South, SpriteEffects.NONE,2.06f, 3.24f),
		//
		// new TileDrawing(4, new Vector2(0, DisplayCoordinates.TOP_LEFT_Y), CardinalPoint.East, SpriteEffects.NONE,
		// 2.2f, 3.23f)
		//
		// };

		for (TileDrawing tmp : DisplayCoordinates.getWalls(position)) {// drawings) {//
			// tmp.init();
			SpriteDrawable sprite = new SpriteDrawable(GraphicAssets.getDefault()
					.getTextureSet(maze.getWallTilesetName())
					.getSprite(tmp.getID(), getFlipHorizontally(tmp, position)));
			sprites[imageIndex].setDrawable(sprite);
			sprites[imageIndex].setWidth((int) (sprite.getSprite().getRegionWidth() * tmp.getXScale()));
			sprites[imageIndex].setHeight((int) (sprite.getSprite().getRegionHeight() * tmp.getYScale()));
			// sprites[imageIndex].setWidth((int) tmp.getWidth());
			// sprites[imageIndex].setHeight((int) tmp.getHeight());
			sprites[imageIndex].setPosition(tmp.getLocation().x, tmp.getLocation().y);
			sprites[imageIndex++].setVisible(true);
		}
	}

	@Override
	public void drawChildren(Batch batch, float parentAlpha) {
		Rectangle scissors = new Rectangle();
		Rectangle clipBounds = new Rectangle(-1, -10, 1.111f, 90);
		ScissorStack.calculateScissors(DarkmoorGame.getInstance().getCamera(), batch.getTransformMatrix(), clipBounds,
				scissors);
		ScissorStack.pushScissors(scissors);
		super.drawChildren(batch, parentAlpha);
		ScissorStack.popScissors();
	}

	private boolean getFlipHorizontally(TileDrawing tmp, ViewFieldPosition position) {
		return tmp.getEffect() == SpriteEffects.FLIP_HORIZONTALLY;
	}

	@Override
	public void dispose() {
	}
}
