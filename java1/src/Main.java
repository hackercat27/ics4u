import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/*
 * Assignment description:
 *
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

    // database class to handle entries
    public static class Database {

        private final List<Integer> databaseEntries = new ArrayList<>();

        public boolean hasSIN(int sin) {
            for (int entry : databaseEntries) {
                if (entry == sin) {
                    return true;
                }
            }
            return false;
        }

        public void addSIN(int sin) {
            databaseEntries.add(sin);
        }

        public int[] getAllEntries() {
            Integer[] wrapperArray = databaseEntries.toArray(new Integer[0]);

            int[] ret = new int[wrapperArray.length];

            // copy the array because wrapper class bs
            for (int i = 0; i < ret.length; i++) {
                ret[i] = wrapperArray[i];
            }

            return ret;
        }

    }

    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);

        Database db = new Database();

        System.out.println("""
                           You are logged into the (definitely official) Service Canada user database.
                           At any point type 'exit' to leave the program.
                           Enter a SIN to check if it is registered.
                           """);

        initializeDatabase(db);

        System.out.print("Dumping database.");
        for (int sin : db.getAllEntries()) {
            // format the strings to add leading zeroes
            // this is why we can't use Arrays.toString()
            // because i want it to look pretty
            System.out.printf("\n%09d", sin);
        }
        System.out.println();

        while (true) {

            String input = scan.nextLine().toLowerCase().trim();

            // accept leading and trailing whitepace because programmer paranoia
            if (input.matches("exit")) {
                break;
            }
            /*
             * regex: we want the user to input a 9 digit number
             * like 003420804.
             * this will reject smaller numbers (6, 8 digit numbers)
             * and negative numbers,
             * since none of those make sense in the context of a SIN
             */
            if (input.matches("[0-9]{9}")) {
                // input will always be a valid int because we regex matched it
                int sin = Integer.parseInt(input);

                if (db.hasSIN(sin)) {
                    System.out.printf("The SIN %09d is registered.\n", sin);
                }
                else {
                    System.out.printf("The SIN %09d not registered.\n", sin);
                }
            }
            else {
                System.out.printf("""
                                  '%s' is not a valid SIN.
                                  SINs must be 9-digit numbers with no separation between digits.
                                  """, input);
            }

        }

        System.out.println("Logging off.");

        scan.close();
    }

    public static void initializeDatabase(Database db) {
        Random r = new Random();
        for (int i = 0; i < 10; i++) {
            db.addSIN(Math.abs(r.nextInt()) % 1_000_000_000);
        }
    }

}
