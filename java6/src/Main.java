import javax.swing.JFrame;
import window.GraphicsPanel;
import window.GraphicsRenderer;

public class Main {

    public static void main(String[] args) {

        GraphicsPanel panel = new GraphicsPanel();
        JFrame frame = new JFrame();

        panel.setGraphicsRenderer(new GraphicsRenderer());

        frame.add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }

}
