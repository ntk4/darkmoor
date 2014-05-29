package com.ntk.darkmoor.config;

public enum GameLanguage {

	ENGLISH ("English"), FRENCH ("French");
	
	private String value;
	
	private GameLanguage(String value) {
		this.value = value;
	}
	
	public String value() {
		return value;
	}
	
	public static String[] stringValues() {
		String[] stringValues = new String[values().length];
		int i = 0;
		for (GameLanguage gameLanguage: values()) {
			stringValues[i++] = gameLanguage.value;
		}
		return stringValues;
	}
	
	public static GameLanguage getDefault() {
		return ENGLISH;
	}
}
