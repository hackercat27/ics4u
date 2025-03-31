package util;

import java.awt.Color;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Scanner;
import java.util.function.Consumer;
import org.joml.Matrix4d;
import org.joml.Quaterniond;
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

    public static Matrix4d getTransform(Vector3d position, Quaterniond rotation, double scale) {
        return new Matrix4d()
                .translate(position)
                .rotate(rotation)
                .scale(scale);
    }

    public static Matrix4d getCameraTransform(Vector3d position, Quaterniond rotation, double scale) {
        return new Matrix4d()
                .scale(scale)
                .rotate(rotation)
                .translate(position);
    }


    public static void forEach(InputStream in, String separator, Consumer<String> sectionConsumer) {
        forEach(new InputStreamReader(in), separator, sectionConsumer);
    }

    public static void forEach(Reader r, String separator, Consumer<String> sectionConsumer) {
        if (r == null) {
            return;
        }
        Scanner scan = new Scanner(r);
        scan.useDelimiter(separator);

        while (scan.hasNext()) {
            sectionConsumer.accept(scan.next());
        }
    }

}
