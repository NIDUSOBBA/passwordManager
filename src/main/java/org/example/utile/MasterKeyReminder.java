package org.example.utile;

import com.github.javakeyring.BackendNotSupportedException;
import org.example.controller.MasterKeyDialogManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

public class MasterKeyReminder {
    private static final Path STATE_FILE = PathDeterminant.reminderPath();
    private static final long INTERVAL_MILLIS = TimeUnit.DAYS.toMillis(2);
    private final MasterKeyDialogManager masterKeyDialogManager;

    public MasterKeyReminder(MasterKeyDialogManager masterKeyDialogManager) {
        this.masterKeyDialogManager = masterKeyDialogManager;
    }

    public void start () throws BackendNotSupportedException {
        long now = System.currentTimeMillis();
        long last = readLastRunTime();

        long timePassed = now - last;
        if (timePassed >= INTERVAL_MILLIS) {
            masterKeyDialogManager.masterKeyRemember();
            saveLastRunTime(now);
        }
    }

    private static long readLastRunTime() {
        try {
            if (Files.exists(STATE_FILE)) {
                String content = Files.readString(STATE_FILE).trim();
                return Long.parseLong(content);
            }
        } catch (IOException | NumberFormatException e) {
            AppLogger.error("The status file could not be read exception: ", e);
        }
        return 0L;
    }

    private static void saveLastRunTime(long time) {
        try {
            System.out.println(STATE_FILE);
            Files.writeString(STATE_FILE, String.valueOf(time));
        } catch (IOException e) {
            AppLogger.error("Couldn't save execution time: ", e);
        }
    }
}
