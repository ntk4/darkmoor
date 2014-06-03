package com.ntk.darkmoor.engine;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.ntk.darkmoor.stub.GameTime;

public abstract class DialogBase {

	private boolean quit;
	private BitmapFont scriptFont;

	public void exit() {
		quit = true;
	}

	public void update(GameTime time) {

	}

	public void draw(Batch batch, float parentAlpha) {

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
}
