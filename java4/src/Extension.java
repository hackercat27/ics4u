import java.util.Arrays;

public class Extension {

    public static String getHumanReadableIndex(int i) {
        return String.format("%d%s", i + 1, getSuffix(i + 1));
    }

    public static String getSuffix(int i) {
        return switch (i) {
            case 1 -> "st";
            case 2 -> "nd";
            case 3 -> "rd";
            default -> "th";
        };
    }

    public static int[] getArray(int length) {
        int[] arr = new int[length];
        for (int i = 0; i < arr.length; i++) {
            // generate numbers between 1-100 inclusive
            arr[i] = (int) (Math.random() * 100) + 1;
        }

        return arr;
    }

    public static double range(int[] arr) {
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;

        for (int num : arr) {
            min = Math.min(min, num);
            max = Math.max(max, num);
        }
        return max - min;
    }

    public static void main(String[] args) {

        for (int i = 0; i < 5; i++) {
            int[] data = getArray(i + 20);

            System.out.printf("""
                              Here's the %s dataset, of length %d:
                              %s
                              Here are some statistics of this dataset:
                              mean   = %3.1f
                              median = %3.1f
                              mode   = %3.1f
                              range  = %3.1f
                              quartiles:
                              1      = %3.1f
                              2      = %3.1f
                              3      = %3.1f
                              """,
                              getHumanReadableIndex(i),
                              data.length,
                              Arrays.toString(data),
                              Statistics.mean(data),
                              Statistics.median(data),
                              Statistics.mode(data),
                              range(data),
                              Statistics.quartileOne(data),
                              Statistics.quartileTwo(data),
                              Statistics.quartileThree(data));

            System.out.println();
        }
    }

}
