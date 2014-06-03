package com.ntk.darkmoor.config;

import com.badlogic.gdx.Gdx;

public class Log {
	public static final String TAG = "Darkmoor";

	public static void debug(String message) {
		Gdx.app.debug(TAG, message);
	}

	public static void error(String message) {
		Gdx.app.error(TAG, message);
	}
}
