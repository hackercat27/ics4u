import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*
 * In this Assignment, I would like you to create a program that does the following:
 * - Creates 10 random 9-digit numbers (kind of like recreating SIN numbers) and print out the array.
 * - Ask a user to check if a number is in the database.
 * - Return a response to the user about whether or not the number is in the database.
 * - EXTEND: Allow the user to keep inputting numbers to check within the database
 *           until the user chooses to exit.
 *           You may need to use a long variable type instead of an int since the size
 *           of the 9-digit numbers could exceed what an int is able to store.
 *           Use similar logic for ints, but with a long instead.
 */

public class Main {

    private static class Database {

        private static class Entry {
            int sin = -1;
        }

        public List<Entry> databaseEntries = new ArrayList<>();



    }

    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);

        while (true) {

            String input = scan.nextLine().toLowerCase();

            if (input.matches(".*exit.*")) {
                break;
            }

            if (input.matches(".*[0-9]{9}")) {

            }

        }

    }

    public static boolean numberInDatabase() {

    }

}
