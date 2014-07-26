package com.ntk.darkmoor.engine.graphics;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Disposable;
import com.ntk.darkmoor.engine.Compass;
import com.ntk.darkmoor.engine.Compass.CompassRotation;
import com.ntk.darkmoor.engine.DisplayCoordinates;
import com.ntk.darkmoor.engine.Team;

public class NavigationControlsGroup extends GameScreenGroup implements Disposable {

	private static final int BUTTON_HEIGHT = 112;
	private static final int BUTTON_WIDTH = 79;
	private static final int NAVIGATION_AREA_TOP = DisplayCoordinates.PLAYABLE_WINDOW_HEIGHT - 418;
	private static final int NAVIGATION_AREA_LEFT = 18;
	public Team team;
	public Skin uiSkin;

	// Scene2d Actors
	private Image strafeLeft, forward, strafeRight, left, back, right;

	@Override
	public void initialize() {
		strafeLeft = new Image();
		strafeLeft.setBounds(NAVIGATION_AREA_LEFT, NAVIGATION_AREA_TOP, BUTTON_WIDTH, BUTTON_HEIGHT);
		// strafeLeft.setDrawable(new SpriteDrawable(GraphicAssets.getDefault().getTextureSet("heads").getSprite(2)));
		strafeLeft.addListener(new InputListener() {

			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				team.getLocation().setDirection(
						Compass.rotate(team.getLocation().getDirection(), CompassRotation.Rotate270));
				return true;
			}

		});
		addActor(strafeLeft);

		forward = new Image();
		forward.setBounds(NAVIGATION_AREA_LEFT + BUTTON_WIDTH, NAVIGATION_AREA_TOP, BUTTON_WIDTH, BUTTON_HEIGHT);
		forward.addListener(new InputListener() {

			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				team.walk(0, -1);
				return true;
			}

		});
		addActor(forward);

		strafeRight = new Image();
		strafeRight
				.setBounds(NAVIGATION_AREA_LEFT + 2 * BUTTON_WIDTH, NAVIGATION_AREA_TOP, BUTTON_WIDTH, BUTTON_HEIGHT);
		strafeRight.addListener(new InputListener() {

			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				team.getLocation().setDirection(
						Compass.rotate(team.getLocation().getDirection(), CompassRotation.Rotate90));
				return true;
			}

		});
		addActor(strafeRight);

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

	}

	@Override
	public void dispose() {
	}
}
