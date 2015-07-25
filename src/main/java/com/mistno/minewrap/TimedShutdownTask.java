package com.mistno.minewrap;

import static com.mistno.minewrap.State.BACKUP;

import java.io.IOException;

public class TimedShutdownTask implements Runnable {

	private final ProcessWrapper process;
	private final StateHolder stateHolder;

	public TimedShutdownTask(ProcessWrapper process, StateHolder stateHolder) {
		this.process = process;
		this.stateHolder = stateHolder;
	}

	@Override
	public void run() {
		try {
			process.writer().write("say Server will restart in 5 minutes\n");
			process.writer().flush();
			Thread.sleep(270000);
			process.writer().write("say Server will restart in 30 seconds\n");
			process.writer().write("say The restart will take several minutes\n");
			process.writer().flush();
			Thread.sleep(30000);
			process.writer().write("save-all\n");
			process.writer().flush();
			Thread.sleep(3000);
			process.writer().write("stop\n");
			process.writer().flush();
			stateHolder.setState(BACKUP);
		} catch (IOException | InterruptedException e) {
			System.err.println("Unable to perform scheduled shutdown. Reason: " + e.getMessage());
		}
	}
}
