import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


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


public class LakeBuilder {

    public static final long seed;

    static {
        seed = new Random().nextLong();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LakeBuilder::new);
    }

    public static final int BOARD_SIZE = 64;
    public static final int PREFERRED_TILE_SIZE = 24;
    public static final int OUTLINE_MARGIN = 0;

    private int workingX = -1;
    private int workingY = -1;

    public static final int NUM_LAND = (BOARD_SIZE * BOARD_SIZE /2);

    public static final Color COLOR_BACKGROUND = new Color(242, 242, 242);

    public enum Tile {

        LAND(new Color(100, 200, 100)),
        OCEAN(new Color(10, 10, 130)),
        LAKE(new Color(100, 100, 255)),
        EMPTY(new Color(222, 222, 222)),
        OUT_OF_MAP(new Color(0xFF00FF));

        public final Color color;
        Tile(Color color) {
            this.color = color;
        }
    }

    private Tile[][] board = new Tile[BOARD_SIZE][BOARD_SIZE];

    public Tile getTile(int x, int y) {
        if (x < 0 || x >= BOARD_SIZE || y < 0 || y >= BOARD_SIZE) {
            return Tile.OUT_OF_MAP;
        }
        return board[y][x];
    }

    public void setTile(Tile t, int x, int y) {
        if (x < 0 || x >= BOARD_SIZE || y < 0 || y >= BOARD_SIZE) {
            return ;
        }
        board[y][x] = t;
    }

    //constructor
    public LakeBuilder() {
        initGame();
        createAndShowWindow();
    }

    /*PROBLEM 3: When half of the squares are land, the land is scattered quite a lot into little islands.
     *           Find a way to make a random map that has the land in bigger chunks.
     */
    public void initGame() {
        //clear board
        for (int y = 0; y< BOARD_SIZE; y++) {
            for (int x = 0; x< BOARD_SIZE; x++) {
                setTile(Tile.EMPTY, x, y);
            }
        }


        makeRandomMap();
//        makeContinents();
    }

    //creating a new map
    private void makeRandomMap() {


        Random random = new Random(seed);

        boolean done = false;
        int landTiles = 0;

        while (!done) {
            int y = random.nextInt(BOARD_SIZE);
            int x = random.nextInt(BOARD_SIZE);

            if (getTile(x, y) == Tile.EMPTY) {
                setTile(Tile.LAND, x, y);
                landTiles++;
                if (landTiles >= NUM_LAND) {
                    done = true;
                }
            }
        }
    }

    private void makeContinents() {

        final double LAND_THRESHOLD = 2;

        for (int y = 0; y < BOARD_SIZE; y++) {
            for (int x = 0; x < BOARD_SIZE; x++) {

                if (getNoise(x, y) > LAND_THRESHOLD) {
                    setTile(Tile.LAND, x, y);
                }
                else {
                    setTile(Tile.EMPTY, x, y);
                }

            }
        }

    }

    public static double getNoise(double x, double y) {

        Random rand = new Random(seed);
        Random trueRand = new Random();
        double scale = BOARD_SIZE * BOARD_SIZE / 144d;
        int voronoiCount = (int) (10 * scale);


        class vec2 {
            double x;
            double y;
        }

        vec2[] points = new vec2[voronoiCount];

        for (int i = 0; i < points.length; i++) {
            points[i] = new vec2();
            points[i].x = rand.nextDouble() * BOARD_SIZE;
            points[i].y = rand.nextDouble() * BOARD_SIZE;
        }

        double distanceSum = 0;
        double minDistance = Double.POSITIVE_INFINITY;

        vec2 salt = new vec2();
        salt.x = (biasedRandomDouble(trueRand) - 0.5) * 2.5d;
        salt.y = (biasedRandomDouble(trueRand) - 0.5) * 2.5d;

        for (vec2 point : points) {

            double distance = Math.hypot(point.x - x + salt.x, point.y - y + salt.y);
            distanceSum += distance * BOARD_SIZE / 12d;
            minDistance = Math.min(minDistance, distance);
        }

        return 4 - minDistance;

    }

    private static double biasedRandomDouble(Random random) {
        double x = random.nextDouble();
        double ret = 2 * Math.pow(x - 0.5, 3) + 0.5;
        return ret;
//        return random.nextDouble();
    }

    /*PROBLEM 1: Fix the function "fillLake()" so that it colours all empty squares that are adjacent to this one.
     *			 You'll need to use a recursive method to solve this.
     *PROBLEM 2: Once you have solved problem 2, now set things up so that if any part
     *           of a lake touches the edge of the board it becomes an ocean.
     */
    public void floodLake(int x, int y) {
        //call subroutine to colour in all contiguous lake squares

        if (getTile(x, y) != Tile.EMPTY) {
            return;
        }

        boolean isEdgeTile = x == 0 || x == BOARD_SIZE - 1 || y == 0 || y == BOARD_SIZE - 1;

        if (isEdgeTile) {
            floodOcean(x, y);
            return;
        }

        setTile(Tile.LAKE, x, y);

        sleep(x, y);
        floodLake(x, y - 1);
        sleep(x, y);
        floodLake(x, y + 1);
        sleep(x, y);
        floodLake(x + 1, y);
        sleep(x, y);
        floodLake(x - 1, y);
    }

    public void floodOcean(int x, int y) {

        if (getTile(x, y) != Tile.EMPTY && getTile(x, y) != Tile.LAKE) {
            return;
        }

        setTile(Tile.OCEAN, x, y);

        sleep(x, y);
        floodOcean(x, y - 1);
        sleep(x, y);
        floodOcean(x + 1, y);
        sleep(x, y);
        floodOcean(x, y + 1);
        sleep(x, y);
        floodOcean(x - 1, y);
    }

    private void sleep(int x, int y) {
        workingX = x;
        workingY = y;
        try {
            panel.repaint();
            Thread.sleep(1);
        }
        catch (InterruptedException e) {
        }
    }

    DrawingPanel panel;

    public void createAndShowWindow() {
        panel = new DrawingPanel();

        JFrame frame = new JFrame("LakeBuilder");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    class DrawingPanel extends JPanel {

        public DrawingPanel() {
            setBackground(COLOR_BACKGROUND);
            this.setPreferredSize(new Dimension(BOARD_SIZE * PREFERRED_TILE_SIZE, BOARD_SIZE * PREFERRED_TILE_SIZE));
            MyMouseListener ml = new MyMouseListener();
            addMouseListener(ml);
        }

        public double getBlockWidth() {
            return getScale() / BOARD_SIZE;
        }

        public double getBlockHeight() {
            return getScale() / BOARD_SIZE;
        }

        public double getScale() {
            return getHeight();
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

//            System.out.println("Painted");

            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());

            if (g instanceof Graphics2D g2) {
                g2.translate(getWidth() / 2 - getHeight() / 2, 0);
            }

            g.setColor(Color.WHITE);
            g.fillRect(-1, 0, getHeight(), getHeight());

            for (int y = 0; y < BOARD_SIZE; y++) {
                for (int x = 0; x < BOARD_SIZE; x++) {
                    colorRect(x, y, g);
                }
            }
        }

        public void colorRect(int x, int y, Graphics g) {

            Tile terrain = getTile(x, y);

            if (x == workingX && y == workingY) {
                g.setColor(Color.RED);
            }
            else {
                g.setColor(terrain.color);
            }

//            if (g instanceof Graphics2D g2) {
//                g.fillRect(getBlockWidth() * x + OUTLINE_MARGIN, getBlockHeight() * y + OUTLINE_MARGIN,
//                           getBlockWidth() - 2 * OUTLINE_MARGIN, getBlockHeight() - 2 * OUTLINE_MARGIN);
//            }
//            else {
                g.fillRect((int) Math.floor(getBlockWidth() * x + OUTLINE_MARGIN), (int) Math.floor(getBlockHeight() * y + OUTLINE_MARGIN),
                           (int) Math.ceil(getBlockWidth() - 2 * OUTLINE_MARGIN), (int) Math.ceil(getBlockHeight() - 2 * OUTLINE_MARGIN));
//            }
        }

        public class MyMouseListener extends MouseAdapter	{	//inner class inside DrawingPanel
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                //calculate which square you clicked on
                int boardX = (int) ((x - getWidth() / 2d + getHeight() / 2d) / getBlockWidth());
                int boardY = (int) (y / getBlockHeight());

                //allow the right mouse button to toggle/cycle the terrain
                if (e.getButton() == MouseEvent.BUTTON3) {
                    if (getTile(boardX, boardY) == Tile.LAND) {
                        setTile(Tile.EMPTY, boardX, boardY);
                    }
                    else {
                        setTile(Tile.LAND, boardX, boardY);
                    }
                }
                if (e.getButton() == MouseEvent.BUTTON1) {
                    new Thread(() -> {
                        floodLake(boardX, boardY);
                        workingX = -1;
                        workingY = -1;
                    }, "painter").start();
                }
            }
        } //end of MyMouseListener class

    } //end of DrawingPanel class

}


