import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class GraphicsRenderer {

    public GraphicsRenderer() {

    }

    public void update(double deltaTime) {

    }

    public void render(BufferedImage buffer, double interp) {
        Graphics2D g2 = buffer.createGraphics();


        // similar to scanner.close(), we're responsible to release these resources
        // (this prevents a memory leak from creating a new graphics object every frame
        g2.dispose();
    }

}
