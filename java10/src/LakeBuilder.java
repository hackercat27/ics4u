import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


/*
	This is a problem based off of a minesweeper game where when you click
	on an empty space it opens up all of the adjacent empty spaces.
	I have modified the program so that there are two types of squares:
	those containing a 1 and those containing a 0. I've called the
	former land, and the latter empty.

	The task:
	when you click on an empty square, colour it light blue and colour all of the adjacent
	empty squares light blue.  I'm calling these squares LAKE.
	If a lake touches the edge of the board it is an OCEAN. It should be coloured a dark
	shade of blue.
	Once you have figured out how to colour whichever lake the user clicks on,
	you should try and colour the oceans if the user clicks on them.

	There are  3 tasks to complete.
	1. Get the lakes to be coloureded in blue (only the lake which you click)
	2. Get the oceans to be coloured in in the correct ocean colour (only the ocean which you click)
	3. (Advanced -- 3+) Get the random map to make continents instead of scattered islands.
 */



public class LakeBuilder
{
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new LakeBuilder();
            }
        });
    } //end main

    //constants
    final static int SCRSIZE = 720;	//screen size
    final static int SIZE = 12; //board size
    final static int sqPIXEL = 22; //pixels per square
    final static int NUM_LAND = (SIZE * SIZE /2); //number of land tiles
    final static int LAND = 1;		//constant for land tile
    final static int EMPTY = 0;		//constant for empty tile
    final static int LAKE = 33;		//this is just any number used for LAKE and OCEAN
    final static int OCEAN = 89;
    final static Color COLOURBACK = new Color(242,242,242);
    final static Color COLOUREMPTY = new Color(222,222,222);
    final static Color COLOURLAND = new Color(100,200,100);
    final static Color COLOURLAKE = new Color(100,100,255);
    final static Color COLOUROCEAN = new Color(10,10,130);

    //global variables
    int[][] board = new int[SIZE][SIZE];

    //constructor
    LakeBuilder() {
        initGame();
        createAndShowGUI();
    }

    /*PROBLEM 3: When half of the squares are land, the land is scattered quite a lot into little islands.
     *           Find a way to make a random map that has the land in bigger chunks.
     */
    void initGame() {
        //clear board
        for (int i=0;i<SIZE;i++) {
            for (int j=0;j<SIZE;j++) {
                board[i][j]=EMPTY;
            }
        }

        //check setup
        if (SCRSIZE / SIZE < 20) {
            System.out.println("Board size too small for number of squares! Aborting ...");
            System.exit(0);
        }

        makeRandomMap();
        //The method below doesn't exist yet. It is for PROBLEM #3.
        //makeContinents();
    }

    //creating a new map
    void makeRandomMap() {
        int i,j;
        boolean done = false;
        int landTiles = 0;
        while (!done) {
            i = (int)(Math.random() * SIZE);
            j = (int)(Math.random() * SIZE);

            if (board[i][j] == EMPTY) {
                board[i][j] = LAND;
                landTiles++;
                if (landTiles >= NUM_LAND) done=true;
            }
        }
    }

    /*PROBLEM 1: Fix the function "findLakes()" so that it colours all empty squares that are adjacent to this one.
     *			 You'll need to use a recursive method to solve this.
     *PROBLEM 2: Once you have solved problem 2, now set things up so that if any part
     *           of a lake touches the edge of the board it becomes an ocean.
     */
    void findLakes(int x, int y) {
        //call subroutine to colour in all contiguous lake squares

        if (board[x][y] == EMPTY) board[x][y] = LAKE;
    }


    //creation of GUI
    void createAndShowGUI() {
        DrawingPanel panel = new DrawingPanel();

        JFrame frame = new JFrame("LakeBuilder");
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        Container content = frame.getContentPane();
        content.add(panel, BorderLayout.CENTER);
        frame.setSize(100, 100); //may not be needed since my JPanel has a preferred size
        frame.setResizable(false);
        frame.setLocationRelativeTo( null );
        frame.pack();
        frame.setVisible(true);

        //once the panel is visible, initialize the graphics. (Is this before paintComponent is run?)
        panel.initGraphics();

    }

    @SuppressWarnings("serial")
    class DrawingPanel extends JPanel	//inner class
    {
        int jpanW, jpanH;
        int blockX, blockY;

        public DrawingPanel() {
            setBackground(COLOURBACK);
            //Because the panel size variables don't get initialized until the panel is displayed,
            //we can't do a lot of graphics initialization here in the constructor.
            this.setPreferredSize(new Dimension(SIZE*sqPIXEL, SIZE*sqPIXEL));
            MyMouseListener ml = new MyMouseListener();
            addMouseListener(ml);
        }

        //** Called by createGUI()
        void initGraphics() {
            jpanW = this.getSize().width;
            jpanH = this.getSize().height;
            blockX = (int)((jpanW/SIZE)+0.5);
            blockY = (int)((jpanH/SIZE)+0.5);
            // System.out.println("init");
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            //Draw white grid
            g.setColor(Color.WHITE);
            for (int i=0;i<SIZE;i++) {
                g.drawLine(blockX*i,0,blockX*i,jpanH);
                g.drawLine(0,blockY*i,jpanW,blockY*i);
            }

            for (int i=0;i<SIZE;i++) {
                for (int j=0;j<SIZE;j++) {
                    colourRect(i,j,g);
                }
            }
        }

        void colourRect(int i, int j, Graphics g) {

            int terrain = board[i][j];

            if (terrain == EMPTY) {
                g.setColor(COLOUREMPTY);
                g.fillRect(blockX*i+1, blockY*j+1, blockX-2, blockY-2);
            }
            if (terrain == LAND) {
                g.setColor(COLOURLAND);
                g.fillRect(blockX*i+1, blockY*j+1, blockX-2, blockY-2);
            }
            if (terrain == LAKE) {
                g.setColor(COLOURLAKE);
                g.fillRect(blockX*i+1, blockY*j+1, blockX-2, blockY-2);
            }
            if (terrain == OCEAN) {
                g.setColor(COLOUROCEAN);
                g.fillRect(blockX*i+1, blockY*j+1, blockX-2, blockY-2);
            }
        }

        class MyMouseListener extends MouseAdapter	{	//inner class inside DrawingPanel
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                //calculate which square you clicked on
                int i = (int)  x/blockX;
                int j = (int) y/blockY;	// blockY/y

                //allow the right mouse button to toggle/cycle the terrain
                if (e.getButton() != MouseEvent.BUTTON1) {
                    switch (board[i][j]) {
                        case LAND:
                            board[i][j] = EMPTY;
                            break;
                        default:
                            board[i][j] = LAND;
                    }
                    repaint();
                    return;
                }

                findLakes(i,j);
                repaint();
            }
        } //end of MyMouseListener class

    } //end of DrawingPanel class

}


