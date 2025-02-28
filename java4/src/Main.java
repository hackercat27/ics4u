import java.util.Arrays;

public class Main {

    public static int[] getArray() {
        // arbitrarily choose 20, any length really could work
        int[] arr = new int[20];
        for (int i = 0; i < arr.length; i++) {
            // generate numbers between 1-100 inclusive
            arr[i] = (int) (Math.random() * 100) + 1;
        }

        return arr;
    }

    public static void main(String[] args) {

        int[] data = getArray();

        System.out.printf("""
                          Of the given dataset,
                          %s
                          here are some statistics:
                          mean = %.1f
                          median = %.1f
                          mode = %.1f
                          quartiles:
                          1 = %.1f
                          2 = %.1f
                          3 = %.1f
                          """,
                          Arrays.toString(data),
                          Stats.mean(data), Stats.median(data), Stats.mode(data),
                          Stats.quartileOne(data), Stats.quartileTwo(data), Stats.quartileThree(data));
    }

}
