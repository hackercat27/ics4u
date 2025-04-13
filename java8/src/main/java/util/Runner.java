package util;

import java.util.function.DoubleSupplier;

public abstract class Runner extends Thread {

    private double currentUps;
    private double deltaTimeSeconds = 0;

    private DoubleSupplier getMaxUps;

    public Runner(double maxUps, String threadName) {
        super(threadName);
        getMaxUps = () -> Math.abs(maxUps);
    }

    public Runner(DoubleSupplier maxUps, String threadName) {
        super(threadName);
        getMaxUps = maxUps;
    }

    @Override
    public final void run() {

        // infinite loop, rely on system.exit
        for (;;) {

            // executions per second -> nanoseconds per executions
            double maxTimeNanos = 1e9 / getMaxUps.getAsDouble();

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
