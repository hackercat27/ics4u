import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;

public class Extension {

    public static void main(String[] args) {

        double[][] matrix = new double[3][2];

        fill(matrix, (y, x) -> Math.random());

        System.out.println(toString(matrix));
        System.out.println("value of " + Arrays.toString(sumDiagonals(matrix)));

    }

    public static double[] sumDiagonals(double[][] matrix) {

        if (!isRectangular(matrix)) {
            return null;
        }

        int width = matrix[0].length;
        int height = matrix.length;

        int checkLength = Math.max(width, height);

        class Diagonal {
            double value;
            int rowNum;
        }

        List<Diagonal> diagonals = new ArrayList<>();

        for (int j = 0; j < height; j++) {

            Diagonal d = new Diagonal();
            d.rowNum = -j;

            for (int i = 0; i < checkLength; i++) {

                int x = i;
                int y = i + j;

                if (x < 0 || x >= width || y < 0 || y >= height) {
                    continue;
                }

                d.value += matrix[y][x];
            }

            diagonals.add(d);

        }

        for (int j = 1; j < width; j++) {

            Diagonal d = new Diagonal();
            d.rowNum = j;

            for (int i = 0; i < checkLength; i++) {

                int x = i + j;
                int y = i;

                if (x < 0 || x >= width || y < 0 || y >= height) {
                    continue;
                }

                d.value += matrix[y][x];
            }

            diagonals.add(d);

        }

        double[] sums = new double[diagonals.size()];

        diagonals.sort(Comparator.comparingDouble(d -> d.value));

        for (int i = 0; i < sums.length; i++) {
            sums[i] = diagonals.get(i).value;
        }

        return sums;

    }

    public static void fill(double[][] matrix, BiFunction<Integer, Integer, Double> valueProvider) {
        for (int y = 0; y < matrix.length; y++) {
            for (int x = 0; x < matrix[y].length; x++) {
                matrix[y][x] = valueProvider.apply(y, x);
            }
        }
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

            builder.append("\n");
        }
        return builder.toString();
    }
}
