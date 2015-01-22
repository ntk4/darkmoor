package com.ntk.darkmoor.engine.graphics;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.ntk.darkmoor.engine.Compass;
import com.ntk.darkmoor.engine.Compass.CompassRotation;
import com.ntk.darkmoor.engine.DisplayCoordinates;
import com.ntk.darkmoor.engine.Team;
import com.ntk.darkmoor.resource.GraphicAssets;

public class NavigationControlsGroup extends GameScreenGroup implements Disposable {

	private static final int BUTTON_HEIGHT = 112;
	private static final int BUTTON_WIDTH = 79;
	private static final int NAVIGATION_AREA_TOP = DisplayCoordinates.PLAYABLE_WINDOW_HEIGHT - 418;
	private static final int NAVIGATION_AREA_LEFT = 18;

	private static final int COMPASS_TOP_DIRECTION_X = 457;
	private static final int COMPASS_TOP_DIRECTION_Y = 330;
	private static final int COMPASS_TOP_DIRECTION_WIDTH = 82;
	private static final int COMPASS_TOP_DIRECTION_HEIGHT = 115;

	private static final int COMPASS_LEFT_DIRECTION_X = 309;
	private static final int COMPASS_LEFT_DIRECTION_Y = 206;
	private static final int COMPASS_LEFT_DIRECTION_WIDTH = 85;
	private static final int COMPASS_LEFT_DIRECTION_HEIGHT = 64;

	private static final int COMPASS_RIGHT_DIRECTION_X = 588;
	private static final int COMPASS_RIGHT_DIRECTION_Y = 210;
	private static final int COMPASS_RIGHT_DIRECTION_WIDTH = 85;
	private static final int COMPASS_RIGHT_DIRECTION_HEIGHT = 60;

	/**
	 * The sprites for the directions (N,S,W,E) start at a specific position in the TextureSet.xml. In the default
	 * resources for EOB2 it's simply 5 and there's no reason not to have it as a constant. However in new game content,
	 * in case this engine is used for a new RPG game, this can change. The only convention is that suppose 5 is the "N"
	 * sprite that appears as the forward direction (top of the compass), then 6 and 7 have to be its minimized
	 * counterparts that appear on the left side and on the right side of the compass respectively
	 */
	private static final int DIRECTION_NORTH_SPRITE_IN_TEXTURE_SET_XML = 5;

	public Team team;
	public Skin uiSkin;

	// Scene2d Actors

	/** navigation buttons */
	private Image turnLeft, forward, turnRight, left, back, right;

	/** direction compass images */
	private Image directionTop, directionLeft, directionRight;

	@Override
	public void initialize() {
		initializeNavigation();

		initializeCompass();
	}

	private void initializeCompass() {
		directionTop = new Image();
		directionTop.setBounds(COMPASS_TOP_DIRECTION_X, COMPASS_TOP_DIRECTION_Y, COMPASS_TOP_DIRECTION_WIDTH,
				COMPASS_TOP_DIRECTION_HEIGHT);
		directionTop.setDrawable(new SpriteDrawable(GraphicAssets.getDefault().getTextureSet("interface")
				.getSprite(resolveTopDirectionSprite())));
		addActor(directionTop);

		directionLeft = new Image();
		directionLeft.setBounds(COMPASS_LEFT_DIRECTION_X, COMPASS_LEFT_DIRECTION_Y, COMPASS_LEFT_DIRECTION_WIDTH,
				COMPASS_LEFT_DIRECTION_HEIGHT);
		directionLeft.setDrawable(new SpriteDrawable(GraphicAssets.getDefault().getTextureSet("interface")
				.getSprite(resolveTopDirectionSprite() + 1)));
		addActor(directionLeft);

		directionRight = new Image();
		directionRight.setBounds(COMPASS_RIGHT_DIRECTION_X, COMPASS_RIGHT_DIRECTION_Y, COMPASS_RIGHT_DIRECTION_WIDTH,
				COMPASS_RIGHT_DIRECTION_HEIGHT);
		directionRight.setDrawable(new SpriteDrawable(GraphicAssets.getDefault().getTextureSet("interface")
				.getSprite(resolveTopDirectionSprite() + 2)));
		addActor(directionRight);
	}

