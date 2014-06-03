package com.ntk.darkmoor.config;

import com.badlogic.gdx.Gdx;

public class Log {
	public static final String TAG = "Darkmoor";

	public static void debug(String message, Object... args) {
		Gdx.app.debug(TAG, String.format(message, args));
	}

	public static void error(String message, Object... args) {
		Gdx.app.error(TAG, String.format(message, args));
	}
}
