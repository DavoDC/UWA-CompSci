import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Color;
import java.util.Random;

/**
 * Write a description of class FifteenPuzzle here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class FifteenPuzzle implements MouseListener
{ 
    private int[][] grid; // the current positions of the tiles and space, denoted by 0..15
    private SimpleCanvas sc; // the canvas for display 

    private final int size     = 4;               // the number of tiles across and down 
    private final int tilesize = 50;              // the size of a tile
    private final int gridsize = (size * tilesize); // the size of the grid 

    public  static int[][] goal   = 
        {  
            {1,2,3,4}, 
            {5,6,7,8}, 
            {9,10,11,12}, 
            {13,14, 15, 0}  
        };

    public  static int[][] start   = 
        {  
            {1,2,3,4}, 
            {5,6,7,8}, 
            {9,10,11,12}, 
            {13,14, 0, 15}  
        };

    Random rng = new Random();

    // this constructor sets up the grid as initialGrid and displays it on the canvas
    // (plus it initialises the other instance variables)
    public FifteenPuzzle ()  
    {
        grid = start;
        sc = new SimpleCanvas("Display" , gridsize+25, gridsize+25, Color.white);

        randomize();

        drawGrid();

        sc.addMouseListener(this);
    }

    public void mouseClicked (MouseEvent e) 
    {
        int x = e.getX();
        int y = e.getY();

        if (finished()) 
        {
            sc.drawRectangle(0,0,1000,1000,Color.yellow);
            sc.drawString("Well done!", 100, 150);
            return;
        }

        moveTile(x, y);
    } 

    public void mousePressed (MouseEvent e) {} 

    public void mouseReleased(MouseEvent e) {} 

    public void mouseEntered (MouseEvent e) {} 

    public void mouseExited (MouseEvent e) {} 

    private void randomize()
    {
        int x = 300;
        while (x != 0)
        {
            moveTile(rng.nextInt(gridsize),rng.nextInt(gridsize));
            x--;
        }
    }


    // draws the current grid on the canvas
    private void drawGrid() 
    {
        float rn = rng.nextInt(10000);
        float rm = (float) Math.random();
        Color background = Color.getHSBColor(rm, rm, rn);
        sc.drawRectangle(0,0,gridsize*5,gridsize*5,background);
        
        int ypos = 5;
        int r = 0;
        for (int[] i : grid)
        {
            int xpos = 5;
            int c = 0;
            for (int j : i)
            {           

                if (j != 0  && j == goal[r][c])
                {
                    drawTile(xpos, ypos, Color.green);
                    drawNumber( j ,xpos, ypos);
                }

                if (j != 0  && j != goal[r][c])
                {
                    drawTile(xpos, ypos, Color.orange);
                    drawNumber( j ,xpos, ypos);
                }

                xpos += 55;
                c++;

            }

            ypos += 55;
            r++;
        }
    }

    // draws the tile at x,y in colour c at the appropriate spot on the canvas
    private void drawTile(int x, int y, Color c)
    {
        sc.drawRectangle(x ,y ,x+tilesize ,y+tilesize, c);
        sc.repaint();
    }

    // draws the number k at x,y
    private void drawNumber(int k , int x , int y)
    {
        sc.drawString( (""+ k + " "), x+20  , y+30 );
        sc.repaint();
    }

    // returns true iff x,y is directly adjacent to the space (not diagonally)
    private boolean legalClick(int x, int y)
    {
        boolean result = false;

        int zeror = zerofinder()[0];
        int zeroc = zerofinder()[1];
        int selr = converter(y);
        int selc = converter(x);

        if (selr == zeror - 1)
        {
            if(selc == zeroc)
            {
                result = true;
            }
        }
        else if (selr == zeror)
        {
            if (selc == zeroc - 1)
            {
                result = true;
            }
            else if(selc == zeroc + 1)
            {
                result = true;
            }
        }
        else if(selr == zeror + 1)
        {
            if(selc == zeroc)
            {
                result = true;
            }
        }

        return result;
    }

    // Finds where the zero is , in the form [r,c]
    private int[] zerofinder()
    {
        int zeror = 0;
        int zeroc = 0;
        for (int i = 0; i < grid.length ; i++)
        {
            for (int j = 0; j < grid[i].length ; j++)
            {
                if ( grid[i][j] == 0)
                {
                    zeror = i;
                    zeroc = j;
                }
            }
        }

        int[] zeroCO = new int[2];

        zeroCO[0] = zeror;
        zeroCO[1] = zeroc;

        return zeroCO;
    }

    // moves the tile at x,y into the space (if it is adjacent), and re-draws the grid; o/w do nothing 
    public void moveTile (int x, int y)
    {
        if (!legalClick(x,y))
        {
            return;
        }

        // Selected 
        int first = converter(y);
        int second = converter(x);
        int selected = grid[first][second];

        //Current zero location
        int zeror = zerofinder()[0];
        int zeroc = zerofinder()[1];

        //Zero pos becomes selected value
        grid[zeror][zeroc] = selected;

        // Selected pos becomes zero
        grid[first][second] = 0;

        drawGrid();
    }

    // Helps convert a co-ord to its grid equivalent
    public int converter(int k)
    {
        int pos = 0;
        if (k < 55)
        {
            pos = 0;
        }
        else if (k < 110)
        {
            pos = 1;
        }
        else if (k < 165)
        {
            pos = 2;
        }
        else
        {
            pos = 3;
        }
        return pos;
    }

    // returns true iff the puzzle is finished
    private boolean finished()
    {
        String result = "";
        for (int[] i : grid)
        {
            for (int j : i)
            {
                result += j;
            }
        }

        if (result.equals("1234567891011121314150"))
        {
            return true;
        }

        return false;
    }

}
