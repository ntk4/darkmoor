package com.ntk.darkmoor.engine;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class DialogBase extends Dialog {

	public DialogBase() {
		super("", new WindowStyle());
	}
	
	public DialogBase(String title, Skin skin) {
		super(title, skin);
	}

	private boolean quit;
	private BitmapFont scriptFont;

	public void exit() {
		quit = true;
	}

	public void update(float delta) {

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
