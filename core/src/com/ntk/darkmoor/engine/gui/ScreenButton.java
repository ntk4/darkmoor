package com.ntk.darkmoor.engine.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;

public class ScreenButton extends Button {

	private EventListener selectedListener;
	private String text;
	private Rectangle rectangle;
	private boolean visible;
	private Color textColor;
	private boolean reactOnMouseOver;
	private Object tag;

	public ScreenButton(String text, Rectangle rectangle) {
		this.text = text;
		this.rectangle = rectangle;
		this.visible = true;
		this.textColor = Color.WHITE;
		this.reactOnMouseOver = true;
	}

	public void onSelectEntry() {
		if (selectedListener != null) {
			Event e = new Event();
			e.setTarget(this);
			selectedListener.handle(e);
		}
	}


	// ntk: careful here! we provide only one selected Listener!
	protected EventListener getSelectedListener() {
		return selectedListener;
	}

	protected void setSelectedListener(EventListener selectedListener) {
		this.selectedListener = selectedListener;
	}

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

	public boolean isReactOnMouseOver() {
		return reactOnMouseOver;
	}

	public void setReactOnMouseOver(boolean reactOnMouseOver) {
		this.reactOnMouseOver = reactOnMouseOver;
	}

	public Object getTag() {
		return tag;
	}

	public void setTag(Object tag) {
		this.tag = tag;
	}
}
