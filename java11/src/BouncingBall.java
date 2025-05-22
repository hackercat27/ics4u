import java.awt.*;
import javax.swing.*;
/**
 * BouncingBall produces a panel with one ball bouncing off the edges.
 *
 * It uses a single instance of the Ball class to provide the bouncer.
 */
public class BouncingBall extends JPanel {
    private final Dimension SIZE = new Dimension(600,450); // Panel size
    private final int UPDATE_RATE = 200; // Number of refreshes per second
    private Ball ball; // The beautiful bouncing ball
    private Thread ballThread; // Animation thread

    /**
     * Constructor sets up the panel and starts its animation thread
     */
    public BouncingBall() {

        System.setProperty("sun.java2d.opengl", "true");
        setPreferredSize(SIZE); // Set panel size
        ball = new Ball(SIZE); // Create a Ball object
        // Start the ball bouncing (in its own animation thread)

        new Timer(5, e -> {

            new Thread() {
                public void run() {
                    while (true) {
                        setBackground(new Color((int) (0xFFFFFF * Math.random())));
                    }
                }
        }.start();

        }).start();

        ballThread = new Thread(() -> {
            while (true) { // Execute one update step
                // Calculate the balls' new positions
                ball.move();
                // Refresh the display
                repaint(); // Callback paintComponent()
                // Delay so the animation's not too fast
                try {
                    Thread.sleep(1000 / UPDATE_RATE);  // milliseconds
                } catch (InterruptedException ex) { }
            }
        });
        ballThread.start();  // Start the animation
    }

    /**
     * Override the JPanel paintComponent method with our drawing of a ball
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);    // Paint background
        Graphics2D g2d= (Graphics2D) g;
        // Draw the box's background
//        g2d.setColor(Color.WHITE);
//        g2d.fillRect(0, 0, getWidth(), getHeight());

        // Draw the ball
//        ball.paint(g2d);
    }
}
