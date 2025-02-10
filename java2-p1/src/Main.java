
/*
 * Assignment description:
 *
 * Create a program to satisfy the following:
 *
 * There must be at least three arrays of at least two different variable types
 * (ie. not all Strings or all int arrays)
 *
 * Choose any sports league of your choice (or any other gaming league that you can find stats for)
 *
 * Initialize each array with at least five pieces of data (ie. 5 sports teams) similar to page 1 and 2 of this note. (no more than 10)
 *
 * Determine each team’s winning percentage, similar to Line 104-110 in the provided code.
 *
 * Ask the user to choose some options of how to view the information in your database, similar to Line 112 to end of code.
 */

public class Main {

    public static void main(String[] args) {

        int databaseSize = 7;

        String[] teamNames = new String[databaseSize];
        int[] gamesPlayed = new int[databaseSize];
        int[] gamesWon = new int[databaseSize];

        teamNames[0] = "Pine Valley";
        teamNames[1] = "Springfield";
        teamNames[2] = "Steel City";
        teamNames[3] = "Maple Hollow";
        teamNames[4] = "Ingersoll";
        teamNames[5] = "Ashdale";
        teamNames[6] = "Redwood";

        for (int i = 0; i < databaseSize; i++) {
            // "i found stats"
            gamesPlayed[i] = (int) ((Math.random() * 9) + 20);
            gamesWon[i] = gamesPlayed[i] - (int) ((Math.random() * 15));
            if (teamNames[i].equalsIgnoreCase("ingersoll")) {
                gamesWon[i] = 0;
            }
        }


        for (int i = 0; i < databaseSize; i++) {

            double winRate = (double) gamesWon[i] / gamesPlayed[i];
            System.out.printf("Sports team %s has played %d games and won %d - winning %.2f per cent of games\n",
                              teamNames[i], gamesPlayed[i], gamesWon[i], winRate * 100);

        }

    }

}