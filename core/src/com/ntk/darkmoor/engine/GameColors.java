package com.ntk.darkmoor.engine;

import com.badlogic.gdx.graphics.Color;

public class GameColors {
	public static final Color White = Color.WHITE;
	public static final Color Black = Color.BLACK;
	public static final Color LightGrey = getColor(166, 166, 186, 255);
	public static final Color Light = getColor(150, 150, 174);
	public static final Color Dark = getColor(52, 52, 81);
	public static final Color Main = getColor(109, 109, 138);
	public static final Color Cyan = getColor(85, 255, 255, 255);
	public static final Color Red = getColor(255, 85, 85, 255);
	public static final Color Green = getColor(0, 170, 0, 255);
	public static final Color LightBlue = getColor(138, 146, 207, 255);
	public static final Color Blue = getColor(101, 105, 182, 255);
	public static final Color DarkBlue = getColor(44, 48, 138, 255);
	public static final Color Yellow = getColor(255, 255, 85, 255);
	public static final Color LightGreen = getColor(146, 207, 138, 255);

	public static Color getColor(int r, int g, int b, int a) {
		int col = Color.rgba8888((float)r/255, (float)g/255, (float)b/255, (float)a/255);
		Color result = Color.WHITE;
		Color.rgba8888ToColor(result, col);
		return result;
	}
	
	public static Color getColorFromArgb(int a, int r, int g, int b) {
		int col = Color.rgba8888((float)r, (float)g/255, (float)b/255, (float)a/255);
		Color result = Color.WHITE;
		Color.rgba8888ToColor(result, col);
		return result;
	}

	public static Color getColor(int r, int g, int b) {
		int col = Color.rgb888((float)r/255, (float)g/255, (float)b/255);
		Color result = Color.WHITE;
		Color.rgb888ToColor(result, col);
		return result;
	}

}
