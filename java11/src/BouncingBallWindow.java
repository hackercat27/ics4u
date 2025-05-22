import javax.swing.JFrame;
import javax.swing.SwingUtilities;
/**
 * BouncingBallWindow is the top level of three that produce a window with
 * an animated bouncing ball in it.
 *
 */
public class BouncingBallWindow extends JFrame {
    public BouncingBallWindow() {  // Create window
        setTitle("One Bouncing Ball");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add(new BouncingBall());  // Add bouncing ball panel

        pack();  // Set the window size
        setLocationRelativeTo(null);  // Center the window in the display


    }

    public static void main(String[] args) {
        // Run GUI in the Event Dispatcher Thread (EDT)
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // Set up main window (using Swing's Jframe)
                BouncingBallWindow bbw = new BouncingBallWindow();
                bbw.setVisible(true);
            }
        });
    }
}
