import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        String[] words = new String[] {
                "ball",
                "kite",
                "rain",
                "date",
                "test",
                "crab",
                "star",
                "quiz",
                "jazz",
                "frog",
                "rake"
        };

        Reader r = new InputStreamReader(System.in);
        Scanner scan = new Scanner(r);

        do {
            String word = words[(int) (Math.random() * words.length)];

            char firstLetter = word.charAt(0);
            char lastLetter = word.charAt(word.length() - 1);

            System.out.printf("A random %d letter word has been chosen.\n", word.length());

            System.out.println("Guess the first letter.");
            guessLetter(firstLetter, r);
            System.out.println("Guess the last letter.");
            guessLetter(lastLetter, r);
            System.out.printf("Now, guess the word. It starts with %s and ends with %s\n",
                              firstLetter, lastLetter);
            guessWord(word, r);

            System.out.println("Do you want to play again? [y/n]");
        }
        while ("y".equalsIgnoreCase(scan.next()));

    }

    public static void guessLetter(char realLetter, Reader r) {
        Scanner scan = new Scanner(r);

        for (boolean firstTime = true;; firstTime = false) {

            if (!firstTime) {
                System.out.println("Please input a single letter.");
            }
            String input = scan.next().toLowerCase();

            if (!input.matches("[a-z]")) {
                continue;
            }

            int delta = input.charAt(0) - realLetter;

            if (delta < 0) {
                System.out.printf("The real letter is after %s.\n", input);
            }
            else if (delta > 0) {
                System.out.printf("The real letter is before %s.\n", input);
            }
            else {
                System.out.printf("Correct! It's %s.\n", input);
                break;
            }
        }

    }

    public static void guessWord(String realWord, Reader r) {
        Scanner scan = new Scanner(r);

        for (boolean firstTime = true;; firstTime = false) {

            if (!firstTime) {
                System.out.printf("Please input a %d letter word.\n", realWord.length());
            }

            String input = scan.next().toLowerCase();

            if (input.equalsIgnoreCase(realWord)){
                System.out.printf("Correct! %s is the word.\n", input);
                break;
            }
            else {
                System.out.printf("%s is not the word.\n", input);
            }
        }

    }

}
