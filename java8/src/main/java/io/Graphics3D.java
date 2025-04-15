package io;

import geom.Face3D;
import geom.Shape3D;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.joml.Matrix4d;
import org.joml.Quaterniond;
import org.joml.Vector3d;
import util.Utils;

public class Graphics3D {

    public static final double INT_SCALE = Integer.MAX_VALUE;

    private final List<Face3D> drawQueue = new ArrayList<>();

    public void draw(Shape3D shape, Vector3d pos, Quaterniond rotation, double scale) {

        Matrix4d transform = new Matrix4d()
                .translate(pos)
                .rotate(rotation)
                .scale(scale);

        Face3D[] faces = shape.getFaces();

        for (Face3D face : faces) {
            for (Vector3d point : face.getPoints()) {
                transform.transformPosition(point);
            }
        }

        drawQueue.addAll(List.of(faces));
    }

    public void render(Graphics2D g2, Matrix4d transform, Matrix4d projection) {

        List<Face3D> faces;

        synchronized (drawQueue) {
            faces = new ArrayList<>(drawQueue);
            drawQueue.clear();
        }


        Matrix4d mat = new Matrix4d()
                .mul(projection)
                .mul(transform);

        Vector3d forwardVector = new Vector3d(0, 0, 1);

        // backface culling
        faces.removeIf(face -> mat.transformDirection(face.getNormal()).dot(forwardVector) < 0);

        faces.sort(Comparator.comparingDouble(o -> -mat.transformPosition(o.getCentroid()).length()));
//        drawQueue.sort(Comparator.comparingDouble(o -> mat.transformDirection(o.getNormal()).dot(forwardVector)));

        mat.transformDirection(forwardVector);
        forwardVector.normalize();

        for (Face3D face : faces) {

            Polygon p = face.getUV();

            Vector3d rotNormal = transform.transformDirection(face.getNormal()).normalize();

            Vector3d toLightVector = new Vector3d(1, 1, 2).normalize();

            double light = Math.abs(rotNormal.dot(toLightVector));

            double min = 0.6;

            double a = light * (1 - min) + min;

            AffineTransform af = g2.getTransform();

            try {
                AffineTransform faceTransform = Utils.getTransform(face, mat);
                if (faceTransform == null) {
                    continue;
                }
                g2.transform(faceTransform);
            }
            catch (NoninvertibleTransformException e) {
                continue;
            }

            g2.setPaint(face.getMaterial().getPaint());
            g2.fill(p);
            g2.setColor(new Color(0, 0, 0, 1 - (float) a));
            g2.fill(p);
            g2.setTransform(af);
        }

    }

}
