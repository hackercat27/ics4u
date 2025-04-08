package util;

import org.joml.Quaterniond;
import org.joml.Vector3d;

public class AnimationTimer {

    public class Frame {
        public Vector3d position = new Vector3d();
        public Quaterniond rotation = new Quaterniond();
        public double scale = 1;

        public Frame(Vector3d position, Quaterniond rotation, double scale) {
            this.position.set(position);
            this.rotation.set(rotation);
            this.scale = scale;
        }
    }

    private double tps;
    private Frame[] frames;

    private double time;

    public AnimationTimer(double tps, Frame[] frames) {
        this.tps = tps;
        this.frames = frames;
    }

    public void update(double deltaTime) {
        time += deltaTime;
    }

    public Frame getCurrentFrame() {
        int i = (int) (time * tps);
        if (frames == null || i < 0 || i >= frames.length) {
            return null;
        }
        return frames[i];
    }



}
