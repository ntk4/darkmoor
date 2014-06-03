package com.ntk.darkmoor.engine;

import java.util.Date;

public class HandAction {

	public enum ActionResult {
		/** Last action has no report */
		Ok,

		/** Need ammo */
		NoAmmo,

		/** Hero can't reach target */
		CantReach,
	}

	private ActionResult result;
	private Date time;

	public HandAction(ActionResult result) {
		this.result = result;
		this.time = new Date();
	}

	public boolean isOutdated(Date time, int length) {
		return (time.getTime() + length < time.getTime());
	}

	public ActionResult getResult() {
		return result;
	}

	public Date getTime() {
		return time;
	}
}
