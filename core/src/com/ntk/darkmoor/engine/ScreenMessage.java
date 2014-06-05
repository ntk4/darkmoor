package com.ntk.darkmoor.engine;

import com.badlogic.gdx.graphics.Color;

public class ScreenMessage {

	private String message;
	private Color color;
	private int life;
	private static int duration;

	public ScreenMessage(String msg, Color color) {
		this.message = msg;
		this.color = color;
		this.life = 15000; // 15 seconds
	}

	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}

	public Color getColor() {
		return color;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public static int getDuration() {
		return duration;
	}

	public static void setDuration(int duration) {
		ScreenMessage.duration = duration;
	}

}
