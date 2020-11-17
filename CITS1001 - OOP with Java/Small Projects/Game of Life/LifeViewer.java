
/**
 * Write a description of class LifeViewer here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import java.awt.Color;

public class LifeViewer
{
    private Life life;
    private int width, height; 
    private SimpleCanvas c;
    
    private final static int CELL_SIZE = 5;
    private final static Color BACK_COLOUR = Color.white;
    private final static Color CELL_COLOUR = Color.red;
    private final static Color GRID_COLOUR = Color.black;
    
    public LifeViewer(Life life)
    {
        this.life = life;
        width  = life.getMap().length;
        height = life.getMap()[0].length;
        c = new SimpleCanvas("The Game of Life", width * CELL_SIZE + 1, height * CELL_SIZE + 1, BACK_COLOUR);
        display();
    }
    
    private void display()
    {
        drawCells();
        drawGrid();
        c.repaint();
    }
    
    private void drawCells()
    {
        Color col;
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
            {
                if (life.getMap()[i][j]) col = CELL_COLOUR;
                else                     col = BACK_COLOUR;
                c.setForegroundColour(col);
                c.drawRectangle(i * CELL_SIZE, j * CELL_SIZE, (i + 1) * CELL_SIZE, (j + 1) * CELL_SIZE);
            }
    }
    
    private void drawGrid()
    {
        c.setForegroundColour(GRID_COLOUR);
        for (int i = 0; i <= width; i++)
            c.drawLine(i * CELL_SIZE, 0, i * CELL_SIZE, height * CELL_SIZE);
        for (int j = 0; j <= height; j++)
            c.drawLine(0, j * CELL_SIZE, width * CELL_SIZE, j * CELL_SIZE);
    }
    
    public void animate(int n)
    {
        for (int i = 0; i < n; i++)
        {
            life.nextGeneration();
            display();
            c.wait(250);
        }
    }
}
