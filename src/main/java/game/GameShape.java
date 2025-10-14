package game;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * simple shape model used by the game.
 */
public class GameShape {
    public enum Type {
        CIRCLE,
        SQUARE
    }

    private final Type type;
    private final Color color;
    private final int x;
    private final int y;
    private final int size;

    public GameShape(Type type, Color color, int x, int y, int size) {
        this.type = type;
        this.color = color;
        this.x = x;
        this.y = y;
        this.size = size;
    }

    public Type getType() {
        return type;
    }

    public Color getColor() {
        return color;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSize() {
        return size;
    }

    public boolean contains(int px, int py) {
        if (type == Type.SQUARE) {
            return px >= x && px <= x + size && py >= y && py <= y + size;
        }
        // circle
        int cx = x + size / 2;
        int cy = y + size / 2;
        int dx = px - cx;
        int dy = py - cy;
        int radius = size / 2;
        return dx * dx + dy * dy <= radius * radius;
    }

    public void draw(Graphics2D g2) {
        drawAt(g2, x, y);
    }

    public void drawShadow(Graphics2D g2, int offset, Color shadowColor) {
        drawShadowAt(g2, x, y, offset, shadowColor);
    }

    public int getCenterX() {
        return x + size / 2;
    }

    public int getCenterY() {
        return y + size / 2;
    }

    public void drawAt(Graphics2D g2, int drawX, int drawY) {
        g2.setColor(color);
        if (type == Type.CIRCLE) {
            g2.fillOval(drawX, drawY, size, size);
        } else {
            g2.fillRect(drawX, drawY, size, size);
        }
    }

    public void drawShadowAt(Graphics2D g2, int drawX, int drawY, int offset, Color shadowColor) {
        g2.setColor(shadowColor);
        int sx = drawX + offset;
        int sy = drawY + offset;
        if (type == Type.CIRCLE) {
            g2.fillOval(sx, sy, size, size);
        } else {
            g2.fillRect(sx, sy, size, size);
        }
    }
}
