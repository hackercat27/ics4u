package window;

import geom.Camera3D;
import geom.Shape3D;
import io.Graphics3D;
import io.Input;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import org.joml.Matrix4d;
import org.joml.Quaterniond;
import org.joml.Vector2d;
import util.FileUtils;
import util.Utils;


public class GraphicsRenderer {

    public static final double INT_SCALE = Graphics3D.INT_SCALE;

    private Camera3D camera;
    private Shape3D marker = FileUtils.getShape("res:/objects/marker_red.obj");
    private Shape3D paper = FileUtils.getShape("res:/objects/paper.obj");

    private Graphics3D g3 = new Graphics3D();

    public GraphicsRenderer() {
        camera = new Camera3D();

        Input.addBind("mwheeldown", "front");
        Input.addBind("mwheelup", "back");
    }

    private double time = 0;
    private double depth = -1.5;

    public void update(double deltaTime) {

        double markerDepth = -5.29d;
        double markerScale = 0.08;
        double paperScale = 12.5;

        marker.update(deltaTime);

        if (Input.isActionJustPressed("front")) {
            depth += 0.5;
        }
        if (Input.isActionJustPressed("back")) {
            depth -= 0.5;
        }

        time += deltaTime;

        Vector2d p = Input.getCursorPos();

        paper.position.set(0, 0, -5.5 + depth);
        paper.rotation.set(new Quaterniond());
        paper.rotation.rotateAxis(Math.PI / 2, 1, 0, 0);

        marker.rotation.set(new Quaterniond());
        marker.rotation.rotateAxis(0.4, 0, 1, 0);
        marker.rotation.rotateAxis(Math.PI / 2 - 0.3, 1, 0, 0);
        marker.scale = 1.2 * markerScale;
        paper.scale = paperScale * markerScale;

        p.mul(Math.abs(depth + markerDepth));

        marker.position.set(p, depth + markerDepth);

        camera.update(deltaTime);
    }

    public void render(Graphics2D g2, double width, double height, double t) {

        g3.draw(paper, paper.position, paper.rotation, paper.scale);
        g3.draw(marker, marker.position, marker.rotation, marker.scale);

        double ratio = width / height;

        // translate graphics2d to adhere to NDC instead of the default coord space
        g2.scale(width, -height);
        g2.translate(0.5, -0.5);

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
//        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

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

        Matrix4d perspectiveTransform = new Matrix4d().perspective(camera.getFOV(t), ratio, near, far);
        Matrix4d cameraTransform = Utils.getCameraTransform(camera.getPosition(t), camera.getRotation(t), 1);

        g3.render(g2, cameraTransform, perspectiveTransform);

    }
}
