import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

public class GridGamePanel extends JPanel {

    private static final int GRID_WIDTH = 4;
    private static final int GRID_HEIGHT = 4;

    private static BufferedImage getImage(String path) {
        BufferedImage tmp;

//        try {
//            tmp = ImageIO.read(Objects.requireNonNull(GridGamePanel.class.getResourceAsStream(path)));
//        }
//        catch (IOException | NullPointerException e) {
//            e.printStackTrace();
//        }

        tmp = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = tmp.createGraphics();
        g2.setColor(Color.MAGENTA);
        g2.fillRect(0, 0, 16, 16);
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, 8, 8);
        g2.fillRect(8, 8, 16, 16);
        g2.dispose();

        return tmp;
    }

    private enum TileState {

        SET(getImage("")),
        UNSET(getImage(""));

        private final BufferedImage image;

        TileState(BufferedImage image) {
            this.image = image;
        }
    }

    private TileState[][] tiles;

    public GridGamePanel() {
        setPreferredSize(new Dimension(GRID_WIDTH * 100, GRID_HEIGHT * 100));
        resetGame(GRID_WIDTH, GRID_HEIGHT);
    }

    private void resetGame(int width, int height) {

        GridLayout layout = new GridLayout();
        setLayout(layout);

        tiles = new TileState[height][width];

        layout.setRows(height);
        layout.setColumns(width);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                final int finalX = x;
                final int finalY = y;

                JButton button = new JButton() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        if (tiles[finalY][finalX] == null) {
                            tiles[finalY][finalX] = TileState.SET;
                        }
                        Image buf = tiles[finalY][finalX].image;
                        if (buf != null) {
                            setIcon(new ImageIcon(buf.getScaledInstance(getHeight(), getHeight(), BufferedImage.SCALE_SMOOTH)));
                        }
                        super.paintComponent(g);
                    }
                };

                button.addActionListener(e -> {
                    // update state (game logic)
                    handleClick(finalX, finalY);
                });

                add(button);
            }
        }

    }

    public void handleClick(int x, int y) {

        switch (tiles[y][x]) {

        }

    }

    public void gameLoop() {

    }

}
