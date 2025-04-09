import java.util.Arrays;
import java.util.function.BiFunction;

public class Main {

    public static void main(String[] args) {

        double[][] matrix = new double[3][2];
        double[][] squareMatrix = new double[3][3];

        fill(matrix, (y, x) -> Math.random());
        fill(squareMatrix, (y, x) -> (double) (x + y));

        if (isSquare(matrix)) {
            System.out.println(toString(matrix));
        }
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
