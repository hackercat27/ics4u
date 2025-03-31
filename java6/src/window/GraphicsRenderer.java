package window;

import geom.Camera3D;
import geom.Face3D;
import geom.Line3D;
import geom.Shape3D;
import geom.ShapeGenerator;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.TexturePaint;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.function.Consumer;
import javax.imageio.ImageIO;
import org.joml.Matrix4d;
import org.joml.Quaterniond;
import org.joml.Vector3d;
import org.joml.Vector4d;
import util.Utils;


public class GraphicsRenderer {

    public static final double INT_SCALE = Integer.MAX_VALUE;

    final List<Line3D> lines = new ArrayList<>();
    final List<Shape3D> shapes = new ArrayList<>();
    Camera3D camera;

    BufferedImage texture;


    public static void forEach(InputStream in, String separator, Consumer<String> sectionConsumer) {
        forEach(new InputStreamReader(in), separator, sectionConsumer);
    }

    public static void forEach(Reader r, String separator, Consumer<String> sectionConsumer) {
        if (r == null) {
            return;
        }
        Scanner scan = new Scanner(r);
        scan.useDelimiter(separator);

        while (scan.hasNext()) {
            sectionConsumer.accept(scan.next());
        }
    }

    int alpha = 0xB0 << 24;
    int bits = 0xFFFFFF;

    private Color lineColor = new Color(0xFF585256);

    Shape3D tet = (ShapeGenerator.tetrahedron(
            new Color(new Color(0xa83dfd).getRGB() & bits | alpha, true)));
    Shape3D oct = (ShapeGenerator.octahedron(
            new Color(new Color(0x8d56fc).getRGB() & bits | alpha, true)));
    Shape3D ico = (ShapeGenerator.icosahedron(
            new Color(new Color(0x15ECBA).getRGB() & bits | alpha, true)));
    Shape3D cub = (ShapeGenerator.cube(
            new Color(new Color(0xfd219d).getRGB() & bits | alpha, true)));
    Shape3D dod = (ShapeGenerator.dodecahedron(
            new Color(new Color(0xFF4710).getRGB() & bits | alpha, true)));

    public GraphicsRenderer() {

        try {
            texture = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/image.jpg")));
        }
        catch (IOException | NullPointerException ignored) {
            System.out.println("could not get texture file");
        }

        camera = new Camera3D();



        tet.scale = 2;
        oct.scale = 2;

        shapes.add(tet);
        shapes.add(oct);
        shapes.add(ico);
        shapes.add(cub);
        shapes.add(dod);
    }

    double time = 0;

    public void update(double deltaTime) {
        time += deltaTime;

        synchronized (shapes) {

            positions(cub, "java6/positions_cube.txt");
            positions(ico, "java6/positions_ico.txt");
            positions(dod, "java6/positions_dodec.txt");
            positions(oct, "java6/positions_oct.txt");
            positions(tet, "java6/positions_tetra.txt");

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
            forEach(fis, ",", num -> {
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
            forEach(fis, ",", num -> {
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

        if (texture != null) {
            double scale = (double) buffer.getHeight() / texture.getHeight();
            int x = (int) (buffer.getWidth() / 2d - (scale * texture.getWidth() / 2));
            int y = 0;
            g2.drawImage(texture, x, y, (int) (texture.getWidth() * scale), (int) (texture.getHeight() * scale), null);
        }

        double ratio = (double) buffer.getWidth() / buffer.getHeight();
        double scale = buffer.getHeight();

        // translate graphics2d to adhere to NDC instead of the default coord space
        g2.scale(buffer.getWidth(), -buffer.getHeight());
        g2.translate(0.5, -0.5);

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(new Color(0xf1edea));
//        g2.fillRect((int) -ratio, -1, (int) (ratio * 2), 2);

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
                new Color(0xA0D7D0CD, true),
                new Color(0x9F948C)}));
        g2.fillRect((int) -ratio, -1, (int) (ratio * 2), 2);
        g2.scale(1 / INT_SCALE, 1 / INT_SCALE);

        for (Shape3D shape : shapes) {

            Matrix4d objectTransform = new Matrix4d();
            objectTransform.translate(shape.position);
            objectTransform.rotate(shape.rotation);
            objectTransform.scale(shape.scale);

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
