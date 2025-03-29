package util;

import java.awt.Color;
import org.joml.Vector3d;
import org.joml.Vector4d;

public class Utils {

    public static void sleep(double nanos) {

        long millis = (long) (nanos / 1e6);

        int nanosMod = (int) (nanos % 1e6);

        try {
            Thread.sleep(millis, nanosMod);
        }
        catch (InterruptedException ignored) {}
    }


    public static Vector4d getColor(Color color) {
        return new Vector4d(color.getRed() / 255d,
                            color.getGreen() / 255d,
                            color.getBlue() / 255d,
                            color.getAlpha() / 255d);
    }

    public static Color getColor(Vector3d vec) {
        return new Color(
                (float) Math.min(1, Math.abs(vec.x)),
                (float) Math.min(1, Math.abs(vec.y)),
                (float) Math.min(1, Math.abs(vec.z)));
    }

    public static Color getColor(Vector3d vec, double alpha) {
        return new Color(
                (float) Math.min(1, Math.abs(vec.x)),
                (float) Math.min(1, Math.abs(vec.y)),
                (float) Math.min(1, Math.abs(vec.z)),
                (float) Math.min(1, Math.abs(alpha)));
    }

    public static Color getColor(Vector4d vec) {
        return new Color(
                (float) Math.min(1, Math.abs(vec.x)),
                (float) Math.min(1, Math.abs(vec.y)),
                (float) Math.min(1, Math.abs(vec.z)),
                (float) Math.min(1, Math.abs(vec.w)));
    }
}
