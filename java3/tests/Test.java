import java.io.StringReader;
import java.util.Scanner;

public class Test {

    public static void main(String[] args) {

        System.out.println(Main.getDirection("s", "d"));
        System.out.println();

        Main.guessLetter(new Scanner(new StringReader("a\nz\ng\ni\nh\nj\n")), "j");
        System.out.println();
        Main.guessWord(new Scanner(new StringReader("12345\nyo\ntests\nbroth\nworld")), "world");

//        System.out.println(Main.endsWith("di stefano", "no"));

    }

}
