package geom;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import org.joml.Quaterniond;
import org.joml.Vector2d;
import org.joml.Vector3d;

public class Shape3D {

    public static final int INDEX_SEPARATOR = -1;

    private Color color;
    private Vector3d[] points;
    private Vector2d[] uvs;
    private int[] indices;

    public Vector3d position = new Vector3d();
    public Quaterniond rotation = new Quaterniond();
    public double scale = 1;

    public Shape3D(Color color, Vector3d[] points, Vector2d[] uvs, int[] indices) {
        this.color = color;
        this.points = points;
        this.uvs = uvs;
        this.indices = indices;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Vector3d[] getPoints() {
        return points;
    }

    public Color getColor() {
        return color;
    }

    public int[] getIndices() {
        return indices;
    }

    public Face3D[] getFaces() {
        List<Face3D> faces = new ArrayList<>();

        List<Vector3d> facePoints = new ArrayList<>();
        List<Vector2d> faceUvs = new ArrayList<>();

        for (int i = 0; i < indices.length; i++) {
            int index = indices[i];
            boolean isLast = i >= indices.length - 1;

            if (index >= 0 && index < points.length) {
                facePoints.add(points[index]);
                faceUvs.add(uvs[index]);
            }
            if (index == INDEX_SEPARATOR || isLast) {
                faces.add(new Face3D(facePoints.toArray(new Vector3d[0]), faceUvs.toArray(new Vector2d[0])));
                facePoints.clear();
                faceUvs.clear();
            }


        }

        return faces.toArray(new Face3D[0]);
    }

    public Vector3d getPosition() {
        return position;
    }

    public Quaterniond getRotation() {
        return rotation;
    }

    public double getScale() {
        return scale;
    }
}
