package com.ntk.darkmoor;

import com.badlogic.gdx.Game;
import com.ntk.darkmoor.stub.GameScreenBase;

public class ScreenManager {

	private static Game game;
	private static IntroScreen introScreen;
	
	public static void init(Game parentGame) {
		game = parentGame;
		introScreen = new IntroScreen();
	}
	
	public static void showIntroScreen() {
		game.setScreen(introScreen);
	}
	
	public static void exitScreen() {
		game.getScreen().hide();
		game.dispose();
	}

	public static void addScreen(GameScreenBase screen) {
		// TODO Auto-generated method stub
		
	}
}
