package geom;

import org.joml.Quaterniond;
import org.joml.Vector3d;

public class Camera3D {

    public Vector3d position = new Vector3d();
    public Quaterniond rotation = new Quaterniond();
    public double fov = Math.toRadians(80);

    private Vector3d lastPosition = new Vector3d();
    private Quaterniond lastRotation = new Quaterniond();
    private double lastFOV = fov;

    public Camera3D(Vector3d position, Quaterniond rotation, double fov) {
        this.position.set(position);
        this.rotation.set(rotation);
        this.fov = fov;
    }

    public Camera3D() {
    }


    private double time;
    public void update(double deltaTime) {
        lastRotation.set(rotation);
        lastPosition.set(position);
        lastFOV = fov;

        time += deltaTime;

        position.set(Math.sin(time) * 4, 0, -8 + Math.cos(time) * 4);
    }

    public void setAngles(double yaw, double pitch) {
        rotation.set(new Quaterniond());
        rotation.rotateAxis(pitch, 1, 0, 0);
        rotation.rotateAxis(yaw, 0, 1, 0);
    }

    public double getFOV(double t) {
        return org.joml.Math.lerp(lastFOV, fov, t);
    }

    public Quaterniond getRotation(double t) {
        return new Quaterniond(lastRotation).slerp(rotation, t);
    }

    public Vector3d getPosition(double t) {
        return new Vector3d(lastPosition).lerp(position, t);
    }
}
