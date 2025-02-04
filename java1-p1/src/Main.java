import java.util.Scanner;

public class Main {

    private static Scanner scanner;

    public static void main(String[] args) {

        scanner = new Scanner(System.in);

        int MAX_ARRAY_SIZE = 400;
        int MIN_ARRAY_SIZE = 1;

        int size = getArraySize(MIN_ARRAY_SIZE, MAX_ARRAY_SIZE);

        int randBoundA = getInteger("random number minimum");
        int randBoundB = getInteger("random number maximum");

        int MIN_RAND = Math.min(randBoundA, randBoundB);
        int MAX_RAND = Math.max(randBoundA, randBoundB);

        int[] arr = new int[size];

        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;

        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) (Math.random() * (MAX_RAND - MIN_RAND));
        }

        for (int num : arr) {
            min = Math.min(num, min);
            max = Math.max(num, max);
        }

        System.out.printf("min = %d, max = %d, sample size = %d\n", min, max, arr.length);

        // we're responsible for releasing this
        scanner.close();
    }

    private static int getArraySize(int min, int max) {
        Integer value = null;

        do {
            if (value != null) {
                System.out.println("That input is not acceptable.");
            }
            System.out.printf("Enter an integer between %s and %s (inclusive) for random number sample size:\n", min, max);

            String input = scanner.nextLine();

            try {
                value = Integer.parseInt(input);
            }
            catch (NumberFormatException ignored) {}

            // prevent comparison against null
            if (value == null) {
                value = -1;
            }

            // very technically, we shouldn't trust that max > min
            // since nothing has actually asserted that
        } while (!(value >= Math.min(min, max) && value <= Math.max(max, min)));

        return value;
    }

    private static int getInteger(String name) {
        Integer value = null;

        do {
            System.out.printf("Enter an integer for %s:\n", name);

            String input = scanner.nextLine();

            try {
                value = Integer.parseInt(input);
            }
            catch (NumberFormatException ignored) {}

        } while (value == null);

        return value;
    }

}
