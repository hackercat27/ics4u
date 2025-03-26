package geom;

import java.awt.Color;
import org.joml.Vector3d;

public class ShapeGenerator {

    private ShapeGenerator() {}

    public static Shape3D tetrahedron() {

        double b = 1;
        double r = b / Math.sin(Math.PI / 3);
        double a = r * Math.cos(Math.PI / 3);

        // refer to diagram for a, b, r definitions

        return new Shape3D(Color.RED, new Vector3d[] {
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

    public static Shape3D octahedron() {
        return null;
    }

    public static Shape3D icosahedron() {
        return null;
    }

    public static Shape3D cube() {
        return new Shape3D(Color.MAGENTA, new Vector3d[]{
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
                2, 6, 7, 3, Shape3D.INDEX_SEPARATOR // bottom face
        });
    }

    public static Shape3D dodecahedron() {
        return null;
    }

}
