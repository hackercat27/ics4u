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

@SuppressWarnings("unused")
public class ConnectFour {

    /*
     * Task #4:
     * Make the board a 6x7 board instead of a 7x7 square
     */
    static final int BOARD_ROWS = 6; // 6 rows
    static final int BOARD_COLS = 7; // 7 columns
    static final int EMPTY = 0;
    static final int RED = 1;
    static final int YELLOW = -1;
    static final int CONNECT_LENGTH = 4;

    int[][] board = new int[BOARD_ROWS][BOARD_COLS];
    boolean turn = true; // true = RED turn;
    boolean checkWinner = false; //recommended for checkWin() method
    boolean boardFull = false; //recommended for checkWin() method
    boolean isTie = false;//recommended for checkWin() method
    boolean isEmpty = false;

    JLabel topLabel;
    GraphicsPanel grpanel;

    int redWins = 0;
    int yellowWins = 0;

    ConnectFour() {
        setupGame();
        createAndShowGUI();
    }

    void setupGame() {
        // clear board
        for (int i = 0; i < BOARD_ROWS; i++) {
            for (int j = 0; j < BOARD_COLS; j++) {
                board[i][j] = EMPTY;
            }
        }
        checkWinner = false;
        boardFull = false;
        isTie = false;
        turn = true;
    }

    boolean emptySquare(int rx, int cy) {
        return board[rx][cy] == EMPTY;
    }
    /*
     * Task #6: Currently, this method will just make a circle appear in the cell
     * that is clicked. Can you make the circle "drop" to the bottom most cell,
     * like it would in a real connect4 game?
     * Di Stefano could figure this out without Google!! So can you!!
     */

    void processClick(int row, int col) {
        if(checkWinner) return; // don't allow click after win
        // find the bottom most empty row in this column
        int dropRow = -1;
        for (int r = BOARD_ROWS - 1; r >= 0; r--) {
            if (board[r][col] == EMPTY) {
                dropRow = r;
                break;
            }
        }
        if (dropRow == -1)
            return; // column full

        if (turn) {
            board[dropRow][col] = RED;
        } else {
            board[dropRow][col] = YELLOW;
        }
        turn = !turn;
    }

    /*
     *  Task #5:
     *  Write this method. You win if you have four in a row of the same colour
     *  vertically, horizontally or diagonally.
     *  Can you use recurssion?? nope!
     */

    void checkWin() {
        for (int row = 0; row < BOARD_ROWS; row++) {
            for (int col = 0; col < BOARD_COLS; col++) {
                int player = board[row][col];
                if (player == EMPTY) continue;

                if (containsWinLine(row, col, 1, 0, player)
                        || containsWinLine(row, col, 0, 1, player)
                        || containsWinLine(row, col, 1, 1, player)
                        || containsWinLine(row, col, 1, -1, player)) {
                    checkWinner = true;
                    if (player == RED) {
                        redWins++;
                        showMessage("RED wins! RED-YELLOW: " + redWins + "-" + yellowWins);
                    } else {
                        yellowWins++;
                        showMessage("YELLOW wins! RED-YELLOW: " + redWins + "-" + yellowWins);
                    }
                    timer();
                    return;
                }
            }
        }

        // check for tie if the board is full
        boolean full = true;
        for (int col = 0; col < BOARD_COLS; col++) {
            if (board[0][col] == EMPTY) {
                full = false;
                break;
            }
        }
        if (full) {
            isTie = true;
            showMessage("Game is a TIE!");
            timer();
        }
    }

    boolean containsWinLine(int row, int col, int rowDelta, int colDelta, int player) {
        for (int i = 0; i < CONNECT_LENGTH; i++) {
            int r = row + i * rowDelta;
            int c = col + i * colDelta;
            if (r < 0 || r >= BOARD_ROWS || c < 0 || c >= BOARD_COLS) {
                return false; // out of bounds
            }
            if (board[r][c] != player) {
                return false;
            }
        }
        return true;
    }

    /*
     * Task #8: Display a "colour" won message
     */

    /*
     * Task #9: Keep track of overall wins/losses for each colour.
     * Display this stat when displaying the "colour won" message.
     */

