package window;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import util.Utils;

public class GraphicsPanel extends JPanel {

    private final Object waitLock = new Object();
    private final Object rendererLock = new Object();

    private double fpsMax = 500;
    private double fpsCurrent;

    private double tpsMax = 120;
    private double tpsCurrent;

    private GraphicsRenderer graphicsRenderer;

    public GraphicsPanel() {

        setBackground(Color.BLACK);
        setForeground(Color.WHITE);

        Thread renderer = new Thread(() -> {
            // infinite loop, rely on system.exit
            for (;;) {

                // frames per second -> nanoseconds per frame
                double maxTimeNanos = 1e9 / fpsMax;

                double startTimeNanos = System.nanoTime();

                GraphicsPanel.this.repaint();

                for (;;) {
                    try {
                        // synchronized to take ownership of renderingLock to prevent IllegalMonitorStateException
                        synchronized (waitLock) {
                            waitLock.wait();
                        }
                        break;
                    }
                    catch (InterruptedException e) {
                        System.out.println("Interrupted while waiting for lock to clear, ignoring");
                    }
                }

                double endTimeNanos = System.nanoTime();

                double frameDeltaNanos = endTimeNanos - startTimeNanos;

                double extraTime = maxTimeNanos - frameDeltaNanos;

                if (extraTime > 0) {
                    Utils.sleep(extraTime);
                }

                double realEndtimeNanos = System.nanoTime();
                fpsCurrent = 1e9 / (realEndtimeNanos - startTimeNanos);

            }

        }, "rendering-scheduler");

        Thread ticker = new Thread(() -> {
            double deltaTimeSeconds = 0;

            // infinite loop, rely on system.exit
            for (;;) {

                // frames per second -> nanoseconds per frame
                double maxTimeNanos = 1e9 / tpsMax;

                double startTimeNanos = System.nanoTime();

                synchronized (rendererLock) {
                    if (graphicsRenderer != null) {
                        graphicsRenderer.update(deltaTimeSeconds);
                    }
                }

                double endTimeNanos = System.nanoTime();

                double tickDeltaNanos = endTimeNanos - startTimeNanos;

                double extraTimeNanos = maxTimeNanos - tickDeltaNanos;

                if (extraTimeNanos > 0) {
                    Utils.sleep(extraTimeNanos);
                }

//                System.out.printf("%.6f\r", maxTimeNanos);
                double realEndtimeNanos = System.nanoTime();
                tpsCurrent = 1e9 / (realEndtimeNanos - startTimeNanos);
                deltaTimeSeconds = 1 / tpsCurrent;

            }
        }, "update-scheduler");

        renderer.start();
        ticker.start();
    }

    @Override
    public void paintComponent(Graphics g) {

        BufferedImage buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);

        synchronized (rendererLock) {
            if (graphicsRenderer != null) {
                graphicsRenderer.render(buffer, 0);
            }
        }

        super.paintComponent(g);
        g.drawImage(buffer, 0, 0, null);

        synchronized (waitLock) {
            waitLock.notify();
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(854, 480);
    }

    public void setGraphicsRenderer(GraphicsRenderer renderer) {
        this.graphicsRenderer = renderer;
    }
}
