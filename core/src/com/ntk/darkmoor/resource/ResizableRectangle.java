package com.ntk.darkmoor.resource;

import com.badlogic.gdx.math.Rectangle;
import com.ntk.darkmoor.DarkmoorGame;

public class ResizableRectangle extends Rectangle {

	public ResizableRectangle() {
		super();
	}

	public ResizableRectangle(float x, float y, float width, float height) {
		super(x*2, y*(float)1.8, width*2, height*(float)1.8);
	}

	public ResizableRectangle(Rectangle rect) {
		super(rect.x*2, rect.y*(float)1.8, rect.width*2, rect.height*(float)1.8);
	}
	
	public float getXFactor() {
		return DarkmoorGame.DISPLAY_WIDTH / DarkmoorGame.GAME_WIDTH;
	}
	
	public float getYFactor() {
		return DarkmoorGame.DISPLAY_HEIGHT / DarkmoorGame.GAME_HEIGHT;
	}

}
