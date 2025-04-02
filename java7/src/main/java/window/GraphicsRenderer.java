package window;

import geom.Camera3D;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import org.joml.Matrix4d;
import util.Utils;


public class GraphicsRenderer {

    public static final double INT_SCALE = Integer.MAX_VALUE;

    private Camera3D camera;


    public GraphicsRenderer() {

        camera = new Camera3D();
    }

    public void update(double deltaTime) {

    }

    public void render(BufferedImage buffer, double interp) {

        Graphics2D g2 = buffer.createGraphics();

        double ratio = (double) buffer.getWidth() / buffer.getHeight();

        // translate graphics2d to adhere to NDC instead of the default coord space
        g2.scale(buffer.getWidth(), -buffer.getHeight());
        g2.translate(0.5, -0.5);

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(Color.BLACK);
        // this over paints but i don't care anymore
        g2.fillRect((int) -ratio, -1, (int) (ratio * 2), 2);

        // the java graphics api expects integer values, but NDC expects to use
        // floating point values from 0 to 1 - so we need to essentially use
        // integers as a fraction, ie: 'int a' can represent a float by treating it as 'a / 2147483647'
        // which will always be in the range of [-1, 1] which should be good enough
        g2.scale(1 / INT_SCALE, 1 / INT_SCALE);

        double near = 0.01;
        double far = 1000;

        Matrix4d perspectiveTransform = new Matrix4d().perspective(camera.getFOV(interp), ratio, near, far);
        Matrix4d cameraTransform = Utils.getCameraTransform(camera.getPosition(interp), camera.getRotation(interp), 1);


        // similar to scanner.close(), we're responsible to release these resources
        // (this prevents a memory leak from creating a new graphics object every frame
        g2.dispose();
    }

}
