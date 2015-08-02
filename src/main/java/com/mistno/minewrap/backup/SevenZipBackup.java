package com.mistno.minewrap.backup;

import static com.mistno.minewrap.State.*;

import java.io.*;
import java.text.DateFormat;
import java.util.Date;

import com.mistno.minewrap.*;
import com.mistno.minewrap.config.Config;

public class SevenZipBackup extends AbstractBackup{
	
	private final StateHolder stateHolder;

	public SevenZipBackup(StateHolder stateHolder) {
		this.stateHolder = stateHolder;
	}
	
	@Override
	public void performBackup() {
		System.out.println("Backup process started...");

		String dateTimeString = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).format(new Date()).replace(":", "").replace(" ", "_").replace("-", "");
		File target = new File(Config.get("backup.targetDir") + "/minewrap_backup_" + dateTimeString + ".7z");
		File source = new File(Config.get("backup.sourceDir"));
		if (!source.exists() || !source.isDirectory()) {
			System.err.println("backup.sourceDir didn't exist or wasn't a directory, aborting backup process!");
			stateHolder.setState(END);
			return;
		}

		try {
			new ProcessBuilder("7z", "a", target.getAbsolutePath(), source.getAbsolutePath(), "-y")
				.inheritIO()
				.start()
				.waitFor();
		} catch (IOException | InterruptedException e) {
			System.err.println("Error running 7zip!");
			e.printStackTrace();
			stateHolder.setState(END);
			return;
		}
		
		stateHolder.setState(RUN);
		System.out.println("Backup process finished!");
	}
}
