import java.util.Arrays;
import java.util.Scanner;

/*
 * Assignment description:
 *
 * - Make an array of size 15 that contains ints
 * - Fill it with random numbers that range from 1-100
 * - Print out the array
 * - Find the largest, find the smallest number.
 * -    ** Try to do this without sorting the array.
 * -    ** Create a method that takes one value of the array and compares it to the next,
 *         where the largest value is always being stored until that is the value to return.
 *      ** If you are going to do a Google search to support you with this,
 *         you must be able to explain your logic to Mrs. Di Stefano
 *
 * EXTEND: Try to use some of the methods within the Array or Arrays class in the Java library.
 * EXTEND: Allow the user to set the array size and the smallest and lowest
 *         numbers that you will use to randomly generate numbers.
 *         Then print the array and find the largest and smallest numbers.
 */

public class Main {

    private static Scanner scanner;

    public static void main(String[] args) {

        scanner = new Scanner(System.in);

        int MAX_ARRAY_SIZE = 30;
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
            arr[i] = (int) (Math.random() * (MAX_RAND - MIN_RAND)) + MIN_RAND;
        }

        System.out.printf("Random numbers: %s\n", Arrays.toString(arr));

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
            System.out.printf("Enter an integer between %s and %s (inclusive) for random number sample size:\n",
                              min, max);

            String input = scanner.nextLine();

            try {
                value = Integer.parseInt(input);
            }
            catch (NumberFormatException ignored) {}

            // prevent comparison against null in while condition
            if (value == null) {
                value = -1;
            }

            // technically, we shouldn't trust that max > min
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
