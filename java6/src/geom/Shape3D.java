package geom;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import org.joml.Quaterniond;
import org.joml.Vector3d;

public class Shape3D {

    public static final int INDEX_SEPARATOR = -1;

    private Vector3d[] points;
    private Color color;
    private int[] indices;

    public Vector3d position = new Vector3d();
    public Quaterniond rotation = new Quaterniond();
    public double scale = 1;

    public Shape3D(Color color, Vector3d[] points, int[] indices) {
        this.color = color;
        this.points = points;
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

        List<Vector3d> faceBuilder = new ArrayList<>();

        for (int i = 0; i < indices.length; i++) {
            int index = indices[i];
            boolean isLast = i >= indices.length - 1;

            if (index == INDEX_SEPARATOR || isLast) {
                faces.add(new Face3D(faceBuilder.toArray(new Vector3d[0])));
                faceBuilder.clear();
            }
            else {
                faceBuilder.add(points[index]);
            }

        }

        return faces.toArray(new Face3D[0]);
    }
}
