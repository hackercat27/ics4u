package geom;

import java.util.ArrayList;
import java.util.List;
import org.joml.Quaterniond;
import org.joml.Vector2d;
import org.joml.Vector3d;

public class Shape3D {

    public static final int INDEX_SEPARATOR = -1;

    private Material material;
    private Vector3d[] points;
    private Vector2d[] uvs;
    private int[] indices;

    public Vector3d position = new Vector3d();
    public Vector3d lastPosition = new Vector3d();
    public Quaterniond rotation = new Quaterniond();
    public double scale = 1;

    public Shape3D(Material material, Vector3d[] points, Vector2d[] uvs, int[] indices) {
        this.material = material;
        this.points = points;
        this.uvs = uvs;
        this.indices = indices;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public Vector3d[] getPoints() {
        return points;
    }

    public Material getMaterial() {
        return material;
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
                facePoints.add(new Vector3d(points[index]));
                faceUvs.add(new Vector2d(uvs[index]));
            }
            if (index == INDEX_SEPARATOR || isLast) {
                faces.add(new Face3D(material, facePoints.toArray(new Vector3d[0]), faceUvs.toArray(new Vector2d[0])));
                facePoints.clear();
                faceUvs.clear();
            }
        }

        return faces.toArray(new Face3D[0]);
    }

    public void update(double deltaTime) {
        lastPosition.set(position);
    }

    public Vector3d getPosition(double t) {
        return new Vector3d(lastPosition).lerp(position, t);
    }

    public Quaterniond getRotation() {
        return rotation;
    }

    public double getScale() {
        return scale;
    }
}
