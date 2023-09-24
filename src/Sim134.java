public class Sim134 {

    boolean[][] cells;  // Array containing the values in all the cells of the Cellular Automata grid
    int iterations;     // Count of iterations remaining
    GUI gui;            // The GUI corresponding to this simulation

    public static void main(String[] args) throws Exception {

        // HANDLE USER CONTROL

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
        }

        // Special case: iterations specified is below 0
        int iterations = Integer.parseInt(args[0]);
        if (iterations < 0) {
            System.out.println("\n\tYour first argument must be greater than 0.\n\tIt will specify the number of iterations (Integer > 0).");
        }

        // Special case: pattern type specified is not one of the expected designs
        String patternType = args[1].toUpperCase();
        if (!(patternType.equals("R") || patternType.equals("J") || patternType.equals("D"))) {
            System.out.println("\n\tYour second argument must be one of the following:\n\t\tR - Random\n\t\tJ - Jam (oscillator pattern)\n\t\tD - Dart (glider patter)");
        }

        Sim134 sim = new Sim134(iterations, patternType);

    }

    public Sim134(int iterations, String patternType) throws Exception {

        this.iterations = iterations;
        cells = new boolean[80][80];

        initializePattern(patternType);
        gui = new GUI(cells);
        gui.show();

        for (int i = 0; i < iterations; i++) {
            Thread.sleep(1000);
            updateCells();
            gui.show();
        }
    }


    private void initializePattern(String patternType) {

        // Case: Jam Oscillator
        if (patternType.equals("J")) {
            cells[35][37] = true;
            cells[35][38] = true;
            cells[35][39] = true;
            cells[36][41] = true;
            cells[37][36] = true;
            cells[37][41] = true;
            cells[38][35] = true;
            cells[38][37] = true;
            cells[38][40] = true;
            cells[39][35] = true;
            cells[39][38] = true;
            cells[40][36] = true;
            cells[40][37] = true;
        }
    }

    private void updateCells() {
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
                boolean alive = cells[i][j];
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

    private int sumNeighbors(int x, int y) {
        int count = 0;
        int tempX;
        int tempY;

        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++){
                tempX = x + i;
                tempY = y + j;

                // Check for out of bounds, or if we're looking at the inputted cell
                if (tempX < 0 || tempY < 0 || tempX >= cells.length || tempY >= cells[0].length || (tempX == x && tempY == y)) { continue; }

                // Add to count if cell is alive
                if (cells[tempX][tempY]) { count++; }
            }
        }

        return count;
    }

}
