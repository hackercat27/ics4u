package geom;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.joml.Vector3d;

public class Shape3DFactory {

    private Shape3DFactory() {}

    public static Shape3D tetrahedron(Color color) {

        double b = 1;
        double r = b / Math.sin(Math.PI / 3);
        double a = r * Math.cos(Math.PI / 3);

        // refer to diagram for a, b, r definitions

        return new Shape3D(color, new Vector3d[] {
                new Vector3d(-b, -a, -a), // A
                new Vector3d( b, -a, -a), // B
                new Vector3d( 0, -a,  r), // C
                new Vector3d( 0,  r,  0)  // D
        }, new int[] {
                0, 2, 1, Shape3D.INDEX_SEPARATOR,
                0, 1, 3, Shape3D.INDEX_SEPARATOR,
                0, 3, 2, Shape3D.INDEX_SEPARATOR,
                1, 2, 3, Shape3D.INDEX_SEPARATOR
        });

    }

    public static Shape3D cube(Color color) {
        return new Shape3D(color, new Vector3d[]{
                new Vector3d( 1,  1,  1), // 0 right top    back
                new Vector3d(-1,  1,  1), // 1 left  top    back
                new Vector3d( 1, -1,  1), // 2 right bottom back
                new Vector3d(-1, -1,  1), // 3 left  bottom back
                new Vector3d( 1,  1, -1), // 4 right top    front
                new Vector3d(-1,  1, -1), // 5 left  top    front
                new Vector3d( 1, -1, -1), // 6 right bottom front
                new Vector3d(-1, -1, -1)  // 7 left  bottom front
        }, new int[] {
                4, 5, 7, 6, Shape3D.INDEX_SEPARATOR, // front face
                1, 0, 2, 3, Shape3D.INDEX_SEPARATOR, // back face
                0, 4, 6, 2, Shape3D.INDEX_SEPARATOR, // right face
                1, 3, 7, 5, Shape3D.INDEX_SEPARATOR, // left face
                0, 1, 5, 4, Shape3D.INDEX_SEPARATOR, // top face
                2, 6, 7, 3, Shape3D.INDEX_SEPARATOR  // bottom face
        });
    }

    public static Shape3D octahedron(Color color) {
        Vector3d[] points = new Vector3d[] {
                new Vector3d( 1,  0,  0),
                new Vector3d(-1,  0,  0),
                new Vector3d( 0,  1,  0),
                new Vector3d( 0, -1,  0),
                new Vector3d( 0,  0,  1),
                new Vector3d( 0,  0, -1)
        };

        int[] indices = new int[] {
                0, 4, 2, Shape3D.INDEX_SEPARATOR,
                0, 3, 4, Shape3D.INDEX_SEPARATOR,
                0, 5, 3, Shape3D.INDEX_SEPARATOR,
                0, 2, 5, Shape3D.INDEX_SEPARATOR,
                1, 2, 4, Shape3D.INDEX_SEPARATOR,
                1, 4, 3, Shape3D.INDEX_SEPARATOR,
                1, 3, 5, Shape3D.INDEX_SEPARATOR,
                1, 5, 2, Shape3D.INDEX_SEPARATOR
        };

        return new Shape3D(color, points, indices);
    }

    public static Shape3D dodecahedron(Color color) {

        // this method is actualy such a mess
        // never write code like this
        // god i hope my teacher never sees this

        double phi = (1 + Math.sqrt(5)) / 2;
        double r = 1;
        double h = 1.3; // what the actual fuck is this number from????

        double dihedralAngle = Math.acos(-1/Math.sqrt(5));

        List<Vector3d> points = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();

        int edgeCount = 5;

        for (int k = 0; k < 2; k++) {

            double shellFlipAngle = k * Math.PI;

            for (int j = 0; j < edgeCount + 1; j++) {

                double verticalOffsetAngle = Math.PI - dihedralAngle; // supp angle theorem
                double extraAngle = 0;
                double faceAngle = Math.TAU * (double) j / edgeCount + shellFlipAngle;
                Vector3d faceCenterOffset = new Vector3d(r * phi / 2, 0, 0);
                Vector3d faceCenterVerticalOffset = new Vector3d(faceCenterOffset);

                faceCenterOffset.rotateAxis(faceAngle, 0, 1, 0);
                faceCenterVerticalOffset.rotateAxis(verticalOffsetAngle, 0, 0, 1);
                faceCenterVerticalOffset.rotateAxis(faceAngle, 0, 1, 0);

                boolean isMiddleFace = j == edgeCount;

                if (isMiddleFace) {
                    // add middle face
                    extraAngle = Math.PI + shellFlipAngle;
                    faceCenterOffset.set(0, 0, 0);
                    faceCenterVerticalOffset.set(0, 0, 0);
                    faceAngle = 0;
                    verticalOffsetAngle = 0;
                }

                for (int i = 0; i < edgeCount; i++) {

                    double pointAngle = Math.TAU * (double) i / edgeCount + extraAngle;

                    Vector3d point = new Vector3d(r, 0, 0);

                    point.rotateAxis(pointAngle, 0, 1, 0);
                    point.rotateAxis(verticalOffsetAngle, 0, 0, 1);
                    point.rotateAxis(faceAngle, 0, 1, 0);

                    point.add(faceCenterOffset);
                    point.add(faceCenterVerticalOffset);

                    point.add(0, -h, 0);
                    point.rotateAxis(shellFlipAngle, 1, 0, 0);

                    points.add(point);
                    indices.add(points.indexOf(point));

                }
                indices.add(Shape3D.INDEX_SEPARATOR);
            }
        }

        return new Shape3D(color, points.toArray(new Vector3d[0]), indices.stream().mapToInt(Integer::intValue).toArray());
    }

    public static Shape3D icosahedron(Color color) {

        List<Vector3d> points = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();

        Shape3D dual = dodecahedron(color);

        List<Vector3d> centroids = new ArrayList<>();

        List<Vector3d> dualPoints = new ArrayList<>();

        double EPSILON = 0.1;

        for (Vector3d point : dual.getPoints()) {
            boolean similarExists = false;

            for (Vector3d dualPoint : dualPoints) {
                if (new Vector3d(dualPoint).sub(point).length() < EPSILON) {
                    similarExists = true;
                    break;
                }
            }

            if (!similarExists) {
                dualPoints.add(point);
            }

        }

        for (Face3D face : dual.getFaces()) {
            centroids.add(face.getCentroid());
        }

        for (Vector3d dualPoint : dualPoints) {

            // sort by the distance to the current point on the dodecahedron
            centroids.sort(Comparator.comparingDouble(o -> new Vector3d(o).sub(dualPoint).length()));

            Face3D face = new Face3D(centroids.subList(0, 3).toArray(new Vector3d[0]));

            double direction = face.getNormal().dot(face.getCentroid());
            boolean flip = direction < 0;

            for (int i = 0; i < 3; i++) {
                Vector3d centroid = centroids.get(i);
                points.add(centroid);
                indices.add(points.indexOf(centroid));
            }
            if (flip) {
                indices = indices.reversed();
            }
            indices.add(Shape3D.INDEX_SEPARATOR);
        }


        return new Shape3D(color, points.toArray(new Vector3d[0]), indices.stream().mapToInt(Integer::intValue).toArray());
    }

}
