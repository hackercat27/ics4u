package logging;

import java.io.PrintStream;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Logger {

    public static final PrintStream err;
    public static final PrintStream warn;
    public static final PrintStream out;

    static {
        out = System.out;
        warn = System.out;
        err = System.out;
    }

    public static void log(Level level, String message, Object... args) {
        log(level, String.format(message, args));
    }

    public static void log(Level level, Object o) {
        write(level, String.valueOf(o));
    }

    private static String getTime() {
        LocalTime time = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return time.format(formatter);
    }

    private static void write(Level level, String message) {

        Level minLevel;
        try {
            minLevel = Level.valueOf(System.getProperty("hackercat.logging.level").toUpperCase());
        }
        catch (IllegalArgumentException ignored) {
            minLevel = Level.INFO; // default to just info
        }

        PrintStream stream = switch (level) {
            case Level.ERROR -> err;
            case Level.WARN -> warn;
            default -> out;
        };

        if (level.getPriority() < minLevel.getPriority()) {
            return;
        }

        stream.printf("[%s] [%s/%s]: %s\n", getTime(), Thread.currentThread().getName(), level.name(), message);
    }
}
