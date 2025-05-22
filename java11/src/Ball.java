import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Ball creates one Ball object. The object is set to a random size.
 * It has a method that makes it move, as well as a method to repaint its appearance.
 */

public class Ball {
    //starting with 6 variable declarations
    private Dimension screenSize;//holds the dimensions of the window that the ball can bounce around in
    private Color color; //holds the color of the ball
    private Point location; //holds the ball's current location
    private int radius; //holds the radius of the ball
    private int diameter; // holds the diameter of the ball
    private Point speed; //holds the ball's current speed

    private static BufferedImage logo;

    //Ball constructor
    public Ball(Dimension screenSize) {
        this.screenSize = screenSize; //Having this in front refers to the variable. The parameter value is after the equal sign
        radius = ((int)(Math.random()*40))+10; //Math.random() chooses a double b/t 0 & 1 AND the int casts the double to an int

        location = new Point(screenSize.width/2,screenSize.height/2); //sets ball's start to middle
        color = new Color((float)Math.random(),(float)Math.random(),(float)Math.random()); //sets random colour of ball
        speed = new Point(1 + ((int)(Math.random()*10)), 1 + ((int)(Math.random()*10))); //sets speed of ball
    }

    public int getDiameter() {//method to get diameter
        return radius * 2;
    }

    public Color getColor() {//method to get color
        return color;
    }

    public Point getLocation() {//method to get the location
        return location;
    }

    public Point getSpeed() {//method to get the speed of the ball
        return speed;
    }

    public void move() {
        location.setLocation(location.x+speed.x,location.y+speed.y);
        if ( (location.x < 0) || (location.x > screenSize.width-diameter) )
            speed.x = -getSpeed().x;
        if ( (location.y < 0) || (location.y > screenSize.height-diameter) )
            speed.y = -getSpeed().y;
    }

    private BufferedImage getLogo() {
        if (logo == null) {
            try {
                logo = ImageIO.read(new File("java11/DVD_logo.svg.png"));
            }
            catch (IOException ignored) {}
        }
        return logo;
    }

    public void paint(Graphics2D g2d) {
        g2d.setColor(color);
//        g2d.fillOval(location.x, location.y, getDiameter(), getDiameter());
        g2d.drawImage(getLogo(), location.x, location.y, getDiameter(), getDiameter(), null);
    }
}
