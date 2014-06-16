package com.ntk.darkmoor.stub;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Rectangle;

public class Display {

	public static void pushScissor(Rectangle scissor) {
		// TODO Auto-generated method stub
		
	}

	public static void popScissor() {
		// TODO Auto-generated method stub
		
	}

	public static void clearBuffers() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
	}

	public static void init() {
		// TODO Auto-generated method stub
		
	}

}
