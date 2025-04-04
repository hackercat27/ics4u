import javax.swing.JFrame;
import window.GraphicsPanel;
import window.GraphicsRenderer;

public class Main {

    public static void main(String[] args) {

        System.setProperty("sun.java2d.opengl", "true");
//        System.out.println(System.getProperty("sun.java2d.opengl"));

        JFrame frame = new JFrame("java7 2d arrays");
        GraphicsPanel panel = new GraphicsPanel(frame);

        panel.setGraphicsRenderer(new GraphicsRenderer());

        frame.add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }



}
