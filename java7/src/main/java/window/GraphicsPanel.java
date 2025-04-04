package window;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JPanel;
import util.Runner;

public class GraphicsPanel extends JPanel {

    private final Object graphicsRendererLock = new Object();

    private double fpsMax = 500;
    private double tpsMax = 120;

    private GraphicsRenderer graphicsRenderer;

    public GraphicsPanel(JFrame parent) {

        setPreferredSize(new Dimension(854, 480));

        setBackground(Color.BLACK);
        setForeground(Color.WHITE);

        Thread renderer = new Runner(fpsMax, "renderer") {
            @Override
            public void execute() {
                fps = getExecutionsPerSecond();
                GraphicsPanel.this.repaint();
//                for (;;) {
//                    try {
//                        // synchronized to take ownership of renderingLock to prevent IllegalMonitorStateException
//                        synchronized (graphicsRendererLock) {
//                            graphicsRendererLock.wait();
//                        }
//                        break;
//                    }
//                    catch (InterruptedException e) {
//                        System.out.println("Interrupted while waiting for lock to clear, ignoring");
//                    }
//                }
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

        ticker.start();
        renderer.start();
    }

    private double fps;
    public double getFPS() {
        return fps;
    }

    @Override
    public void paintComponent(Graphics g) {

        double resScale = 480d / getHeight();

        BufferedImage buffer = new BufferedImage((int) (getWidth() * resScale), (int) (getHeight() * resScale), BufferedImage.TYPE_INT_ARGB);

        synchronized (graphicsRendererLock) {
            if (graphicsRenderer != null) {
                graphicsRenderer.render((Graphics2D) g, getWidth(), getHeight(), 0);
            }
        }

        super.paintComponent(g);
        ((Graphics2D) g).scale(1 / resScale, 1 / resScale);
        g.drawImage(buffer, 0, 0, null);

        synchronized (graphicsRendererLock) {
            graphicsRendererLock.notify();
        }
    }

    public void setGraphicsRenderer(GraphicsRenderer renderer) {
        this.graphicsRenderer = renderer;
    }
}
