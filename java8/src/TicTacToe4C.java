
/* This game is written all in one class (file).
 * At some point as complexity increases, it would
 * make sense to write games in more than one class,
 * splitting the graphics off from the data and
 * processing.
 */

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class TicTacToe4C {

    // Task #1: Create a 4x4 grid. Show Mrs. Di Stefano, then switch it back
    static final int GRID_SIZE = 3; // the grid size
    private static final int WIN_LENGTH = 3;
    static final int EMPTY = 0;
    private static final int X = 1;
    private static final int O = -1;

    int[][] board = new int[GRID_SIZE][GRID_SIZE];
    // X = +1 (set board[row][col] = 1)
    // O = -1 (set board[row][col] = -1)
    // empty = 0 (when the board[row][col] = 0)

    boolean isXTurn = true; // when turn is true, it's X's turn

    JLabel topLabel;
    GraphicsPanel grpanel;

    TicTacToe4C() {
        setupGame();
        createAndShowGUI();
    }

    void setupGame() {
        // make it X's turn
        isXTurn = true;

        // clear board
        for (int y = 0; y < GRID_SIZE; y++) {
            for (int x = 0; x < GRID_SIZE; x++) {
                board[y][x] = EMPTY;
            }
        }
        // Task #2: write ONE line of code that would print an X in the bottom right box

//        board[GRID_SIZE - 1][GRID_SIZE - 1] = X;


        // Task #3: write ONE line of code that would print an O in the middle box

//        board[GRID_SIZE / 2][GRID_SIZE / 2] = O;
    }

    /*
     * Task #4: This method still needs to be written use boolean logic in an
     * if/else loop to process one click.
     */
    void processClick(int x, int y) {

        // won't let you click on or change a cell
        if (board[x][y] != EMPTY)
            return; // meaning move on to the next part of the method

        //add if/else loop here:

        if (isXTurn) {
            board[x][y] = X;
        }
        else {
            board[x][y] = O;
        }
        isXTurn = !isXTurn;


//        showMessage("Put a winning message here");
    }

    /*
     * Task #5: This method still needs to be written
     */
    void checkWin() {
        boolean xWin = false;
        boolean oWin = false;
        boolean hasEmptySquare = false;

        for (int x = 0; x < GRID_SIZE; x++) {
            for (int y = 0; y < GRID_SIZE; y++) {

                if (board[x][y] != X && board[x][y] != O) {
                    hasEmptySquare = true;
                    break;
                }

            }
            if (hasEmptySquare) {
                break;
            }
        }

        // check sum on each row

        for (int x = 0; x < GRID_SIZE; x++) {
            int xSum = 0;
            int oSum = 0;
            for (int y = 0; y < GRID_SIZE; y++) {

                if (board[x][y] == X) {
                    xSum++;
                }
                if (board[x][y] == O) {
                    oSum++;
                }

            }

            if (xSum == WIN_LENGTH) {
                xWin = true;
            }
            if (oSum == WIN_LENGTH) {
                oWin = true;
            }
        }

        for (int y = 0; y < GRID_SIZE; y++) {
            int xSum = 0;
            int oSum = 0;
            for (int x = 0; x < GRID_SIZE; x++) {

                if (board[x][y] == X) {
                    xSum++;
                }
                else if (board[x][y] == O) {
                    oSum++;
                }

            }

            if (xSum == WIN_LENGTH) {
                xWin = true;
            }
            if (oSum == WIN_LENGTH) {
                oWin = true;
            }
        }

        // check sum on one diagonal

        {
            int xSum = 0;
            int oSum = 0;
            for (int i = 0; i < GRID_SIZE; i++) {

                if (board[i][i] == X) {
                    xSum++;
                }
                if (board[i][i] == O) {
                    oSum++;
                }

            }

            if (xSum == WIN_LENGTH) {
                xWin = true;
            }
            if (oSum == WIN_LENGTH) {
                oWin = true;
            }
        }
        // check sum on the other diagonal

        {
            int xSum = 0;
            int oSum = 0;
            for (int i = 0; i < GRID_SIZE; i++) {

                if (board[i][GRID_SIZE - 1 - i] == X) {
                    xSum++;
                }
                if (board[i][GRID_SIZE - 1 - i] == O) {
                    oSum++;
                }

            }

            if (xSum == WIN_LENGTH) {
                xWin = true;
            }
            if (oSum == WIN_LENGTH) {
                oWin = true;
            }
        }

        if (xWin) {
            showMessage("X Wins!");
            timer();
        }
        else if (oWin) {
            showMessage("O Wins!");
            timer();
        }
        else if (!hasEmptySquare) {
            showMessage("It's a draw.");
            timer();
        }

    }

    void createAndShowGUI() {
        JFrame frame = new JFrame("TicTacToe");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 520);
        frame.setLocationRelativeTo(null);

        topLabel = new JLabel("Enter status messages here");
        topLabel.setFont(new Font("Sans Serif", Font.BOLD, 20));
        topLabel.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.BLACK));
        topLabel.setOpaque(true);
        topLabel.setForeground(Color.RED);
        topLabel.setVisible(false);

        grpanel = new GraphicsPanel();
        grpanel.add(topLabel);
        frame.add(grpanel);
        frame.setVisible(true);
    }

    // method that will print whatever message you would like to when the game is
    // over
    void showMessage(String text) {

        topLabel.setText("    " + text + "    ");
        topLabel.setVisible(true);

        Timer timer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                topLabel.setVisible(false);
            }
        });

        timer.setRepeats(false);
        timer.start();
    }

    void timer() {
        Timer winTimer = new Timer(1500, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setupGame();
                grpanel.repaint();
            }
        });
        winTimer.setRepeats(false);
        winTimer.start();
    }

    private class GraphicsPanel extends JPanel implements MouseListener {

        int jPanelWidth, jPanelHeight;
        int blockX, blockY;
        int spacingAmount; // spacing amount

        GraphicsPanel() {
            this.addMouseListener(this);
            this.setBackground(new Color(255, 255, 230));
        }

        void initGraphics() {
            jPanelWidth = (int) this.getSize().getWidth();
            jPanelHeight = (int) this.getSize().getHeight();
            blockX = (int) ((jPanelWidth / GRID_SIZE) + 0.5);
            blockY = (int) ((jPanelHeight / GRID_SIZE) + 0.5);
            spacingAmount = (int) (blockX * 0.1); // 10%
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            initGraphics(); // doing this every time the screen is redrawn allows the window to be resized
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2.setStroke(new BasicStroke(2.0f));

            if (isXTurn) {
                setBackground(new Color(0xFF8492));
            }
            else {
                setBackground(new Color(0x84D0FF));
            }

            g2.setColor(Color.GRAY);
            for (int i = 0; i < GRID_SIZE; i++) {
                g2.drawLine(blockX * i, 0, blockX * i, jPanelHeight);
                g2.drawLine(0, blockY * i, jPanelWidth, blockY * i);
            }

            g2.setStroke(new BasicStroke(4.0f));
            for (int i = 0; i < GRID_SIZE; i++) {
                for (int j = 0; j < GRID_SIZE; j++) {
                    if (board[i][j] == X) {
                        g2.setColor(Color.RED);
                        g2.drawLine(blockX * i + spacingAmount, blockY * j + spacingAmount, blockX * (i + 1) - spacingAmount, blockY * (j + 1) - spacingAmount);
                        g2.drawLine(blockX * (i + 1) - spacingAmount, blockY * j + spacingAmount, blockX * i + spacingAmount, blockY * (j + 1) - spacingAmount);
                    }
                    if (board[i][j] == O) {
                        g2.setColor(Color.BLUE);
                        g2.drawOval(blockX * i + spacingAmount, blockY * j + spacingAmount, blockX - spacingAmount * 2, blockY - spacingAmount * 2);
                    }
                }
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            // calculate which square you clicked on
            processClick((x / blockX), (y / blockY));
            grpanel.repaint();
            checkWin();

        }

        @Override
        public void mousePressed(MouseEvent e) {}

        @Override
        public void mouseReleased(MouseEvent e) {}

        @Override
        public void mouseEntered(MouseEvent e) {}

        @Override
        public void mouseExited(MouseEvent e) {}
    }

    public static void main(String[] cheese) {
        new TicTacToe4C();
    }
}



