package geom;

import java.awt.Color;
import java.awt.Paint;
import java.awt.Polygon;
import java.awt.TexturePaint;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import org.joml.Matrix4d;
import org.joml.Vector2d;
import org.joml.Vector3d;
import org.joml.Vector4d;
import util.Utils;
import window.GraphicsRenderer;

public class Face3D {

    private Vector3d[] points;
    private Vector2d[] uvs;
    private BufferedImage texture;
    private Vector4d color;

    public Face3D(Vector3d[] points, Vector2d[] uvs) {
        this.points = points;
        this.uvs = uvs;

        for (int i = 0; i < uvs.length; i++) {
            if (uvs[i] == null) {
                uvs[i] = switch(i) {
                    case 1 -> new Vector2d(1, 0);
                    case 2 -> new Vector2d(0, 1);
                    default -> new Vector2d(0, 0);
                };
            }
        }

        if (points.length < 3) {
            System.out.println("Warning: created degenerate 3d face with less than 3 vertices!");
        }
    }

    public Vector3d[] getPoints() {
        return points;
    }

    public Vector2d[] getUVS() {
        return uvs;
    }

    public Vector3d getNormal() {

        Vector3d last = new Vector3d(points[points.length - 1]);
        Vector3d origin = new Vector3d(points[0]);
        Vector3d first = new Vector3d(points[1]);

        first.sub(origin);
        last.sub(origin);

        // cross product!!!!
        Vector3d normal = last.cross(first);
        normal.normalize();

        return normal;
    }

    public Vector3d getCentroid() {
        Vector3d sum = new Vector3d();
        for (Vector3d point : points) {
            sum.add(point);
        }
        sum.div(points.length);
        return sum;
    }
    
    public Polygon transform(Matrix4d transform) {

        int[] x = new int[points.length];
        int[] y = new int[points.length];

        for (int i = 0; i < points.length; i++) {
            Vector3d point = new Vector3d(points[i]);
            transform.transformPosition(point);
            point.div(point.z);

            // see GraphicsRenderer.render for why we need to multiply by INT_SCALE here
            x[i] = (int) (point.x * GraphicsRenderer.INT_SCALE);
            y[i] = (int) (point.y * GraphicsRenderer.INT_SCALE);
        }

        return new Polygon(x, y, points.length);
    }

    public Polygon getUV() {

        int[] x = new int[uvs.length];
        int[] y = new int[uvs.length];

        for (int i = 0; i < uvs.length; i++) {
            Vector2d point = uvs[i];

            // see GraphicsRenderer.render for why we need to multiply by INT_SCALE here
            x[i] = (int) (point.x * GraphicsRenderer.INT_SCALE);
            y[i] = (int) (point.y * GraphicsRenderer.INT_SCALE);
        }

        return new Polygon(x, y, uvs.length);
    }

    public Paint getPaint() {
        if (texture != null) {
            final double scale = GraphicsRenderer.INT_SCALE;
            return new TexturePaint(texture, new Rectangle2D.Double(0, 0, scale, scale));
        }
        return Utils.getColor(color);
    }

}
