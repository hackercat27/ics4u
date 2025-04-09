package window;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import util.Runner;

public class GraphicsPanel extends JPanel {

    private final Object graphicsRendererLock = new Object();

    private double fpsMax = 500;
    private double tpsMax = 60;

    private long lastUpdateTimeNanos;

    private GraphicsRenderer graphicsRenderer;

    public GraphicsPanel() {

        setPreferredSize(new Dimension(854, 480));

        setBackground(Color.BLACK);
        setForeground(Color.WHITE);

        Thread renderer = new Runner(fpsMax, "renderer") {
            @Override
            public void execute() {
                GraphicsPanel.this.repaint();
            }
        };

        Thread ticker = new Runner(tpsMax, "update-scheduler") {
            @Override
            public void execute() {
                synchronized (graphicsRendererLock) {
                    if (graphicsRenderer != null) {
                        graphicsRenderer.update(getDeltaTime());
                        lastUpdateTimeNanos = System.nanoTime();
                    }
                }
            }
        };

        ticker.start();
        renderer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (!(g instanceof Graphics2D g2)) {
            return;
        }

        synchronized (graphicsRendererLock) {
            if (graphicsRenderer != null) {
                double targetTickTimeNanos = 1e9 / tpsMax;
                double tickProgress = (System.nanoTime() - lastUpdateTimeNanos) / targetTickTimeNanos;

                graphicsRenderer.render(g2, getWidth(), getHeight(), tickProgress);
            }
        }
    }

    public void setGraphicsRenderer(GraphicsRenderer renderer) {
        this.graphicsRenderer = renderer;
    }
}
