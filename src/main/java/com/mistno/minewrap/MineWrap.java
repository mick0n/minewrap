package com.mistno.minewrap;

import com.mistno.minewrap.backup.OverrideBackup;
import com.mistno.minewrap.backup.ZipBackup;
import com.mistno.minewrap.config.Config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;

import static java.lang.ProcessBuilder.Redirect.INHERIT;
import static java.util.concurrent.TimeUnit.SECONDS;

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
                    if (Config.contains("backup.zip.enabled") && Config.get("backup.zip.enabled").equalsIgnoreCase("true")) {
                        new ZipBackup(stateHolder).performBackup();
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

        int interval;
        try {
            interval = Integer.parseInt(Config.get("backup.interval"));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            interval = 24; //Fallback to 24 hour interval
        }
        scheduler.schedule(new TimedShutdownTask(process, stateHolder), TimeUtil.getSecondsUntilNextInterval(interval).getSeconds(), SECONDS);
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
            Config.initialize(new FileInputStream(new File("minewrap.properties")));
            new MineWrap();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}