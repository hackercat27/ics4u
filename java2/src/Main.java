
/*
 * Coding Task: Create a program in order to complete as many of the following tasks as you can:
 * Within the main method:
 * - Print out each array using the Arrays.toString(int [] arr) method. (remember you’ll need to import Arrays)
 * - Print out information by accessing each method and print out information.
 * - OUTSIDE of the main method, create methods that:
 *     - Create a method that:
 *         - Randomly generates a number between 1-10 (which will be used to set the size of the array)
 *         - Create an int array. Initialize the array to be the size of the random number between 1-10
 *           that you just created.
 *         - Randomly generate numbers between 10-20 and fill the array with these random numbers
 *           (Adjust the added value to our random number generator as modelled in Java 2C).
 *     - Return true if the array has between 5-10 terms
 *     - Return true if the first number is the smallest in the array (do you understand how to adjust your for loop?)
 *     - Return true if the last number is the largest in the array (do you understand how to adjust your for loop?)
 *     - Return the product of the first two values in the array
 */

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {

        int[] arr = getArray();

        System.out.printf("The first array is %s\n",
                          Arrays.toString(arr));
        System.out.printf("It %s between 5-10 terms.\n",
                          isArrayLengthBetween(arr, 5, 10)? "has" : "does not have");
        System.out.printf("The smallest element %s present at the start of the array.\n",
                          isSmallestFirst(arr)? "is" : "is not");
        System.out.printf("The largest element %s present at the end of the array.\n",
                          isLargestLast(arr)? "is" : "is not");
        System.out.printf("The product of elements 0 and 1 of this array is %d.\n",
                          getProduct(arr));
    }

    /**
     * Creates an array of at least length 2, and fills it with random numbers
     * before returning it.
     *
     * @return an array of a random length filled with random numbers
     */
    public static int[] getArray() {
        // the assignment specifies to generate an array with a min length of 1, but 2
        // is used here instead to prevent throwing an error with the getProduct() method.
        int[] arr = new int[random(2, 10)];
        fillArray(arr, 10, 20);
        return arr;
    }

    /**
     * Returns {@code true} if the array's length is between min and max (inclusive),
     * otherwise it returns {@code false}.
     *
     * @param arr The input array.
     * @param min The minimum value (inclusive) to consider for the array length.
     * @param max The maximum value (inclusive) to consider for the array length.
     * @return {@code true} if the array's length is between min and max (inclusive),
     *         otherwise {@code false}
     */
    public static boolean isArrayLengthBetween(int[] arr, int min, int max) {
        if (min > max) {
            // make sure the arguments aren't nonsense
            throw new RuntimeException(new IllegalStateException("min > max!"));
        }
        return arr.length >= min && arr.length <= max;
    }

    /**
     * Returns {@code true} if the array contains the smallest element at
     * the start of the array. {@code} true will still be returned if other
     * equally small elements are elsewhere in the array.
     *
     * @param arr The array to check
     * @return {@code true} if the smallest element is included at the start of the
     *         array, otherwise {@code false}
     */
    public static boolean isSmallestFirst(int[] arr) {
        if (arr.length == 0) {
            // throw a more detailed error rather than just an IndexOutOfBoundsException
            throw new RuntimeException(new IllegalStateException("arr.length == 0!"));
        }
        return arr[0] == getSmallest(arr);
    }

    /**
     * Returns {@code true} if the array contains the largest element at
     * the end of the array. {@code true} will still be returned if other
     * equally large elements are elsewhere in the array.
     *
     * @param arr The array to check
     * @return {@code true} if the smallest element is included at the start of the
     *         array, otherwise {@code false}
     */
    public static boolean isLargestLast(int[] arr) {
        if (arr.length == 0) {
            // throw a more detailed error rather than just an IndexOutOfBoundsException
            throw new RuntimeException(new IllegalStateException("arr.length == 0!"));
        }
        return arr[arr.length - 1] == getLargest(arr);
    }

    /**
     * Returns the product of the first 2 elements in the given array.
     * The array must be of length 2.
     *
     * @param arr Array of at least length 2 to read the first 2 elements from
     * @return the product of the first 2 elements in the given array
     */
    public static int getProduct(int[] arr) {
        if (arr.length < 2) {
            // throw a more detailed error rather than just an IndexOutOfBoundsException
            throw new RuntimeException(new IllegalStateException("arr.length < 2!"));
        }
        return arr[0] * arr[1];
    }

    // Any extra helper methods

    private static void fillArray(int[] arr, int minValue, int maxValue) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = random(minValue, maxValue);
        }
    }

    private static int random(int min, int max) {
        // this will still work even if min > max,
        // so even though the inputs will be nonsense we won't throw an exception
        // (unlike in the isArrayLengthBetween() method)
        int range = max - min;
        return min + (int) (Math.random() * range);
    }

    private static int getSmallest(int[] arr) {
        // assume that it must be smaller than the largest possible int
        int min = Integer.MAX_VALUE;
        for (int n : arr) {
            min = Math.min(n, min);
        }
        // the only 2 ways that min can still equal Integer.MAX_VALUE is if the array
        // contained only Integer.MAX_VALUE or if it was of length 0.
        // in the latter, Integer.MAX_VALUE can be considered to be an "error state"
        return min;
    }

    private static int getLargest(int[] arr) {
        // assume that it must be larger than the smallest possible int
        int max = Integer.MIN_VALUE;
        for (int n : arr) {
            max = Math.max(n, max);
        }
        // the only 2 ways that max can still equal Integer.MIN_VALUE is if the array
        // contained only Integer.MIN_VALUE or if it was of length 0.
        // in the latter, Integer.MIN_VALUE can be considered to be an "error state"
        return max;
    }
}
