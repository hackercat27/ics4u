package window;

import geom.Camera3D;
import geom.Face3D;
import geom.Line3D;
import geom.Shape3D;
import geom.Shape3DFactory;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import javax.imageio.ImageIO;
import org.joml.Matrix4d;
import org.joml.Quaterniond;
import org.joml.Vector3d;
import org.joml.Vector4d;
import util.Utils;


public class GraphicsRenderer {

    public static final double INT_SCALE = Integer.MAX_VALUE;

    private final List<Line3D> lines = new ArrayList<>();
    private final List<Shape3D> shapes = new ArrayList<>();
    private Camera3D camera;

    private BufferedImage texture;

    private static final int SHAPE_ALPHA = 0xB0 << 24;
    private static final int COLOR_BITS = 0xFFFFFF;

    private static final Color lineColor = new Color(0xFF585256);

    private static final Color colorTetrahedron  = new Color(new Color(0xa83dfd).getRGB() & COLOR_BITS | SHAPE_ALPHA, true);
    private static final Color colorOctahedron   = new Color(new Color(0x8d56fc).getRGB() & COLOR_BITS | SHAPE_ALPHA, true);
    private static final Color colorIcosahedron  = new Color(new Color(0x15ECBA).getRGB() & COLOR_BITS | SHAPE_ALPHA, true);
    private static final Color colorCube         = new Color(new Color(0xfd219d).getRGB() & COLOR_BITS | SHAPE_ALPHA, true);
    private static final Color colorDodecahedron = new Color(new Color(0xFF4710).getRGB() & COLOR_BITS | SHAPE_ALPHA, true);

    private static final Shape3D tetrahedron  = Shape3DFactory.tetrahedron(colorTetrahedron);
    private static final Shape3D octahedron   = Shape3DFactory.octahedron(colorOctahedron);
    private static final Shape3D icosahedron  = Shape3DFactory.icosahedron(colorIcosahedron);
    private static final Shape3D cube         = Shape3DFactory.cube(colorCube);
    private static final Shape3D dodecahedron = Shape3DFactory.dodecahedron(colorDodecahedron);

    public GraphicsRenderer() {

        try {
            texture = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/image.jpg")));
        }
        catch (IOException | NullPointerException ignored) {
            System.out.println("could not get texture file");
        }

        camera = new Camera3D();

        tetrahedron.scale = 2;
        octahedron.scale = 2;

        shapes.add(tetrahedron);
        shapes.add(octahedron);
        shapes.add(icosahedron);
        shapes.add(cube);
        shapes.add(dodecahedron);
    }

    double time = 0;

    public void update(double deltaTime) {
        time += deltaTime;

        synchronized (shapes) {

            positions(cube, "java6/positions_cube.txt");
            positions(icosahedron, "java6/positions_ico.txt");
            positions(dodecahedron, "java6/positions_dodec.txt");
            positions(octahedron, "java6/positions_oct.txt");
            positions(tetrahedron, "java6/positions_tetra.txt");

            camera.update(deltaTime);
        }

        synchronized (lines) {
            positionsLines("java6/positions_lines.txt");
        }
    }

    private void positions(Shape3D shape, String filename) {

        InputStream fis;
        try {
            fis = new FileInputStream(filename);
        }
        catch (FileNotFoundException e) {
            fis = null;
        }

        if (fis != null) {
            List<Double> values = new ArrayList<>();
            Utils.forEach(fis, ",", num -> {
                try {
                    values.add(Double.parseDouble(num));
                }
                catch (NumberFormatException e) {
                    values.add(0d);
                }
            });

            try {
                shape.position.x = values.get(0);
                shape.position.y = values.get(1);
                shape.position.z = values.get(2);
                shape.rotation.set(new Quaterniond());
                shape.rotation.rotateAxis(Math.toRadians(values.get(4)), 1, 0, 0);
                shape.rotation.rotateAxis(Math.toRadians(values.get(3)), 0, 1, 0);
                shape.scale = values.get(5);
            }
            catch (IndexOutOfBoundsException ignored) {}
        }
    }

    private void positionsLines(String filename) {
        InputStream fis;
        try {
            fis = new FileInputStream(filename);
        }
        catch (FileNotFoundException e) {
            fis = null;
        }

        if (fis != null) {
            lines.clear();
            List<Double> values = new ArrayList<>();
            Utils.forEach(fis, ",", num -> {
                try {
                    values.add(Double.parseDouble(num));
                }
                catch (NumberFormatException e) {
                    values.add(0d);
                }
            });

            try {
                for (int i = 0; ; i += 6) {
                    lines.add(new Line3D(new Vector3d(values.get(0 + i), values.get(1 + i), values.get(2 + i)),
                                         new Vector3d(values.get(3 + i), values.get(4 + i), values.get(5 + i))
                    ));
                }
            }
            catch (IndexOutOfBoundsException ignored) {}
        }
    }

