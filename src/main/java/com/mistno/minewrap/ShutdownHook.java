package com.mistno.minewrap;

import static com.mistno.minewrap.State.END;

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
		if(process.isRunning()) {
			System.out.println("Trying to terminate server gracefully...");
			try {
				process.writer().write("stop\n");
				process.writer().flush();
				process.get().waitFor();
			} catch (IOException | InterruptedException e) {
				System.exit(2);
			}
		}
	}
}
