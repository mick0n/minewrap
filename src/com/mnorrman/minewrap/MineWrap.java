package com.mnorrman.minewrap;

import static com.mnorrman.minewrap.State.RUN;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.io.*;
import java.text.DateFormat;
import java.util.Date;
import java.util.concurrent.*;

import org.joda.time.*;

public class MineWrap {

	private final ExecutorService threadPool = Executors.newFixedThreadPool(2);
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

	private ShutdownHook hook;
	private ServerInputTask serverInputTask;
	private StateHolder stateHolder;

	public MineWrap() throws Exception {
		stateHolder = new StateHolder();

		boolean keepRunning = true;
		while (keepRunning) {
			switch (stateHolder.getState()) {
			case RUN:
				ProcessWrapper process = startServerProcess();
				initTasks(process);
				try {
					String line = "";
					while ((line = process.reader().readLine()) != null) {
						System.out.println(line);
					}
				} catch (IOException e) {
					System.err.println("Minecraft closed unexpectedly: " + e.getMessage());
				} finally {
					threadPool.shutdownNow();
					process.close();
				}
				break;
			case BACKUP:
				performBackup();
				break;
			case END:
			default:
				keepRunning = false;
			}
		}
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
		scheduler.scheduleAtFixedRate(new TimedShutdownTask(process, stateHolder), seconds.getSeconds(), 24 * 60 * 60, SECONDS);

		serverInputTask = new ServerInputTask(process, stateHolder);
		threadPool.submit(serverInputTask);
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

	private void performBackup() throws InterruptedException, IOException {
		System.out.println("Backup process started...");

		String dateTimeString = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).format(new Date()).replace(":", "").replace(" ", "_").replace("-", "");
		File target = new File(Config.get("backup.targetDir") + "/backup_" + dateTimeString + ".7z");
		File source = new File(Config.get("backup.sourceDir"));
		if (!source.exists() || !source.isDirectory()) {
			System.err.println("backup.sourceDir didn't exist or wasn't a directory, aborting backup process!");
			return;
		}

		ProcessBuilder pb = new ProcessBuilder("7z", "a", target.getAbsolutePath(), source.getAbsolutePath());
		pb.redirectErrorStream(true);
		Process p = pb.start();

		try (BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
			String line = "";
			while ((line = br.readLine()) != null) {
				System.out.println(line);
			}
		} catch (IOException e) {
			System.err.println("7-zip closed unexpectedly: " + e.getMessage());
		}
		stateHolder.setState(RUN);
		System.out.println("Backup process finished!");
	}

	private ProcessWrapper startServerProcess() throws IOException {
		File serverJar = new File(Config.get("server.jar"));
		if (serverJar.getParent() == null) {
			throw new RuntimeException("Can't find server directory, did you specify the absolute path in minewrap.properties?");
		}

		ProcessBuilder pb = new ProcessBuilder("java", "-Xmx" + Config.get("server.xmx"), "-Xms" + Config.get("server.xms"), "-jar", serverJar.getName(), "nogui");
		pb.directory(new File(serverJar.getParent()));
		pb.redirectErrorStream(true);
		return new ProcessWrapper(pb.start());
	}

}