    /*
     * Task #3:
     * Write a comment for each line of code in this method explaining its purpose.
     * This will demonstrate your understanding of our Layouts logic from an earlier concept.
     */

    void createAndShowGUI() {
        /*
         * curiously, no layout manager is ever used in this method.
         * good thing i don't know how they work.
         */

        // make the frame
        JFrame frame = new JFrame("TicTacToe");

        // make the program call System.exit() when the x button is clicked
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // set the size (note, should probably be replaced with frame.pack() )
        frame.setSize(500, 520);

        // put it in the middle of the screen
        frame.setLocationRelativeTo(null);

        // initialize the
        topLabel = new JLabel("Enter status messages here");
        topLabel.setFont(new Font("Sans Serif", Font.BOLD, 20));
        topLabel.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.BLACK));
        topLabel.setOpaque(true);
        topLabel.setForeground(Color.RED);
        topLabel.setVisible(false);

        // init the graphics panel and add it to the frame.
        // also add the label to the graphics panel.
        grpanel = new GraphicsPanel();
        grpanel.add(topLabel);
        frame.add(grpanel);

        // show the window
        frame.setVisible(true);

    }

    void showMessage(String text) {
        topLabel.setText("    " + text + "    ");
        topLabel.setVisible(true);

        Timer timer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                topLabel.setVisible(false);

                // repaint
                // no more click boolean
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

    @SuppressWarnings("serial")
    class GraphicsPanel extends JPanel implements MouseListener {

        int jpanW, jpanH;
        int squareWidth, squareHeight;
        int spc; // spacing amount

        GraphicsPanel() {
            this.addMouseListener(this);
            this.setBackground(new Color(74, 74, 75));
        }

        void initGraphics() {
            jpanW = this.getSize().width;
            jpanH = this.getSize().height;
            squareWidth = (int) ((jpanW / BOARD_COLS) + 0.5);
            squareHeight = (int) ((jpanH / BOARD_ROWS) + 0.5);
            spc = (int) (squareWidth * 0.1); // 10% spacing buffer
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            initGraphics(); // doing this every time the screen is redrawn allows the window to be resized
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setStroke(new BasicStroke(2.0f));

            g.setColor(Color.GRAY);
            for (int i = 0; i <= BOARD_COLS; i++) {
                g.drawLine(squareWidth * i, 0, squareWidth * i, jpanH);
            }
            for (int i = 0; i <= BOARD_ROWS; i++) {
                g.drawLine(0, squareHeight * i, jpanW, squareHeight * i);
            }
            /*
             * Task #7: Make these ovals look 3D
             */

            for (int i = 0; i < BOARD_ROWS; i++) {
                for (int j = 0; j < BOARD_COLS; j++) {
                    /*
                     * Task #1: Draw a filled in red circle
                     * You may want to look back at our tic tac toe program
                     */

                    if (board[i][j] == RED) {
                        drawPiece(new Color(0xFF0000), new Color(0xA00000), g2, i, j, spc / 2);
                    }
                    /*
                     * Task #2: Draw a filled in yellow circle
                     * You may want to look back at our tic tac toe program
                     */
                    if (board[i][j] == YELLOW) {
                        drawPiece(new Color(0xFAD90A), new Color(0xA08B00), g2, i, j, spc / 2);
                    }
                }
            }
        }

        private void drawPiece(Color color, Color shadow, Graphics2D g2, int row, int col, int off) {
            int baseSizeX = squareWidth - 2 * spc;
            int baseSizeY = squareWidth - 2 * spc;
            int baseX = col * squareWidth + spc;
            int baseY = row * squareHeight + spc;

            // piece's shadow
            g2.setColor(shadow);
            g2.fillOval(baseX + off, baseY + off, baseSizeX, baseSizeY);

            // piece base
            g2.setColor(color);
            g2.fillOval(baseX, baseY, baseSizeX, baseSizeY);
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            // calculate which square you clicked on
            int i = (int) y / squareHeight;
            int j = (int) x / squareWidth;
            processClick(i, j);
            grpanel.repaint();
            checkWin();

        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

    }

    public static void main(String[] cheese) {
        new ConnectFour();
    }
}
