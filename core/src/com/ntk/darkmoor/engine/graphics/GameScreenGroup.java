package com.ntk.darkmoor.engine.graphics;

import java.util.Date;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Disposable;
import com.ntk.darkmoor.engine.Team;

public abstract class GameScreenGroup extends Group  implements Disposable {

	protected static final int HERO_WINDOW_X = 736;
	protected static final int HERO_WINDOW_HEIGHT = 334;
	protected static final int HERO_WINDOW_WIDTH = 288;
	public Team team;
	public Skin uiSkin;
	protected Date now;

	public GameScreenGroup() {
		super();
	}

	/**
	 * Initializes the structures needed for the hero windows. Has to be called when initializing the game screen and
	 * not on every frame. On every frame call updateHeroes()
	 * 
	 */
	public abstract void initialize();
	/**
	 * Updates the hero information/images on screen. This method is intended to be called on every frame. It presumes
	 * that all the structures have already been initialized with a call to initializeHeroRectangles, otherwise will
	 * throw exception.
	 */
	public abstract void update(); 
	
	
	@Override
	public void dispose() {
	}

}