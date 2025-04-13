import javax.swing.JFrame;
import window.GraphicsPanel;
import window.GraphicsRenderer;

public class Main {

    public static void main(String[] args) {

        // enforce hardware accel because otherwise bad performance :(
        System.setProperty("sun.java2d.opengl", "true");
        System.setProperty("hackercat.logging.level", "verbose");

        JFrame frame = new JFrame("java8");
        GraphicsPanel panel = new GraphicsPanel();

        panel.setGraphicsRenderer(new GraphicsRenderer());

        frame.add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.pack();

    }

}
