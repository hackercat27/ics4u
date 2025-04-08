package logging;

public enum Level {
    VERBOSE(-1), INFO(0), WARN(1), ERROR(2);
    private final int priority;

    Level(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return this.priority;
    }
}
