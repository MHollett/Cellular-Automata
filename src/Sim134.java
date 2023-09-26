import java.util.Random;

public class Sim134 {

    boolean[][] cells;      // Array containing the values in all the cells of the Cellular Automata grid
    boolean[][] pastCells;  // Array containing previous set of cells
    int iterations;         // Count of iterations remaining
    GUI gui;                // The GUI corresponding to this simulation


    // Handles user control and creates the simulation object based on inputs
    public static void main(String[] args) throws Exception {

        // Special case: not enough arguments were entered
        if (args.length < 2) {
            System.out.println("\n\tYou must inset 2 arguments. Please try again.");
            System.out.println("\t - Argument 1: Number of iterations (Integer >= 0)");
            System.out.println("\t - Argument 2: Pattern Type\n\t\tR - Random\n\t\tJ - Jam (oscillator pattern)\n\t\tD - Dart (glider patter)");

            return;
        }

        // Special case: number of iterations was not an integer
        try {
            Integer.parseInt(args[0]);
        } catch (Exception e) {
            System.out.println("\n\tYour first argument must be an Integer >= 0.\n\tIt will specify the number of iterations.");
            return;
        }

        // Special case: iterations specified is below 0
        int iterations = Integer.parseInt(args[0]);
        if (iterations < 0) {
            System.out.println("\n\tYour first argument must be greater than 0.\n\tIt will specify the number of iterations (Integer > 0).");
            return;
        }

        // Special case: pattern type specified is not one of the expected designs
        String patternType = args[1];
        if (!(patternType.equals("R") || patternType.equals("J") || patternType.equals("D"))) {
            System.out.println("\n\tYour second argument must be one of the following:\n\t\tR - Random\n\t\tJ - Jam (oscillator pattern)\n\t\tD - Dart (glider patter)");
            return;
        }

        Sim134 sim = new Sim134(iterations, patternType);

    }


    // Initializes and runs the simulation
    public Sim134(int iterations, String patternType) throws Exception {

        this.iterations = iterations;
        cells = new boolean[80][80];
        pastCells = new boolean[80][80];

        int MAGNIFICATION = 12;
        int DELAY = 100; // In milliseconds
        initializePattern(patternType);
        gui = new GUI(cells, MAGNIFICATION);
        gui.show();




        for (int i = 0; i < iterations; i++) {
            Thread.sleep(DELAY);

            copyCells();
            updateCells();
            gui.show();
        }

    }


    // Inputs the starting pattern
    private void initializePattern(String patternType) {

        // Case: Jam Oscillator
        if (patternType.equals("J")) {
            int startingX = (int) (cells.length / 2) - 4;
            int startingY = (int) (cells[0].length / 2) - 4;

            cells[startingX][startingY + 2] = true;
            cells[startingX][startingY + 3] = true;
            cells[startingX][startingY + 4] = true;
            cells[startingX + 1][startingY + 6] = true;
            cells[startingX + 2][startingY + 1] = true;
            cells[startingX + 2][startingY + 6] = true;
            cells[startingX + 3][startingY] = true;
            cells[startingX + 3][startingY + 2] = true;
            cells[startingX + 3][startingY + 5] = true;
            cells[startingX + 4][startingY] = true;
            cells[startingX + 4][startingY + 3] = true;
            cells[startingX + 5][startingY + 1] = true;
            cells[startingX + 5][startingY + 2] = true;
        }

        // Case: Random
        else if (patternType.equals("R")) {
            Random rand = new Random();

            for (int i = 0; i < cells.length; i++) {
                for (int j = 0; j < cells[0].length; j++) {
                    cells[i][j] = rand.nextBoolean();
                }
            }
        }

        else if (patternType.equals("D")) {
            int startingX = (int) (cells.length / 2) - 8;
            int startingY = 12 * (int) (cells[0].length / 16);

            cells[startingX][startingY + 8] = true;
            cells[startingX + 1][startingY + 7] = true;
            cells[startingX + 1][startingY + 9] = true;
            cells[startingX + 2][startingY + 6] = true;
            cells[startingX + 2][startingY + 7] = true;
            cells[startingX + 3][startingY + 9] = true;
            cells[startingX + 4][startingY + 5] = true;
            cells[startingX + 4][startingY + 9] = true;
            cells[startingX + 5][startingY + 2] = true;
            cells[startingX + 5][startingY + 5] = true;
            cells[startingX + 6][startingY + 1] = true;
            cells[startingX + 6][startingY + 3] = true;
            cells[startingX + 6][startingY + 6] = true;
            cells[startingX + 6][startingY + 7] = true;
            cells[startingX + 6][startingY + 8] = true;
            cells[startingX + 6][startingY + 9] = true;
            cells[startingX + 7][startingY] = true;
            cells[startingX + 7][startingY + 3] = true;
            cells[startingX + 14][startingY + 8] = true;
            cells[startingX + 13][startingY + 7] = true;
            cells[startingX + 13][startingY + 9] = true;
            cells[startingX + 12][startingY + 6] = true;
            cells[startingX + 12][startingY + 7] = true;
            cells[startingX + 11][startingY + 9] = true;
            cells[startingX + 10][startingY + 5] = true;
            cells[startingX + 10][startingY + 9] = true;
            cells[startingX + 9][startingY + 2] = true;
            cells[startingX + 9][startingY + 5] = true;
            cells[startingX + 8][startingY + 1] = true;
            cells[startingX + 8][startingY + 3] = true;
            cells[startingX + 8][startingY + 6] = true;
            cells[startingX + 8][startingY + 7] = true;
            cells[startingX + 8][startingY + 8] = true;
            cells[startingX + 8][startingY + 9] = true;
        }
    }


    // updates the cell according to the rules of The Game of Life and updates the GUI to reflect the changes
    private void updateCells() {
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
                boolean alive = pastCells[i][j];
                int neighbors = sumNeighbors(i,j);

                // Case 1: Cell is alive. If the # of neighbors is not 2 or 3, the cell dies
                if (alive) {
                    if (!(neighbors == 2 || neighbors == 3)) { cells[i][j] = false; }
                }
                // Case 2: Cell is dead. If the # of neighbors is 3, the cell comes to life
                else {
                    if (neighbors == 3) { cells[i][j] = true; }
                }

                // Update gui
                gui.updateCell(i, j, cells[i][j]);
            }
        }

        gui.show();
    }

    // Gets the count of alive neighbors to the cell inputted as x and y coordinates
    private int sumNeighbors(int x, int y) {
        int count = 0;
        int tempX;
        int tempY;


        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++){
                tempX = x + i;
                tempY = y + j;

                // Check for out of bounds, or if we're looking at the inputted cell
                if (tempX < 0 || tempY < 0 || tempX >= pastCells.length || tempY >= pastCells[0].length || (tempX == x && tempY == y)) { continue; }

                // Add to count if cell is alive
                if (pastCells[tempX][tempY]) {
                    count++;
                }
            }
        }

        return count;
    }

    // Copies the cells array to the pastCells array (Stores the cell history)
    private void copyCells() {
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
                pastCells[i][j] = cells[i][j];
            }
        }
    }

}
