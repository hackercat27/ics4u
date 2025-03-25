package window;

import geom.Face3D;
import geom.Shape3D;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.joml.Matrix4d;
import org.joml.Quaterniond;
import org.joml.Vector3d;


public class GraphicsRenderer {

    Shape3D[] shapes;

    public GraphicsRenderer() {

        shapes = new Shape3D[1];
        shapes[0] = new Shape3D(Color.MAGENTA, new Vector3d[]{
                new Vector3d(1, 1, 0), new Vector3d(1, 0, 0), new Vector3d(0, 1, 0)
        }, new int[] {
                0, 1, 2, Shape3D.INDEX_SEPARATOR
        });

    }

    double time = 0;

    public void update(double deltaTime) {
        time += deltaTime;

        for (Shape3D shape : shapes) {
            shape.rotation.set(new Quaterniond());
            shape.rotation.rotateAxis(time, 1, 0, 0);
        }
    }

    public void render(BufferedImage buffer, double interp) {

//        double time = System.currentTimeMillis() / 1000d;

        Graphics2D g2 = buffer.createGraphics();
        // translate graphics2d to centered origin rather than top left origin,
        // as well as invert y coordinate
//        g2.translate(buffer.getWidth() / 2, buffer.getHeight() / 2);
//        g2.scale(1, -1);

        double ratio = (double) buffer.getWidth() / buffer.getHeight();
        double scale = buffer.getHeight();

        Matrix4d projection = new Matrix4d()
                .perspective(Math.toRadians(90), ratio, 0.1, 1000);
        Matrix4d coordSpace = new Matrix4d();
        coordSpace.scale(scale * ratio, scale, scale);

        for (Shape3D shape : shapes) {

            Matrix4d transform = new Matrix4d();
            transform.translate(shape.position);
            transform.rotate(shape.rotation);
            transform.scale(shape.scale);

            Matrix4d product = new Matrix4d()
                    .mul(projection)
                    .mul(coordSpace)
                    .mul(transform)
                    ;

            List<Face3D> faces = new ArrayList<>(List.of(shape.getFaces()));

            faces.sort(Comparator.comparingDouble(o -> o.getNormal().z()));

            g2.setColor(Color.MAGENTA);

            for (Face3D face : faces) {
                g2.fill(face.transform(product));
            }
        }

        // similar to scanner.close(), we're responsible to release these resources
        // (this prevents a memory leak from creating a new graphics object every frame
        g2.dispose();
    }

}
