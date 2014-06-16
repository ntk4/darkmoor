package com.ntk.darkmoor.resource;

import com.badlogic.gdx.math.Rectangle;
import com.ntk.darkmoor.DarkmoorGame;

public class ResizableRectangle extends Rectangle {

	public ResizableRectangle() {
		super();
	}

	public ResizableRectangle(float x, float y, float width, float height) {
		super(x*2, y*2, width*2, height*2);
	}

	public ResizableRectangle(Rectangle rect) {
		super(rect.x*2, rect.y*2, rect.width*2, rect.height*2);
	}
	
	public float getXFactor() {
		return DarkmoorGame.DISPLAY_WIDTH / DarkmoorGame.GAME_WIDTH;
	}
	
	public float getYFactor() {
		return DarkmoorGame.DISPLAY_HEIGHT / DarkmoorGame.GAME_HEIGHT;
	}

}
