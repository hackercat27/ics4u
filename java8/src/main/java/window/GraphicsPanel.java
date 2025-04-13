package window;

import io.Input;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.function.DoubleSupplier;
import javax.swing.JPanel;
import org.joml.Vector2d;
import util.Runner;

public class GraphicsPanel extends JPanel {

    private final Object graphicsRendererLock = new Object();

    private double fpsMax = -1;
    private double tpsMax = 66;

    private long lastUpdateTimeNanos;

    private GraphicsRenderer graphicsRenderer;

    public GraphicsPanel() {

        setPreferredSize(new Dimension(854, 480));

        setBackground(Color.BLACK);
        setForeground(Color.WHITE);

        addKeyListener(Input.get());
        addMouseListener(Input.get());
        addMouseMotionListener(Input.get());
        addMouseWheelListener(Input.get());

        // allow focusing this component so that input events work correctly
        setFocusable(true);

        DoubleSupplier getMaxFPS = () -> {
            // if fpsMax < 0 then resort to system max refreshrate
            if (fpsMax > 0) {
                return fpsMax;
            }
            try {
                return getGraphicsConfiguration()
                        .getDevice()
                        .getDisplayMode()
                        .getRefreshRate();
            }
            catch (NullPointerException ignored) {
                return 60;
            }
        };

        Thread renderer = new Runner(getMaxFPS, "renderer") {
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
                        lastUpdateTimeNanos = System.nanoTime();
                        graphicsRenderer.update(getDeltaTime());
                    }
                    Input.get().update();
                    Input.get().setScreenSize(new Vector2d(getWidth(), getHeight()));
                }
            }
        };

        ticker.start();
        renderer.start();
    }

    public void setFPS(double fps) {
        this.fpsMax = fps;
    }

    public void setTps(double tps) {
        this.tpsMax = tps;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (!(g instanceof Graphics2D g2)) {
            return;
        }

        synchronized (graphicsRendererLock) {
            if (graphicsRenderer != null) {

                long nowNanos = System.nanoTime();
                long deltaNanos = nowNanos - lastUpdateTimeNanos;

                double targetTickTimeNanos = 1e9 / tpsMax;
                double tickProgress = deltaNanos / targetTickTimeNanos;

                graphicsRenderer.render(g2, getWidth(), getHeight(), Math.clamp(tickProgress, 0, 1));

            }
        }
    }

    public void setGraphicsRenderer(GraphicsRenderer renderer) {
        this.graphicsRenderer = renderer;
    }
}
