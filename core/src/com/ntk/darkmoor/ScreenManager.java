package com.ntk.darkmoor;


public class ScreenManager {

	private static DarkmoorGame game;
	private static IntroScreen introScreen;
	
	public static void init(DarkmoorGame parentGame) {
		game = parentGame;
		
		introScreen = new IntroScreen(parentGame);
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
