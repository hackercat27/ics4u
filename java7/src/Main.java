import java.util.Arrays;
import java.util.function.BiFunction;

public class Main {

    public static void main(String[] args) {

        double[][] squareMatrix = new double[3][2];
        double[][] matrix = new double[3][3];

        fill(matrix, (y, x) -> Math.random());
        fill(squareMatrix, (y, x) -> (double) (x + y));

        System.out.println(toString(matrix));

        if (isSquare(matrix)) {
            System.out.println("This matrix is square.");
        }
        else if (isRectangular(matrix)) {
            System.out.println("This matrix is rectangular.");
        }

        System.out.println("This matrix has the following column sum:");
        System.out.println(Arrays.toString(sumColumns(matrix)));
        System.out.println("This matrix has the following row sum:");
        System.out.println(Arrays.toString(sumRows(matrix)));
        System.out.println("The sum of all of the elements in this array is " + sumAll(matrix) + ".");
    }

    public static void fill(double[][] matrix, BiFunction<Integer, Integer, Double> valueProvider) {
        for (int y = 0; y < matrix.length; y++) {
            for (int x = 0; x < matrix[y].length; x++) {
                matrix[y][x] = valueProvider.apply(y, x);
            }
        }
    }

    public static double[] sumColumns(double[][] matrix) {
        if (!isRectangular(matrix)) {
            return null;
        }

        double[] ret = new double[matrix.length];

        for (int i = 0; i < matrix.length; i++) {

            for (int j = 0; j < matrix[i].length; j++) {
                ret[i] += matrix[i][j];
            }

        }

        return ret;
    }

    public static double[] sumRows(double[][] matrix) {
        if (!isRectangular(matrix)) {
            return null;
        }

        double[] ret = new double[matrix[0].length];

        for (int i = 0; i < ret.length; i++) {

            for (int j = 0; j < matrix[i].length; j++) {
                ret[i] += matrix[i][j];
            }

        }

        return ret;
    }

    public static double sumAll(double[][] matrix) {
        double sum = 0;
        for (int y = 0; y < matrix.length; y++) {
            for (int x = 0; x < matrix[y].length; x++) {
                sum += matrix[y][x];
            }
        }
        return sum;
    }

    public static boolean isSquare(double[][] matrix) {
        for (double[] row : matrix) {
            if (matrix.length != row.length) {
                return false;
            }
        }
        return true;
    }

    public static boolean isRectangular(double[][] matrix) {
        int length = matrix[0].length;
        for (double[] row : matrix) {
            if (row.length != length) {
                return false;
            }
        }
        return true;
    }

    public static double[][] add(double[][] A, double[][] B) {
        if (!isRectangular(A) || !isRectangular(B)) {
            return null;
        }
        if (A.length != B.length || A[0].length != B[0].length) {
            return null;
        }

        double[][] C = new double[A.length][A[0].length];

        for (int y = 0; y < A.length; y++) {
            for (int x = 0; x < A[y].length; x++) {
                C[y][x] = A[y][x] + B[y][x];
            }
        }

//        void byte short int long float double boolean char goto instanceof if else do while for continue break
//        strictfp volatile transient public private protected static enum class interface default case switch
//        try catch finally final throw throws new assert this package import const

        return C;
    }

    public static String toString(double[][] matrix) {

        if (matrix == null || !isRectangular(matrix)) {
            return null;
        }
        if (matrix.length == 1) {
            return Arrays.toString(matrix[0]);
        }

        String leftTop = "⎡";
        String leftMid = "⎢";
        String leftBot = "⎣";
        String rightTop = "⎤";
        String rightMid = "⎥";
        String rightBot = "⎦";

        StringBuilder builder = new StringBuilder();

        for (int y = 0; y < matrix.length; y++) {
            boolean top = y == 0;
            boolean bottom = y == matrix.length - 1;

            if (top)         builder.append(leftTop);
            else if (bottom) builder.append(leftBot);
            else             builder.append(leftMid);

            for (int x = 0; x < matrix[y].length; x++) {
                boolean last = x == matrix[y].length - 1;

                builder.append(String.format("%2.2f", matrix[y][x]));
                if (!last) {
                    builder.append(" ");
                }
            }

            if (top)         builder.append(rightTop);
            else if (bottom) builder.append(rightBot);
            else             builder.append(rightMid);

            if (!bottom) {
                builder.append("\n");
            }
        }
        return builder.toString();
    }
}