	private void initializeNavigation() {
		turnLeft = new Image();
		turnLeft.setBounds(NAVIGATION_AREA_LEFT, NAVIGATION_AREA_TOP, BUTTON_WIDTH, BUTTON_HEIGHT);
		// strafeLeft.setDrawable(new SpriteDrawable(GraphicAssets.getDefault().getTextureSet("heads").getSprite(2)));
		turnLeft.addListener(new InputListener() {

			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				team.getLocation().setDirection(
						Compass.rotate(team.getLocation().getDirection(), CompassRotation.Rotate270));
				return true;
			}

		});
		addActor(turnLeft);

		forward = new Image();
		forward.setBounds(NAVIGATION_AREA_LEFT + BUTTON_WIDTH, NAVIGATION_AREA_TOP, BUTTON_WIDTH, BUTTON_HEIGHT);
		forward.addListener(new InputListener() {

			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				team.walk(0, -1);
				return true;
			}

		});
		addActor(forward);

		turnRight = new Image();
		turnRight.setBounds(NAVIGATION_AREA_LEFT + 2 * BUTTON_WIDTH, NAVIGATION_AREA_TOP, BUTTON_WIDTH, BUTTON_HEIGHT);
		turnRight.addListener(new InputListener() {

			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				team.getLocation().setDirection(
						Compass.rotate(team.getLocation().getDirection(), CompassRotation.Rotate90));
				return true;
			}

		});
		addActor(turnRight);

		left = new Image();
		left.setBounds(NAVIGATION_AREA_LEFT, NAVIGATION_AREA_TOP - BUTTON_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT);
		left.addListener(new InputListener() {

			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				team.walk(-1, 0);
				return true;
			}

		});
		addActor(left);

		back = new Image();
		back.setBounds(NAVIGATION_AREA_LEFT + BUTTON_WIDTH, NAVIGATION_AREA_TOP - BUTTON_HEIGHT, BUTTON_WIDTH,
				BUTTON_HEIGHT);
		back.addListener(new InputListener() {

			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				team.walk(0, 1);
				return true;
			}

		});
		addActor(back);

		right = new Image();
		right.setBounds(NAVIGATION_AREA_LEFT + 2 * BUTTON_WIDTH, NAVIGATION_AREA_TOP - BUTTON_HEIGHT, BUTTON_WIDTH,
				BUTTON_HEIGHT);
		right.addListener(new InputListener() {

			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				team.walk(1, 0);
				return true;
			}

		});
		addActor(right);
	}

	@Override
	public void update() {
		updateCompass();
	}

	/**
	 * updates the compass images
	 * 
	 */
	private void updateCompass() {
		// TODO: Cache all SpriteDrawables, as they're used without change all throughout the game.

		directionTop.setDrawable(new SpriteDrawable(GraphicAssets.getDefault().getTextureSet("interface")
				.getSprite(resolveTopDirectionSprite())));

		directionLeft.setDrawable(new SpriteDrawable(GraphicAssets.getDefault().getTextureSet("interface")
				.getSprite(resolveTopDirectionSprite() + 1))); // +1: read comments of
																// DIRECTION_NORTH_SPRITE_IN_TEXTURE_SET_XML

		directionRight.setDrawable(new SpriteDrawable(GraphicAssets.getDefault().getTextureSet("interface")
				.getSprite(resolveTopDirectionSprite() + 2))); // +2: read comments of
																// DIRECTION_NORTH_SPRITE_IN_TEXTURE_SET_XML

	}

	private int resolveTopDirectionSprite() {
		switch (team.getLocation().getDirection()) {
		case North: // sprites: 5,6,7 in interface.png (see TextureSet.xml, line 1751)
			return DIRECTION_NORTH_SPRITE_IN_TEXTURE_SET_XML;
		case South:
			return DIRECTION_NORTH_SPRITE_IN_TEXTURE_SET_XML + 3;
		case West:
			return DIRECTION_NORTH_SPRITE_IN_TEXTURE_SET_XML + 6;
		case East:
			return DIRECTION_NORTH_SPRITE_IN_TEXTURE_SET_XML + 9;
		default:
			return DIRECTION_NORTH_SPRITE_IN_TEXTURE_SET_XML;
		}
	}

	@Override
	public void dispose() {
	}
}
