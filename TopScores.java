import java.io.*;
import java.util.Scanner;

/**
 * Class that manages the table of the best scores.
 * Each instance object of this class is a distinct table.
 *
 * The getRow, canAdd, and addRow methods are marked to be implemented.
 *
 * The saveToFile and restoreFromFile methods are already implemented to write to file
 * and restore from the file the best scores table.
 * Just uncomment the lines marked and implement the Score class.
 */
public class TopScores {
    // File name to save and restore table.
    private static final String FILE_NAME = "top2048.txt";

    // Maximum rows of the table.
    public static final int MAX_SCORES = 5;

    // The table. Each row of the table is an object of Score class.
    private Score[] table = new Score[MAX_SCORES];
    // Number of rows currently used in table.
    private int rows = 0;

    /**
     * The constructor read the table from file.
     */
    public TopScores() {
        restoreFromFile();
    }

    /**
     * Returns one row of the table.
     * @param idx Index of the row ( 0 .. getNumOfRows() )
     * @return The row. One object of the class Score
     */
    public Score getRow(int idx) {
        return table[idx];

        // TODO
    }

    /**
     * Returns the number of rows currently used in table.
     * @return a points between 0 and MAX_SCORES
     */
    public int getNumOfRows() {
        return rows;
    }

    /**
     * Check the possibility to add the score to the record table.
     * @param score The score to try add
     * @return true if the table is not full or the score is greater than the last stored
     */
    public boolean canAdd(int score) {

        return (table[MAX_SCORES - 1] == null || score > table[MAX_SCORES - 1].points);

        //TODO
    }

    /**
     * Inserts one more row in the table.
     * The table should be sorted in descending order of the score.
     * @param name of the player
     * @param score
     * @return true if is inserted.
     */
    public boolean addRow(String name, int score) {
        Score newScore = new Score(name, score);
        int idx = determinePosition(newScore);
        return addScore(idx, newScore);
        // TODO
    }

    //Determines the position where the passed Score object belongs in the table.
    //Returns table.length if its points is less than the lowest elements points.
    private int determinePosition(Score score) {
        int idx;
        for (idx = table.length; idx > 0; --idx)
            if (table[idx-1] != null && score.compareTo(table[idx-1]) <= 0) break;
        return idx;
    }

    //Adds the given score at the given index, shifting existing scores to accommodate.
    //Returns false if and only if the index is equal to the capacity of the table.
    private boolean addScore(int idx, Score newScore) {
        if (idx == MAX_SCORES) return false;
        for (int i = table.length-1; i > idx;--i)
            table[i] = table[i-1];
        table[idx] = newScore;
        if (rows < MAX_SCORES) ++rows;
        return true;
    }

    /**
     * Load the table of best scores from a text file.
     */
    private void restoreFromFile() {
        try { // Read table from file
            Scanner in = new Scanner(new FileReader(FILE_NAME));
            for (; in.hasNextLine(); ++rows) {
                int points = in.nextInt();
                String name = in.nextLine().trim();
                table[rows] = new Score(name, points);
            }
            in.close();
        } catch (Exception e) {
            System.out.println("Error reading file " + FILE_NAME);
        }
    }

    /**
     * Save the table of best scores in a text file.
     */
    public void saveToFile() {
        try {
            PrintWriter out = new PrintWriter(new FileWriter(FILE_NAME));
            for (int i = 0; i < rows; i++) {
                out.println(table[i].points + " " + table[i].name);
            }
            out.close();
        } catch (Exception e) {
            System.out.println("Error writing file " + FILE_NAME);
        }
    }
}
