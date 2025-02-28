/*
 * Create a Program that:
 *
 * Stores ten 4-letter words that you have pre-set in an array (do not print these words out to the user)
 * Randomly choose ONE of the words in the array to be the “secret word”
 * Create two methods: startsWith(char a) and an endsWith(char b) that will check to see whether the String randomly chosen out of the array starts or ends with specified letters.
 *         **Refer to some of the methods that already exist in the String class as they may be helpful - Java Library
 * Create a method that determines whether or not they have correctly guessed the “secret word”
 *
 * Interact with a user so that they can type in values. You will want to prompt your user to do the following:
 *
 * Choose to play your game (once you’ve explained how the game works)
 *
 * Ask if they would like to do one of the following:
 *
 * Determine the starting letter
 * Determine the ending letter
 * Guess the word
 * Exit the game (once they’ve guessed the word correctly, or no longer want to play)
 *
 */

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);

        String[] words = new String[] {
                "take",
                "tail",
                "fort",
                "core",
                "fail",
                "part",
                "mute",
                "null",
                "boil",
                "pull"
        };

        while (true) {

            String word = words[(int) (Math.random() * words.length)];

            String firstCharacter = String.valueOf(word.charAt(0));
            String lastCharacter = String.valueOf(word.charAt(word.length() - 1));

            System.out.println("A random english word from a secret list has been chosen. This word is "
                                       + word.length() + " characters long.");

            System.out.println("Guess the first letter of this word.");
            guessLetter(scan, firstCharacter);

            System.out.println("Guess the last letter of this word.");
            guessLetter(scan, lastCharacter);

            System.out.println("Now, guess the word. (The first letter is "
                                       + firstCharacter + " and the last letter is " + lastCharacter + ")");
            guessWord(scan, word);

            System.out.println("Do you want to play again? [y/n]");
            if (!"y".equalsIgnoreCase(scan.nextLine().trim())) {
                break;
            }

        }
    }

    public static void guessLetter(Scanner scan, String s) {

        for (boolean firstTime = true;;firstTime = false) {

            if (!firstTime) {
                System.out.println("Guess a letter.");
            }

            String input = scan.nextLine().toLowerCase();

            if (!input.matches("[a-z]")) {
                System.out.println("That's not a single letter.");
                continue;
            }

            int dir = getDirection(s, input);

            if (dir < 0) {
                System.out.println("The real letter is earlier in the alphabet.");
            }
            else if (dir > 0) {
                System.out.println("The real letter is later in the alphabet.");
            }
            else {
                System.out.println("That's the correct letter.");
                break;
            }

        }
    }

    public static void guessWord(Scanner scan, String w) {

        for (boolean firstTime = true;;firstTime = false) {

            if (!firstTime) {
                System.out.println("Guess a " + w.length() + " character word.");
            }

            String input = scan.nextLine().toLowerCase();

            if (!input.matches("[a-z]{" + w.length() + "}")) {
                System.out.println("That's not a " + w.length() + " character word.");
                continue;
            }

            if (!w.matches(input)) {
                System.out.println("That's not the correct word.");
            }
            else {
                System.out.println("That's the correct word.");
                break;
            }

        }
    }

    public static int getDirection(String value, String guess) {
        if (value.length() != 1 || guess.length() != 1) {
            return 0;
        }
        int delta = value.charAt(0) - guess.charAt(0);
        if (delta == 0) {
            return 0;
        }
        return delta / Math.abs(delta);
    }

    public static boolean startsWith(String str, String prefix) {
        return str.matches(prefix + ".*");
    }

    public static boolean endsWith(String str, String suffix) {
        return str.matches(".*" + suffix);
    }

}
