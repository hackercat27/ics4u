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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Extension {

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




        // infinite for loop
        // not 'while (true)' because to me that's confusing - I don't want
        // to loop until "true" becomes "false", i just want to loop forever
        for (;;) {

        System.out.println("There's " + words.length +
                                   " words in a secret list - what number in the list do you want? "
                                   + "Input a number in the range 1-" + words.length + ".");

        Integer i = null;

        do {
            if (i != null) {
                System.out.println("That's not a valid word index.");
            }
            try {
                i = Integer.parseInt(scan.nextLine()) - 1;
            }
            catch (NumberFormatException ignored) {
                i = -1;
            }
        }
        while (i < 0 || i >= words.length);


        while (true) {
            List<String> wordList = new ArrayList<>(List.of(words));
            Collections.shuffle(wordList);

            // ensure that the word isnt in the same place
            if (wordList.get(i).equals(words[i])) {
                continue;
            }

            System.out.println("Here's all the words (shuffled so you can't cheat)");
            for (String word : wordList) {
                System.out.println(word);
            }
            break;
        }

            String word = words[i];

            String firstCharacter = String.valueOf(word.charAt(0));
            String lastCharacter = String.valueOf(word.charAt(word.length() - 1));

            System.out.println("You've chosen a word from this list (it's "
                                       + word.length() + " characters long).");

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

        // admittedly, this is a dubious way to do a for loop,
        // but it does elegantly solve the problem.
        // ideally, I would use a goto but java doesn't support those.

        for (boolean firstTime = true;; firstTime = false) {

            if (!firstTime) {
                // if it's not the first time we need to reprompt the user.
                System.out.println("Guess a letter.");
            }

            String input = scan.nextLine().toLowerCase();

            if (!input.matches("[a-z]")) {
                System.out.println("That's not a single letter.");
                continue;
            }

            int dir = getDirection(s, input);

            if (dir < 0) {
                System.out.printf("The real letter comes before %s.\n", input);
            }
            else if (dir > 0) {
                System.out.printf("The real letter comes after %s.\n", input);
            }
            else {
                System.out.printf("%s is the correct letter.\n", input);
                break;
            }

        }
    }


    public static void guessWord(Scanner scan, String w) {

        for (boolean firstTime = true;; firstTime = false) {

            if (!firstTime) {
                System.out.println("Guess a " + w.length() + " character word.");
            }

            String input = scan.nextLine().toLowerCase();

            // our input is always lowercase, so no need to check for uppercase characters
            if (!input.matches("[a-z]{" + w.length() + "}")) {
                System.out.println("That's not a " + w.length() + " character word.");
                continue;
            }

            if (!w.matches(input)) {
                System.out.printf("%s is not the correct word.\n", input);
            }
            else {
                System.out.printf("%s is the correct word.\n", input);
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
            // avoid funny divide by zero
            return 0;
        }
        return delta / Math.abs(delta);
    }

    // these methods aren't actually ever used - I didn't find them useful.
    // they're here purely for the assignment spec.

    public static boolean startsWith(String str, String prefix) {
        return str.matches(prefix + ".*");
    }

    public static boolean endsWith(String str, String suffix) {
        return str.matches(".*" + suffix);
    }

}
