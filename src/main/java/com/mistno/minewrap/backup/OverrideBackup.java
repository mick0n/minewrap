package com.mistno.minewrap.backup;

import static com.mistno.minewrap.State.RUN;

import java.io.IOException;

import com.mistno.minewrap.*;
import com.mistno.minewrap.config.Config;

public class OverrideBackup extends AbstractBackup{

	private final StateHolder stateHolder;

	public OverrideBackup(StateHolder stateHolder) {
		this.stateHolder = stateHolder;
	}
	
	@Override
	public void performBackup() {
		try {
			System.out.println("Backup process started...");
			new ProcessBuilder(Config.get("backup.script"))
				.inheritIO()
				.start()
				.waitFor();
			stateHolder.setState(RUN);
			System.out.println("Backup process finished.");
		} catch (IOException | InterruptedException e) {
			System.err.println("Error running overrided backup script: " + e.getMessage());
		}
	}
	
}
