package com.ntk.darkmoor.engine.script.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.ntk.darkmoor.engine.GameColors;
import com.ntk.darkmoor.engine.gui.GUI;
import com.ntk.darkmoor.stub.GameTime;

public class GUIScriptButton {

	private String text;
	private Rectangle rectangle;
	private boolean visible;
	private Color textColor;
	private Color backColor;

	public GUIScriptButton() {
		this("", new Rectangle());
	}
	
	public GUIScriptButton(String text, Rectangle rectangle) {
		this.text = text;
		this.rectangle = rectangle;
		this.visible = true;
		this.textColor = Color.WHITE;
		this.backColor = GameColors.Main;
	}

	public void update(GameTime time)
	{
		/* TODO: controller: find out how to replace mouse handling with touch
		// Mouse over
		if (Rectangle.Contains(Mouse.Location))
		{
			// Mouse over
			if (!IsMouseOver)
				OnMouseEnter();

			// Mouse move
			if (!Mouse.MoveDelta.IsEmpty)
				OnMouseMove();

			// Mouse click
			if (Mouse.IsNewButtonDown(System.Windows.Forms.MouseButtons.Left))
				OnMouseClick();

			// Mouse down
			if (Mouse.IsButtonDown(System.Windows.Forms.MouseButtons.Left))
				OnMouseDown();

			// Mouse up
			if (Mouse.IsNewButtonUp(System.Windows.Forms.MouseButtons.Left))
				OnMouseUp();
		}
		else
		{
			// Mouse leave
			if (IsMouseOver)
				OnMouseLeave();
		}

		// Mouse click
		if (Mouse.IsNewButtonUp(System.Windows.Forms.MouseButtons.Left) && IsMouseOver)
			OnMouseClick();
		*/
	}
	
	public void draw(SpriteBatch batch)
	{
		if (batch == null || !isVisible())
			return;

		// Border
		boolean reverse = false;
		/*TODO: controller: replace mouse with touch
		if (IsMouseOver && Mouse.IsButtonDown(System.Windows.Forms.MouseButtons.Left))
			reverse = true;
			*/
		
		GUI.drawSimpleBevel(batch, rectangle, backColor, GameColors.Light, GameColors.Dark, reverse);

		// Text size
		TextBounds textsize = GUI.getDialogFont().getBounds(text);

		// Get text location
		Vector2 pos = new Vector2
		(
			rectangle.x + (rectangle.width - textsize.width) / 2,
			rectangle.y + (rectangle.height - textsize.height) / 2
		);

		// Color
		/*TODO: controller: replace mouse with touch. The commented line is the original one */
		//Color color = isMouseOver() ? GameColors.CYAN : Color.WHITE;
		Color color = Color.WHITE;
		
		GUI.getDialogFont().setColor(color);
		GUI.getDialogFont().draw(batch, text, pos.x, pos.y);
		//original code: batch.drawString(GUI.getDialogFont(), pos, color, text);
	}
	
	//TODO: controller: here we have to add the handlers for mouse enter, leave, PaintEventHandler etc.
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Rectangle getRectangle() {
		return rectangle;
	}

	public void setRectangle(Rectangle rectangle) {
		this.rectangle = rectangle;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public Color getTextColor() {
		return textColor;
	}

	public void setTextColor(Color textColor) {
		this.textColor = textColor;
	}

	public Color getBackColor() {
		return backColor;
	}

	public void setBackColor(Color backColor) {
		this.backColor = backColor;
	}
}
