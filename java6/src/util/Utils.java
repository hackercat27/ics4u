package util;

import java.awt.Color;
import org.joml.Vector3d;

public class Utils {

    public static void sleep(double nanos) {

        long millis = (long) (nanos / 1e6);

        int nanosMod = (int) (nanos % 1e6);

        try {
            Thread.sleep(millis, nanosMod);
        }
        catch (InterruptedException ignored) {}
    }


    public static Color getColor(Vector3d vec) {
        return new Color(
                (float) Math.min(1, Math.abs(vec.x)),
                (float) Math.min(1, Math.abs(vec.y)),
                (float) Math.min(1, Math.abs(vec.z)));
    }
}
