import java.io.StringReader;
import java.util.function.Consumer;

public class Test {

    public static void main(String[] args) {


        int[] testValues = new int[] {
                1, 2, 3, 4, 5
        };

        // test:
        // getProduct, getStringProduct, charToInt
        // forEach

        System.out.println(Main.getProduct(testValues));

//        for (char c = 'a'; c <= 'z'; c++) {
//            System.out.printf("%s -> %d\n", c, Main.charToInt(c));
//        }

        String input = "5,4,6,3,2";

        Consumer<String> consumer = section -> {
            System.out.println(section);
        };

        Main.forEach(new StringReader(input), ",", consumer);

    }



}
