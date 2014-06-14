package com.ntk.darkmoor.engine.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.ntk.darkmoor.engine.GameColors;
import com.ntk.darkmoor.resource.Resources;

public class GUI {

	private static BitmapFont menuFont;
	private static BitmapFont dialogFont;
	private static ShapeRenderer shapeRenderer;

	static {
		menuFont = Resources.lockSharedFontAsset("intro");
		dialogFont = Resources.lockSharedFontAsset("inventory");

		shapeRenderer = new ShapeRenderer();
	}

	public static void Dispose() {
		if (menuFont != null)
			menuFont.dispose();
		menuFont = null;

		if (dialogFont != null)
			dialogFont.dispose();
		dialogFont = null;
	}

	public static void drawDoubleBevel(Batch batch, Rectangle rectangle) {
		drawDoubleBevel(batch, rectangle, GameColors.Main, GameColors.Light, GameColors.Dark, false);
	}

	public static void drawDoubleBevel(Batch batch, Rectangle rectangle, boolean reverse) {
		drawDoubleBevel(batch, rectangle, GameColors.Main, GameColors.Light, GameColors.Dark, reverse);
	}

	public static void drawDoubleBevel(Batch batch, Rectangle rect, Color bg, Color light, Color dark, boolean reverse) {
		shapeRenderer.begin(ShapeType.Filled);

		Vector2 point = new Vector2();
		rect.getPosition(point);
		Color color = reverse ? dark : light;

		shapeRenderer.rect(point.x + 2, point.y, rect.width - 2, 2, color, color, color, color);
		shapeRenderer.rect(point.x + 4, point.y + 2, rect.width - 4, 2, color, color, color, color);
		shapeRenderer.rect(rect.x + rect.width - 4, point.y + 4, 2, rect.height - 8, color, color, color, color);
		shapeRenderer.rect(rect.x + rect.width - 2, point.y + 4, 2, rect.height - 6, color, color, color, color);

		color = reverse ? light : dark;
		shapeRenderer.rect(point.x, point.y + 2, 2, rect.height - 4, color, color, color, color);
		shapeRenderer.rect(point.x + 2, point.y + 4, 2, rect.height - 6, color, color, color, color);
		shapeRenderer.rect(point.x, point.y + rect.height - 2, rect.width - 2, 2, color, color, color, color);
		shapeRenderer.rect(point.x, point.y + rect.height - 4, rect.width - 4, 2, color, color, color, color);

		shapeRenderer.end();
	}

	public static void drawSimpleBevel(SpriteBatch batch, Rectangle rect) {
		drawSimpleBevel(batch, rect, GameColors.Main, GameColors.Light, GameColors.Dark, false);
	}

	public static void drawSimpleBevel(SpriteBatch batch, Rectangle rect, boolean reverse) {
		drawSimpleBevel(batch, rect, GameColors.Main, GameColors.Light, GameColors.Dark, reverse);
	}

	public static void drawSimpleBevel(SpriteBatch batch, Rectangle rect, Color bg, Color light, Color dark,
			boolean reverse) {
		shapeRenderer.begin(ShapeType.Filled);

		Vector2 point = new Vector2();
		rect.getPosition(point);
		Color color = reverse ? dark : light;

		shapeRenderer.rect(point.x + 2, point.y, rect.width - 2, 2, color, color, color, color);
		shapeRenderer.rect(rect.x + rect.width - 2, point.y + 2, 2, rect.height - 4, color, color, color, color);

		color = reverse ? light : dark;
		shapeRenderer.rect(point.x, point.y + 2, 2, rect.height - 2, color, color, color, color);
		shapeRenderer.rect(point.x + 2, point.y + rect.height - 2, rect.width - 4, 2, color, color, color, color);

		shapeRenderer.end();
	}

	public static BitmapFont getDialogFont() {
		return dialogFont;
	}

	public static BitmapFont getMenuFont() {
		return menuFont;
	}

	public static void drawRectangle(Rectangle rect, Color color) {
		shapeRenderer.begin(ShapeType.Filled);

		shapeRenderer.rect(rect.x, rect.y, rect.width ,rect.height, color, color, color, color);

		shapeRenderer.end();
	}

}
