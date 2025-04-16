package util;

import geom.Face3D;
import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import org.joml.Matrix4d;
import org.joml.Quaterniond;
import org.joml.Vector2d;
import org.joml.Vector3d;
import org.joml.Vector4d;
import window.GraphicsRenderer;

public class Utils {

    public static void sleep(double nanos) {

        long millis = (long) (nanos / 1e6);

        int nanosMod = (int) (nanos % 1e6);

        try {
            Thread.sleep(millis, nanosMod);
        }
        catch (InterruptedException ignored) {}
    }


    public static Vector4d getColor(Color color) {
        if (color == null) {
            return new Vector4d(1, 1, 1, 1);
        }
        return new Vector4d(color.getRed() / 255d,
                            color.getGreen() / 255d,
                            color.getBlue() / 255d,
                            color.getAlpha() / 255d);
    }

    public static Color getColor(Vector3d vec) {
        if (vec == null) {
            return Color.WHITE;
        }
        return new Color(
                (float) Math.min(1, Math.abs(vec.x)),
                (float) Math.min(1, Math.abs(vec.y)),
                (float) Math.min(1, Math.abs(vec.z)));
    }

    public static Color getColor(Vector3d vec, double alpha) {
        if (vec == null) {
            return Color.WHITE;
        }
        return new Color(
                (float) Math.min(1, Math.abs(vec.x)),
                (float) Math.min(1, Math.abs(vec.y)),
                (float) Math.min(1, Math.abs(vec.z)),
                (float) Math.min(1, Math.abs(alpha)));
    }

    public static Color getColor(Vector4d vec) {
        if (vec == null) {
            return Color.WHITE;
        }
        return new Color(
                (float) Math.min(1, Math.abs(vec.x)),
                (float) Math.min(1, Math.abs(vec.y)),
                (float) Math.min(1, Math.abs(vec.z)),
                (float) Math.min(1, Math.abs(vec.w)));
    }

    public static Matrix4d getTransform(Vector3d position, Quaterniond rotation, double scale) {
        return new Matrix4d()
                .translate(position)
                .rotate(rotation)
                .scale(scale);
    }

    public static Matrix4d getCameraTransform(Vector3d position, Quaterniond rotation, double scale) {
        return new Matrix4d()
                .scale(scale)
                .rotate(rotation)
                .translate(position);
    }

    public static AffineTransform getTransform(Face3D face, Matrix4d transform) throws NoninvertibleTransformException {

        Vector3d[] points = new Vector3d[3];
        Vector2d[] transformedPoints = new Vector2d[3];
        Vector2d[] uvPoints = new Vector2d[3];

        System.arraycopy(face.getPoints(), 0, points, 0, points.length);
        System.arraycopy(face.getUVS(), 0, uvPoints, 0, points.length);

        for (int i = 0; i < transformedPoints.length; i++) {

            Vector3d vec = transform.transformPosition(points[i], new Vector3d());
            if (vec.z < 0) {
                return null;
            }
            vec.div(vec.z);
            transformedPoints[i] = new Vector2d(vec);
        }

        AffineTransform uvsToUnit = getTransform(uvPoints[0], uvPoints[1], uvPoints[2]);
        AffineTransform pointsToUnit = getTransform(transformedPoints[0], transformedPoints[1], transformedPoints[2]);

        uvsToUnit.invert();
        uvsToUnit.concatenate(pointsToUnit);
        uvsToUnit.invert();

        return uvsToUnit;
    }

    public static AffineTransform getTransform(Vector2d desA, Vector2d desB, Vector2d desC) {

        AffineTransform transform = new AffineTransform();

        Vector2d dA = new Vector2d(desA);
        Vector2d dB = new Vector2d(desB);
        Vector2d dC = new Vector2d(desC);

        // do all steps in reverse order with the AffineTransform compared to
        // the JOML vectors because of differing implementations between the libraries

        Vector2d offset = new Vector2d().sub(dA);

        dA.add(offset);
        dB.add(offset);
        dC.add(offset);

        double theta = -Math.atan2(dB.y, dB.x);
        dA.set(new Vector3d(dA, 0).rotateAxis(theta, 0, 0, 1));
        dB.set(new Vector3d(dB, 0).rotateAxis(theta, 0, 0, 1));
        dC.set(new Vector3d(dC, 0).rotateAxis(theta, 0, 0, 1));

        double xScale = 1 / dB.x;
        dA.x *= xScale;
        dB.x *= xScale;
        dC.x *= xScale;

        double yScale = 1 / dC.y;
        dA.y *= yScale;
        dB.y *= yScale;
        dC.y *= yScale;

        double xShear = -dC.x;
        dA.x += dA.y * xShear;
        dB.x += dB.y * xShear;
        dC.x += dC.y * xShear;

        transform.shear(xShear, 0);

        transform.scale(xScale, yScale);

        transform.rotate(theta);

        transform.translate(offset.x * GraphicsRenderer.INT_SCALE, offset.y * GraphicsRenderer.INT_SCALE);

        return transform;

    }

}
