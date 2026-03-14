// Virginia Tech Honor Code Pledge:
//
// This is a submission for project 2 (Spring 2025)
// As a Hokie, I will conduct myself with honor and integrity at all times.
// I will not lie, cheat, or steal, nor will I accept the actions of
// those who do.
// -- Kyler O'Rourke (korourke)

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
        g2.setColor(color);
        if (type == Type.CIRCLE) {
            g2.fillOval(x, y, size, size);
        } else {
            g2.fillRect(x, y, size, size);
        }
    }
}
