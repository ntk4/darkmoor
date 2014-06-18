package com.ntk.darkmoor.stub;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.ntk.darkmoor.DarkmoorGame;
import com.ntk.darkmoor.engine.DialogBase;
import com.ntk.darkmoor.engine.Dungeon;
import com.ntk.darkmoor.engine.ScriptedDialog;
import com.ntk.darkmoor.engine.SpellBook;
import com.ntk.darkmoor.engine.Team;

public class GameScreenBase extends ScreenAdapter {

	protected TextButton[] buttons;
	protected DarkmoorGame game;
	protected Stage stage;

	protected Skin uiSkin;
	
	public GameScreenBase(Game game) {
		this.game = (DarkmoorGame)game;
		loadContent();
	}
	

	@Override
	public void render(float delta) {
		super.render(delta);
		update(delta, true, false);
		draw(delta);
		
	}
	
	public static Team getTeam() {
		// TODO Auto-generated method stub
		return null;
	}

	public static Dungeon getDungeon() {
		// TODO Auto-generated method stub
		return null;
	}

	public static DialogBase getDialog() {
		// TODO Auto-generated method stub
		return null;
	}

	public static void setDialog(ScriptedDialog scriptedDialog) {
		// TODO Auto-generated method stub
		
	}

	public void loadGameSlot(int selectedSlot) {
		// TODO Auto-generated method stub
		
	}

	public static SpellBook getSpellBook() {
		// TODO Auto-generated method stub
		return null;
	}

	public void loadContent() {
		// TODO Auto-generated method stub

		uiSkin = new Skin(Gdx.files.internal("data/skin/uiskin.json"));
//		FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal("data/font.ttf"));
//		BitmapFont bfont = gen.generateFont(fontSize);
//		gen.dispose();
//		Skin skin = new Skin();
//		skin.add("default-font", bfont);
		setupButtons(uiSkin);
		setupStage();
	}

	public void unloadContent() {
		for (TextButton button: buttons) {
			button.setTouchable(Touchable.disabled);
		}
		
		if (stage != null)
			stage.dispose();
		
		buttons = null;
	}

	public void update(float delta, boolean hasFocus, boolean isCovered) {
		
	}

	public void draw(float delta) {
		// Clears the background
		Display.clearBuffers();
	}

	public void newGame(Team gsteam) {
		// TODO Auto-generated method stub
		
	}


	public void exitScreen() {
		// TODO Auto-generated method stub
		
	}

	public void onLeave() {
		// TODO Auto-generated method stub
		
	}

	public void onEnter() {
		// TODO Auto-generated method stub
		
	}



	protected void setScreen(GameScreenBase newScreen) {
		this.onLeave();
		this.unloadContent();
		game.setScreen(newScreen);
	}

	protected Image setupBackground() {
		return null;
	}


	protected void setupButtons(Skin uiSkin) {
	}


	protected void setupStage() {
		stage = new Stage(new StretchViewport(DarkmoorGame.DISPLAY_WIDTH, DarkmoorGame.DISPLAY_WIDTH));
	
		Image background = setupBackground();
		if (background!=null) 
			stage.addActor(background);
		
		for (TextButton button: buttons) {
			stage.addActor(button);
		}
	
		Gdx.input.setInputProcessor(stage);
	}
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		// center the camera: sets stage coordinates 0,0 to bottom left instead of screen center
		stage.getViewport().update(width, height, true);

	}
}
