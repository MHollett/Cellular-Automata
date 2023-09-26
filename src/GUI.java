import java.awt.Color;

public class GUI {

    private int x,y;                // x-by-y grid of cells
    private int magnification;      // pixel-width of each cell
    private boolean[][] cells;      // stores the value in each cell
    private Picture pic;            // picture to be drawn on screen


    // Initializes a GUI to match the cells array taken as an argument
    public GUI(boolean[][] cells, int m) throws Exception {
        // Special case: cells array is empty
        if (cells.length == 0)
        {
            System.out.println("\nError: empty cells array received in GUI initialization.\n");
            throw new Exception("Empty cells array");
        }

        this.cells = cells;
        x = cells.length;
        y = cells[0].length;
        magnification = m;
        pic = new Picture(x * magnification, y * magnification);


        // Initialize cells according to cells array
        for (int i = 0; i < x; i++)
        {
            for (int j = 0; j < y; j++)
            {
                updateCell(i,j, cells[i][j]);
            }
        }
    }


    // Colors in the given cell according to the value argument. Either white (false) or black (true).
    public void updateCell(int i, int j, boolean value)
    {
        cells[i][j] = value;

        // Initialize Color object
        Color col = new Color(255,255,255);
        Color border = new Color(191,191,191);
        if (value)
        {
            col = new Color(0,0,0);
            //border = new Color(0,0,0);
        }

        // Color in selected cell
        for (int offsetX = 0; offsetX < magnification; offsetX++)
        {
            for (int offsetY = 0; offsetY < magnification; offsetY++)
            {
                // set() colours an individual pixel
                if (offsetX == magnification - 1 || offsetY == magnification - 1) {
                    pic.set((i*magnification)+offsetX, (j*magnification)+offsetY, border);
                }
                else
                {
                    pic.set((i*magnification)+offsetX, (j*magnification)+offsetY, col);
                }
            }
        }
    }

    // update the picture on screen
    public void show()
    {
        pic.show();     // without calling this the pic will not show
    }

}