    public void render(BufferedImage buffer, double interp) {

        Graphics2D g2 = buffer.createGraphics();

//        if (texture != null) {
//            double scale = (double) buffer.getHeight() / texture.getHeight();
//            int x = (int) (buffer.getWidth() / 2d - (scale * texture.getWidth() / 2));
//            int y = 0;
//            g2.drawImage(texture, x, y, (int) (texture.getWidth() * scale), (int) (texture.getHeight() * scale), null);
//        }

        double ratio = (double) buffer.getWidth() / buffer.getHeight();
        double scale = buffer.getHeight();

        // translate graphics2d to adhere to NDC instead of the default coord space
        g2.scale(buffer.getWidth(), -buffer.getHeight());
        g2.translate(0.5, -0.5);

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(new Color(0xf1edea));
        g2.fillRect((int) -ratio, -1, (int) (ratio * 2), 2);

        // the java graphics api expects integer values, but NDC expects to use
        // floating point values from 0 to 1 - so we need to essentially use
        // integers as a fraction, ie: 'int a' can represent a float by treating it as 'a / 2147483647'
        // which will always be in the range of [-1, 1] which should be good enough
        g2.scale(1 / INT_SCALE, 1 / INT_SCALE);

        double near = 0.01;
        double far = 10000;

        Matrix4d perspectiveTransform = new Matrix4d()
                .perspective(camera.getFOV(interp), ratio, near, far);
        Matrix4d cameraTransform = Utils.getCameraTransform(camera.getPosition(interp), camera.getRotation(interp), 1);

        List<Shape3D> shapes;
        synchronized (this.shapes) {
            shapes = new ArrayList<>(this.shapes);
        }

        List<Line3D> lines;
        synchronized (this.lines) {
            lines = new ArrayList<>(this.lines);
        }

        shapes.sort(Comparator.comparingDouble(o -> o.getPosition().z));

        for (Line3D line : lines) {
            g2.setColor(lineColor);
            g2.setStroke(new BasicStroke((float) (1.5 * INT_SCALE / scale), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

            Matrix4d mat = new Matrix4d()
                    .mul(perspectiveTransform)
                    .mul(cameraTransform);

            Line2D l = line.transform(mat);
            g2.draw(l);
        }

        g2.scale(INT_SCALE, INT_SCALE);
        g2.setPaint(new RadialGradientPaint(0, 0, 0.8f, new float[] {0, 0.2f, 1}, new Color[] {
                new Color(0x00FFFFFF, true),
                new Color(0xF2D7D0CD, true),
                new Color(0x9F948C)}));
        g2.fillRect((int) -ratio, -1, (int) (ratio * 2), 2);
        g2.scale(1 / INT_SCALE, 1 / INT_SCALE);

        for (Shape3D shape : shapes) {

            Matrix4d objectTransform = Utils.getTransform(shape.position, shape.rotation, shape.scale);

            Matrix4d mat = new Matrix4d()
                    .mul(perspectiveTransform)
                    .mul(cameraTransform)
                    .mul(objectTransform);

            Matrix4d mat2 = new Matrix4d()
                    .mul(cameraTransform)
                    .mul(objectTransform);

            List<Face3D> faces = new ArrayList<>(List.of(shape.getFaces()));

            faces.sort(Comparator.comparingDouble(o -> -mat2.transformPosition(o.getCentroid()).length()));

            for (Face3D face : faces) {
                Polygon p = face.transform(mat);

                Vector3d rotNormal = mat2.transformDirection(face.getNormal()).normalize();

                double light = Math.abs(rotNormal.dot(new Vector3d(1, 1, 2).normalize()));

                double min = 0.6;

                double a = light * (1 - min) + min;

                Vector4d color = Utils.getColor(shape.getColor());

                color.x *= a;
                color.y *= a;
                color.z *= a;

                g2.setColor(Utils.getColor(color));

                AffineTransform af = g2.getTransform();
                g2.fill(p);
                g2.setColor(lineColor);
                g2.setStroke(new BasicStroke((float) (1.5 * INT_SCALE / scale), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.draw(p);
                g2.setTransform(af);
            }

        }

        // similar to scanner.close(), we're responsible to release these resources
        // (this prevents a memory leak from creating a new graphics object every frame
        g2.dispose();
    }

}
