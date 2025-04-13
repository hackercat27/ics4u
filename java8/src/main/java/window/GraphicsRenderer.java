package window;

import geom.Camera3D;
import geom.Face3D;
import geom.Shape3D;
import io.Input;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import logging.Level;
import logging.Logger;
import org.joml.Matrix4d;
import org.joml.Quaterniond;
import org.joml.Vector2d;
import org.joml.Vector3d;
import util.FileUtils;
import util.Utils;


public class GraphicsRenderer {

    public static final double INT_SCALE = Integer.MAX_VALUE;

    private Camera3D camera;
    private Shape3D cube = FileUtils.getShape(FileUtils.getInputStream("res:/objects/paper.obj"));

    public GraphicsRenderer() {
        camera = new Camera3D();

        Input.addBind("mwheeldown", "front");
        Input.addBind("mwheelup", "back");

        Input.addBind("f", "test");
    }

    double time = 0;

    double depth = -8;

    public void update(double deltaTime) {

        cube.update(deltaTime);

        if (Input.isActionPressed("test")) {
            Logger.log(Level.INFO, "test! :)");
        }

        if (Input.isActionJustPressed("front")) {
            depth += 0.5;
        }
        if (Input.isActionJustPressed("back")) {
            depth -= 0.5;
        }

        time += deltaTime;
//        cube.position.set(0, 0, Math.sin(time) * 2 - 8);

        Vector2d p = Input.getCursorPos();

        double m = -depth * depth;

        p.mul(Math.abs(m));

        cube.position.set(p, m);
//        cube.position.set(0, 0, -8);
//        cube.scale = Math.sin(time) + 1.1;
//        cube.position.x = Math.sin(time);
//        cube.position.y = Math.cos(time);
        cube.rotation.set(new Quaterniond());
        cube.rotation.rotateAxis(Math.PI / 2 * p.x / 8, 0, 1, 0);
        cube.rotation.rotateAxis(Math.PI / 2 * (1 - (p.y / 8)), 1, 0, 0);
//        cube.rotation.rotateAxis(-Math.PI / 2, 0, 1, 0);
    }

    public void render(Graphics2D g2, double width, double height, double interp) {

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

        Matrix4d perspectiveTransform = new Matrix4d().perspective(camera.getFOV(interp), ratio, near, far);
        Matrix4d cameraTransform = Utils.getCameraTransform(camera.getPosition(interp), camera.getRotation(interp), 1);

        Matrix4d objectTransform = Utils.getTransform(cube.getPosition(interp), cube.rotation, cube.scale);

        Matrix4d mat = new Matrix4d()
                .mul(perspectiveTransform)
                .mul(cameraTransform)
                .mul(objectTransform);

        Matrix4d mat2 = new Matrix4d()
                .mul(cameraTransform)
                .mul(objectTransform);


        List<Face3D> faces = new ArrayList<>(List.of(cube.getFaces()));

        Vector3d forwardVector = new Vector3d(0, 0, 1);

        faces.sort(Comparator.comparingDouble(o -> -mat.transformPosition(o.getCentroid()).length()));
//        faces.sort(Comparator.comparingDouble(o -> mat.transformDirection(o.getNormal()).dot(forwardVector)));

        mat.transformDirection(forwardVector);
        forwardVector.normalize();

        for (Face3D face : faces) {

//            Polygon p = new Polygon(new int[] {0, (int) INT_SCALE, 0}, new int[] {0, 0, (int) INT_SCALE}, 3);
            Polygon p = face.getUV();

            Vector3d rotNormal = mat2.transformDirection(face.getNormal()).normalize();

            Vector3d toLightVector = new Vector3d(1, 1, 2).normalize();

            double light = Math.abs(rotNormal.dot(toLightVector));

            double min = 0.6;

            double a = light * (1 - min) + min;

            AffineTransform af = g2.getTransform();

            try {
                AffineTransform faceTransform = Utils.getTransform(face, mat);
                g2.transform(faceTransform);
            }
            catch (NoninvertibleTransformException e) {
                continue;
            }

            g2.setPaint(face.getMaterial().getPaint());
            g2.fill(p);
//            g2.setColor(new Color(0, 0, 0, 1 - (float) a));
//            g2.fill(p);
            g2.setTransform(af);
        }

    }
}
