public class Sim134 {
    public static void main(String[] args) {

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

    }

}
