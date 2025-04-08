package geom;

import java.awt.Paint;
import java.awt.TexturePaint;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import org.joml.Vector4d;
import util.Utils;
import window.GraphicsRenderer;

public class Material {

    private BufferedImage texture;
    private Vector4d color;

    public Material(String name, BufferedImage texture) {
        this.texture = texture;
    }

    public Material(String name, Vector4d color) {
        this.color = color;
    }

    public Paint getPaint() {
        if (texture != null) {
            final double scale = GraphicsRenderer.INT_SCALE;
            return new TexturePaint(texture, new Rectangle2D.Double(0, 0, scale, scale));
        }
        return Utils.getColor(color);
    }

}
