package window;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import util.Runner;

public class GraphicsPanel extends JPanel {

    private final Object graphicsRendererLock = new Object();

    private double fpsMax = 500;
    private double tpsMax = 120;

    private GraphicsRenderer graphicsRenderer;

    public GraphicsPanel() {

        setPreferredSize(new Dimension(854, 480));

        setBackground(Color.BLACK);
        setForeground(Color.WHITE);

        Thread renderer = new Runner(fpsMax, "") {
            @Override
            public void execute() {
                GraphicsPanel.this.repaint();
                for (;;) {
                    try {
                        // synchronized to take ownership of renderingLock to prevent IllegalMonitorStateException
                        synchronized (graphicsRendererLock) {
                            graphicsRendererLock.wait();
                        }
                        break;
                    }
                    catch (InterruptedException e) {
                        System.out.println("Interrupted while waiting for lock to clear, ignoring");
                    }
                }
            }
        };

        Thread ticker = new Runner(tpsMax, "update-scheduler") {
            @Override
            public void execute() {
                synchronized (graphicsRendererLock) {
                    if (graphicsRenderer != null) {
                        graphicsRenderer.update(getDeltaTime());
                    }
                }
            }
        };

        renderer.start();
        ticker.start();
    }

    @Override
    public void paintComponent(Graphics g) {

        BufferedImage buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);

        synchronized (graphicsRendererLock) {
            if (graphicsRenderer != null) {
                graphicsRenderer.render(buffer, 0);
            }
        }

        super.paintComponent(g);
        g.drawImage(buffer, 0, 0, null);

        synchronized (graphicsRendererLock) {
            graphicsRendererLock.notify();
        }
    }

    public void setGraphicsRenderer(GraphicsRenderer renderer) {
        this.graphicsRenderer = renderer;
    }
}
