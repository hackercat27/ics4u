public class Utils {

    public static void sleep(double nanos) {

        long millis = (long) (nanos / 1e6);

        int nanosMod = (int) (nanos % 1e6);

        try {
            Thread.sleep(millis, nanosMod);
        }
        catch (InterruptedException ignored) {}
    }

}
