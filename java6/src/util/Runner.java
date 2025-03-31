package util;

public abstract class Runner extends Thread {

    private double currentUps;
    private double maxUps;
    private double deltaTimeSeconds = 0;


    public Runner(double maxUps, String threadName) {
        super(threadName);
        this.maxUps = Math.abs(maxUps);
    }

    @Override
    public void run() {

        // infinite loop, rely on system.exit
        for (;;) {

            // executions per second -> nanoseconds per executions
            double maxTimeNanos = 1e9 / maxUps;

            double startTimeNanos = System.nanoTime();

            execute();

            double endTimeNanos = System.nanoTime();

            double frameDeltaNanos = endTimeNanos - startTimeNanos;

            double extraTime = maxTimeNanos - frameDeltaNanos;

            if (extraTime > 0) {
                Utils.sleep(extraTime);
            }

            double realEndtimeNanos = System.nanoTime();
            currentUps = 1e9 / (realEndtimeNanos - startTimeNanos);

            deltaTimeSeconds = 1 / currentUps;
        }

    }

    public abstract void execute();

    public double getExecutionsPerSecond() {
        return currentUps;
    }

    public double getDeltaTime() {
        return deltaTimeSeconds;
    }

}
