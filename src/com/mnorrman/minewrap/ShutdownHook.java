package com.mnorrman.minewrap;

import static com.mnorrman.minewrap.State.END;

import java.io.IOException;

public class ShutdownHook extends Thread {

	private final ProcessWrapper process;
	private final StateHolder stateHolder;

	public ShutdownHook(ProcessWrapper process, StateHolder stateHolder) {
		this.process = process;
		this.stateHolder = stateHolder;
	}

	public void run() {
		stateHolder.setState(END);
		System.out.println("Trying to terminate server gracefully...");
		if (process != null) {
			try {
				process.writer().write("stop\n");
				process.writer().flush();
			} catch (IOException e) {
				System.exit(2);
			}
		}
	}
}
