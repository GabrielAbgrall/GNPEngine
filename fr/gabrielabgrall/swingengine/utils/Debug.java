package fr.gabrielabgrall.swingengine.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Debug {

    private static int level;

    public static void setLevel(int level) {
        Debug.level = level;
        log("Debug | Set debug level : " + level);
    }

    public static void log(Object log, int level) {
        if(shouldDebug(level)) System.out.println("[DEBUG] " + LocalDateTime.now().format(DateTimeFormatter.ISO_TIME).substring(0, 8) + " | " + log);
    }

    public static void log(Object log) {
        log(log, 1);
    }

    public static boolean shouldDebug(int level) {
        return Debug.level >= level;
    }

    public static boolean shouldDebug() {
        return shouldDebug(1);
    }
}
