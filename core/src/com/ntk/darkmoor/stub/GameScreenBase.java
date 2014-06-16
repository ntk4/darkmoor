package com.ntk.darkmoor.stub;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.ScreenAdapter;
import com.ntk.darkmoor.DarkmoorGame;
import com.ntk.darkmoor.engine.DialogBase;
import com.ntk.darkmoor.engine.Dungeon;
import com.ntk.darkmoor.engine.ScriptedDialog;
import com.ntk.darkmoor.engine.SpellBook;
import com.ntk.darkmoor.engine.Team;

public class GameScreenBase extends ScreenAdapter {


	protected DarkmoorGame game;
	
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
		
	}

	public void unloadContent() {
		// TODO Auto-generated method stub
		
	}

	public void update(float delta, boolean hasFocus, boolean isCovered) {
		
	}

	public void draw(float delta) {
		
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
}
