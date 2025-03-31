package geom;

import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.Line2D;
import org.joml.Matrix4d;
import org.joml.Vector3d;
import window.GraphicsRenderer;

public class Line3D {

    private final Vector3d a = new Vector3d();
    private final Vector3d b = new Vector3d();

    public Line3D(Vector3d a, Vector3d b) {
        this.a.set(a);
        this.b.set(b);
        this.b.normalize();
    }

    public Vector3d getA() {
        return a;
    }

    public Vector3d getB() {
        return b;
    }

    public Line2D transform(Matrix4d transform) {
        Vector3d transformedA = new Vector3d(this.a);
        transform.transformPosition(transformedA);
        transformedA.div(transformedA.z);

        Vector3d transformedB = new Vector3d(this.b);
        transform.transformPosition(transformedB);
        transformedB.div(transformedB.z);

        // see GraphicsRenderer.render for why we need to multiply by INT_SCALE here
        return new Line2D.Double(transformedA.x * GraphicsRenderer.INT_SCALE,
                                 transformedA.y * GraphicsRenderer.INT_SCALE,
                                 transformedB.x * GraphicsRenderer.INT_SCALE,
                                 transformedB.y * GraphicsRenderer.INT_SCALE);
    }
}
