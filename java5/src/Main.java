import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.function.Consumer;

public class Main {

    public static InputStream getFileInputStream(String path) {
        try {
            return new FileInputStream(path);
        }
        catch (FileNotFoundException e) {
            System.out.printf("Could not find file '%s'\n", path);
            return null;
        }
    }

//    public static String readFile(String path) {
//        InputStream in = getInputStream(path);
//        if (in == null) {
//            return null;
//        }
//        Scanner scan = new Scanner(in);
//        scan.useDelimiter("");
//
//        StringBuilder fileBuilder = new StringBuilder();
//        while (scan.hasNext()) {
//            fileBuilder.append(scan.next());
//        }
//        return fileBuilder.toString();
//    }

    public static void forEachLine(InputStream in, char separator, Consumer<String> sectionConsumer) {
        if (in == null) {
            return;
        }
        Scanner scan = new Scanner(in);
        scan.useDelimiter("");
        StringBuilder sectionBuilder = new StringBuilder();

        while (scan.hasNext()) {
            char c = scan.next().charAt(0);
            if (c == separator) {
                sectionConsumer.accept(sectionBuilder.toString());
                sectionBuilder.setLength(0);
            }
            else {
                sectionBuilder.append(c);
            }
        }
    }

    public static void forEachLine(InputStream in, Consumer<String> lineConsumer) {
        forEachLine(in, '\n', lineConsumer);
    }

    public static void main(String[] args) {

        final int TARGET_VALUE = (int) 1e6;
        // MASSIVE epsilon but it's what she wrote
        final double epsilon = 500;

        forEachLine(getFileInputStream("java5/words_alpha.txt"),
                line -> {
            int product = getStringProduct(line);

            if (equals(product, TARGET_VALUE, epsilon)) {
                System.out.printf("'%s' is a million word. product=%d delta=%d\n",
                                  line, product, TARGET_VALUE - product);
            }
        });

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
