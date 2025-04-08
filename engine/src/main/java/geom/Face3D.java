package geom;

import java.awt.Polygon;
import org.joml.Matrix4d;
import org.joml.Vector2d;
import org.joml.Vector3d;
import window.GraphicsRenderer;

public class Face3D {

    private Vector3d[] points;
    private Vector2d[] uvs;
    private Material material;

    public Face3D(Material material, Vector3d[] points, Vector2d[] uvs) {
        this.points = points;
        this.uvs = uvs;
        this.material = material;
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

    public Material getMaterial() {
        return material;
    }



}
