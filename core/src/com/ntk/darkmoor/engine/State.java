package com.ntk.darkmoor.engine;

import com.ntk.darkmoor.stub.GameTime;

public abstract class State {
	
	public boolean exit; //Q: how is this used?

	public abstract void draw();

	public abstract void onActivated();

	public abstract void onDeactivated();

	public abstract void onEnter();

	public abstract void onLeave();

	public abstract void update(GameTime time);

	public boolean isExit() {
		return exit;
	}

	public void setExit(boolean exit) {
		this.exit = exit;
	}
}
