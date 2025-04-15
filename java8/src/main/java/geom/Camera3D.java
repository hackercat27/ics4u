package geom;

import io.Input;
import org.joml.Quaterniond;
import org.joml.Vector3d;

public class Camera3D {

    public Vector3d position = new Vector3d();
    public Quaterniond rotation = new Quaterniond();
    public double fov = Math.toRadians(60);

    private Vector3d lastPosition = new Vector3d();
    private Quaterniond lastRotation = new Quaterniond();
    private double lastFOV = fov;

    public Camera3D(Vector3d position, Quaterniond rotation, double fov) {
        this.position.set(position);
        this.rotation.set(rotation);
        this.fov = fov;
    }

    public Camera3D() {
        Input.addBind("w", "move_w");
        Input.addBind("a", "move_a");
        Input.addBind("s", "move_s");
        Input.addBind("d", "move_d");

        Input.addBind("left", "look_left");
        Input.addBind("right", "look_right");
    }

    private double yaw, pitch;

    private double time;
    public void update(double deltaTime) {

        double speed = 1;
        double angleSpeed = Math.toRadians(110);

        lastRotation.set(rotation);
        lastPosition.set(position);
        lastFOV = fov;

        time += deltaTime;

        Vector3d delta = new Vector3d();

        if (Input.isActionPressed("look_right")) {
            yaw += angleSpeed * deltaTime;
        }
        if (Input.isActionPressed("look_left")) {
            yaw -= angleSpeed * deltaTime;
        }

        if (Input.isActionPressed("move_w")) {
            delta.z += 1;
        }
        if (Input.isActionPressed("move_s")) {
            delta.z -= 1;
        }
        if (Input.isActionPressed("move_a")) {
            delta.x += 1;
        }
        if (Input.isActionPressed("move_d")) {
            delta.x -= 1;
        }

        delta.rotateAxis(-yaw, 0, 1, 0);

        rotation.set(new Quaterniond());
        rotation.rotateAxis(yaw, 0, 1, 0);
        position.add(delta.mul(deltaTime).mul(speed));
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
