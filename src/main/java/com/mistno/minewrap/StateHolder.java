package com.mistno.minewrap;

import static com.mistno.minewrap.State.RUN;

public class StateHolder {

	private volatile State state = RUN;

	public void setState(State state) {
		this.state = state;
	}

	public State getState() {
		return this.state;
	}
}
