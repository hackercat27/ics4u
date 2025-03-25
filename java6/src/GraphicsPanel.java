import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

public class GraphicsPanel extends JPanel {

    private final Object renderingLock = new Object();

    double fpsMax = 500;
    double fpsCurrent;

    public GraphicsPanel() {

        Thread renderer = new Thread(new Runnable() {
            @Override
            public void run() {

                // infinite loop, rely on system.exit
                for (;;) {

                    // frames per second -> nanoseconds per frame
                    double maxTimeNanos = 1e9 / fpsMax;

                    double startTimeNanos = System.nanoTime();

                    GraphicsPanel.this.repaint();

                    for (;;) {
                        try {
                            renderingLock.wait();
                            break;
                        }
                        catch (InterruptedException e) {
                            System.out.println("Interrupted while waiting for lock to clear, ignoring");
                        }
                    }

                    double endTimeNanos = System.nanoTime();

                    double delta = endTimeNanos - startTimeNanos;

                    double extraTime = delta - maxTimeNanos;

                    if (extraTime > 0) {
                        Utils.sleep(extraTime);
                    }

                    double realEndtimeNanos = System.nanoTime();
                    fpsCurrent = 1e9 / (realEndtimeNanos - startTimeNanos);

                }

            }
        }, "rendering-scheduler");

        Thread ticker = new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }, "update-scheduler");

        renderer.start();;
        ticker.start();
    }

    @Override
    public void paintComponent(Graphics g) {

        if (!(g instanceof Graphics2D g2)) {
            // will spam stdout but whatever
            System.out.println("warning: graphics object not supported");
            super.paintComponent(g);
            return;
        }


        // synchronized to take ownership of renderingLock to prevent IllegalMonitorStateException
        synchronized (renderingLock) {
            renderingLock.notify();
        }


    }
}
