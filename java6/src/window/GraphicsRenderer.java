package window;

import geom.Camera3D;
import geom.Face3D;
import geom.Shape3D;
import geom.ShapeGenerator;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.joml.Matrix4d;
import org.joml.Quaterniond;
import org.joml.Vector3d;
import util.Utils;


public class GraphicsRenderer {

    public static final double INT_SCALE = Integer.MAX_VALUE;

    Shape3D[] shapes;
    Camera3D camera;

    public GraphicsRenderer() {

        camera = new Camera3D();

        shapes = new Shape3D[1];
        shapes[0] = ShapeGenerator.tetrahedron();

    }

    double time = 0;

    public void update(double deltaTime) {
        time += deltaTime;


        for (Shape3D shape : shapes) {
            shape.rotation.set(new Quaterniond());
            shape.rotation.rotateAxis(time, 1, 0, 0);
            shape.rotation.rotateAxis(time / Math.PI, 0, 0, 1);
//            shape.rotation.rotateAxis(Math.PI / 8, 0, 0, 1);
//            shape.rotation.rotateAxis(Math.PI / 8, 0, 1, 0);
            shape.position.set(0, 0, 0);
        }

        camera.update(deltaTime);
    }

    public void render(BufferedImage buffer, double interp) {

        Graphics2D g2 = buffer.createGraphics();

        double ratio = (double) buffer.getWidth() / buffer.getHeight();
        double scale = buffer.getHeight();

        // translate graphics2d to adhere to NDC instead of the default coord space
        g2.scale(buffer.getWidth(), -buffer.getHeight());
        g2.translate(0.5, -0.5);

        // the java graphics api expects integer values, but NDC expects to use
        // floating point values from 0 to 1 - so we need to essentially use
        // integers as a fraction, ie: 'int a' can represent a float by treating it as 'a / 2147483647'
        // which will always be in the range of [-1, 1] which should be good enough
        g2.scale(1 / INT_SCALE, 1 / INT_SCALE);

        double near = 0.01;
        double far = 10000;

        Matrix4d perspectiveTransform = new Matrix4d()
                .perspective(camera.getFOV(interp), ratio, near, far);
        Matrix4d cameraTransform = new Matrix4d();
        cameraTransform.rotate(camera.getRotation(interp));
        cameraTransform.translate(camera.getPosition(interp));

        for (Shape3D shape : shapes) {

            Matrix4d objectTransform = new Matrix4d();
            objectTransform.translate(shape.position);
            objectTransform.rotate(shape.rotation);
            objectTransform.scale(shape.scale);

            Matrix4d mat = new Matrix4d()
                    .mul(perspectiveTransform)
                    .mul(cameraTransform)
                    .mul(objectTransform);

            List<Face3D> faces = new ArrayList<>(List.of(shape.getFaces()));

            // sorting by the normal vector like this only works because
            // we're assuming all of our meshes are convex
            // (which they are because they're just platonic solids)
//            faces.sort(Comparator.comparingDouble(o -> -mat.transformDirection(o.getNormal()).z));
            faces.sort(Comparator.comparingDouble(o -> -mat.transformPosition(o.getCentroid()).z));

            g2.setColor(new Color(0xABEC0480, true));

            for (Face3D face : faces) {
                Polygon p = face.transform(mat);

                Vector3d normal = face.getNormal();
                g2.setColor(Utils.getColor(normal));

                AffineTransform af = g2.getTransform();
                g2.fill(p);
                g2.setTransform(af);
            }
        }

        // similar to scanner.close(), we're responsible to release these resources
        // (this prevents a memory leak from creating a new graphics object every frame
        g2.dispose();
    }

}
