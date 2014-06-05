package com.ntk.darkmoor.engine;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ntk.darkmoor.stub.GameTime;

public abstract class DialogBase {

	private boolean quit;
	private BitmapFont scriptFont;

	public void exit() {
		quit = true;
	}

	public void update(GameTime time) {

	}

	public boolean isQuit() {
		return quit;
	}

	public void setQuit(boolean quit) {
		this.quit = quit;
	}

	public BitmapFont getScriptFont() {
		return scriptFont;
	}

	public void setScriptFont(BitmapFont scriptFont) {
		this.scriptFont = scriptFont;
	}

	public void dispose() {
		
	}

	public void draw(SpriteBatch batch, float parentAlpha) {
		
	}
}
