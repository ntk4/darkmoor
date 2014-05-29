package com.ntk.darkmoor.config;

public enum InputType {
	QWERTY("qwerty"), AZERTY("azerty");
	
	private String value;
	
	private InputType(String value) {
		this.value = value;
	}
	
	public String value() {
		return value;
	}
	
	public static String[] stringValues() {
		String[] stringValues = new String[values().length];
		int i = 0;
		for (InputType inputType: values()) {
			stringValues[i++] = inputType.value;
		}
		return stringValues;
	}
	
	public static InputType getDefault() {
		return QWERTY;
	}
}
