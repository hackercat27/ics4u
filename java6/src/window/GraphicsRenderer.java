package window;

import geom.Face3D;
import geom.Shape3D;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.joml.Matrix4d;
import org.joml.Quaterniond;
import org.joml.Vector3d;


public class GraphicsRenderer {

    public static final double GRAPHICS_SCALE = Integer.MAX_VALUE;

    Shape3D[] shapes;

    public GraphicsRenderer() {

        double b = 0.5;

        shapes = new Shape3D[1];
        shapes[0] = new Shape3D(Color.MAGENTA, new Vector3d[]{
                new Vector3d(b, b, b),
                new Vector3d(-b, b, b),
                new Vector3d(b, -b, b),
                new Vector3d(-b, -b, b),
                new Vector3d(b, b, -b),
                new Vector3d(-b, b, -b),
                new Vector3d(b, -b, -b),
                new Vector3d(-b, -b, -b)
        }, new int[] {
                0, 1, 3, 2, Shape3D.INDEX_SEPARATOR
        });

    }

    double time = 0;

    public void update(double deltaTime) {
        time += deltaTime;

        for (Shape3D shape : shapes) {
            shape.rotation.set(new Quaterniond());
            shape.rotation.rotateAxis(time, 1, 0, 0);
            shape.rotation.rotateAxis(time / Math.PI, 0, 0, 1);
            shape.position.set(0, 0, -5);
        }
    }

    public void render(BufferedImage buffer, double interp) {

//        double time = System.currentTimeMillis() / 1000d;

        Graphics2D g2 = buffer.createGraphics();
        // translate graphics2d to centered origin rather than top left origin,
        // as well as invert y coordinate


        double ratio = (double) buffer.getWidth() / buffer.getHeight();
        double scale = buffer.getHeight();

        g2.scale(buffer.getWidth(), -buffer.getHeight());
        g2.translate(0.5, -0.5);
        g2.scale(1 / GRAPHICS_SCALE, 1 / GRAPHICS_SCALE);

        double near = 0.01;
        double far = 10000;

        Matrix4d projection = new Matrix4d()
                .perspective(Math.toRadians(90), ratio, near, far)
                ;
        Matrix4d camera = new Matrix4d();
        camera.translate(Math.sin(time), 0, 0);

        for (Shape3D shape : shapes) {

            Matrix4d transform = new Matrix4d();
            transform.translate(shape.position);
            transform.rotate(shape.rotation);
            transform.scale(shape.scale);

            Matrix4d mat = new Matrix4d()
                    .mul(projection)
                    .mul(camera)
                    .mul(transform);

            List<Face3D> faces = new ArrayList<>(List.of(shape.getFaces()));

            faces.sort(Comparator.comparingDouble(o -> o.getNormal().z()));

            g2.setColor(Color.MAGENTA);

            for (Face3D face : faces) {
                Polygon p = face.transform(mat);

                for (int i = 0; i < p.npoints; i++) {
                    System.out.printf("[%d, %d] ", p.xpoints[i], p.ypoints[i]);
                }
                System.out.print("\r");

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
