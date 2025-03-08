import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.function.Consumer;

public class Main {

    public static void main(String[] args) {

        final int TARGET_VALUE = (int) 1e6;
        // honestly a massive epsilon but it's what the spec called for
        final double EPSILON = 500;


        FileInputStream in;
        try {
            in = new FileInputStream("java5/words_alpha.txt");
        }
        catch (FileNotFoundException e) {
            System.out.println("Could not find file");
            return;
        }

        forEach(in, "\n", line -> {
            int product = getStringProduct(line);

            if (equals(product, TARGET_VALUE, EPSILON)) {
                System.out.printf("'%s' is a million word.\n",
                                  line);
            }
        });
    }

    public static void forEach(InputStream in, String separator, Consumer<String> sectionConsumer) {
        if (in == null) {
            return;
        }
        Scanner scan = new Scanner(in);
        scan.useDelimiter(separator);

        while (scan.hasNext()) {
            sectionConsumer.accept(scan.next());
        }
    }

    public static int getStringProduct(String word) {
        char[] values = word.toLowerCase().toCharArray();

        int[] intValues = new int[values.length];
        for (int i = 0; i < intValues.length; i++) {
            intValues[i] = values[i] - 'a' + 1; // lua indexing. gross
        }
        return getProduct(intValues);
    }

    public static int getProduct(int[] values) {
        if (values.length == 0) {
            return 1;
        }

        int product = 1;
        for (int value : values) {
            product *= value;
        }
        return product;
    }

    public static boolean equals(double a, double b, double epsilon) {
        return Math.abs(a - b) <= Math.abs(epsilon);
    }

}
