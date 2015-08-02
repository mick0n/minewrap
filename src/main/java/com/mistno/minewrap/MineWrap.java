package com.mistno.minewrap;

import static java.lang.ProcessBuilder.Redirect.INHERIT;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.io.*;
import java.util.concurrent.*;

import org.joda.time.*;

import com.mistno.minewrap.backup.*;
import com.mistno.minewrap.config.Config;

public class MineWrap {

	private final ExecutorService threadPool = Executors.newCachedThreadPool();
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

	private ShutdownHook hook;
	private StateHolder stateHolder;

	public MineWrap() throws Exception {
		stateHolder = new StateHolder();

		boolean keepRunning = true;
		while (keepRunning) {
			switch (stateHolder.getState()) {
			case RUN:
				ProcessWrapper process = startServerProcess();
				initTasks(process);
				Future<?> ServerInputTaskFuture = threadPool.submit(new ServerInputTask(process, stateHolder));
				try {
					process.get().waitFor();
				} catch (InterruptedException e) {
					System.err.println("Minecraft closed unexpectedly");
					e.printStackTrace();
				} finally {
					ServerInputTaskFuture.cancel(true);
					process.close();
				}
				break;
			case BACKUP:
				if (Config.contains("backup.7zip.enabled") && Config.get("backup.7zip.enabled").equalsIgnoreCase("true")) {
					new SevenZipBackup(stateHolder).performBackup();
				}
				if (Config.contains("backup.external.enabled") && Config.get("backup.external.enabled").equalsIgnoreCase("true")) {
					new OverrideBackup(stateHolder).performBackup();
				}
				break;
			case END:
			default:
				keepRunning = false;
			}
		}

		threadPool.shutdownNow();
		scheduler.shutdownNow();
	}

	private void initTasks(ProcessWrapper process) {
		if (hook != null) {
			Runtime.getRuntime().removeShutdownHook(hook);
		}
		hook = new ShutdownHook(process, stateHolder);
		Runtime.getRuntime().addShutdownHook(hook);

		DateTime currentDateTime = DateTime.now();
		DateTime scheduledTime = new DateTime(currentDateTime.getYear(), currentDateTime.getMonthOfYear(), currentDateTime.getDayOfMonth(), 23, 55, currentDateTime.getZone());
		Seconds seconds = Seconds.secondsBetween(currentDateTime, scheduledTime);
		scheduler.schedule(new TimedShutdownTask(process, stateHolder), seconds.getSeconds(), SECONDS);
	}

	private ProcessWrapper startServerProcess() throws IOException {
		File serverJar = new File(Config.get("server.jar"));
		if (serverJar.getParent() == null) {
			throw new RuntimeException("Can't find server directory, did you specify the absolute path in minewrap.properties?");
		}

		ProcessBuilder pb = new ProcessBuilder("java", "-Xmx" + Config.get("server.xmx"), "-Xms" + Config.get("server.xms"), "-jar", serverJar.getName(), "nogui")
				.directory(new File(serverJar.getParent()))
				.redirectErrorStream(true)
				.redirectOutput(INHERIT);
		return new ProcessWrapper(pb.start());
	}

	public static void main(String[] args) {
		try {
			Config.initialize();
			new MineWrap();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}