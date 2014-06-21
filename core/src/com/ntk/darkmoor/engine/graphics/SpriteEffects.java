package com.ntk.darkmoor.engine.graphics;

public enum SpriteEffects {
	NONE("None"), FLIP_HORIZONTALLY("FlipHorizontally"), FLIP_VERTICALY("FlipVertically");
	
	private String description;

	private SpriteEffects(String description) {
		this.description = description;
	}
	
	public String toString() {
		return description;
	}
	
	public static SpriteEffects fromDescription(String description) {
		for (SpriteEffects value: values()) {
			if (value.description.equalsIgnoreCase(description)) {
				return value;
			}
		}
		return NONE;
	}
}
