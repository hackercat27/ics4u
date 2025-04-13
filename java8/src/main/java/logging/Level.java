package logging;

public enum Level {
    /** Verbose logging, for informal debugging and log spam.*/
    VERBOSE(-1),
    /** Standard logging, for normal application info.*/
    INFO(0),
    /** Minor error logging, for inconsequential errors.*/
    WARN(1),
    /** Error logging, for errors that can't be easily smoothed over - indicating an issue that should be fixed.*/
    ERROR(2),
    /** Fatal errors that will result in the current thread halting or the program exiting.*/
    FATAL(3);
    private final int priority;

    Level(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return this.priority;
    }
}
