
package game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.util.function.Consumer;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * simple Swing window to display and click shapes.
 */
public class GameWindow {
    private final JFrame frame;
    private final GamePanel panel;
    private final int panelWidth;
    private final int panelHeight;

    public GameWindow(
        String title,
        int panelWidth,
        int panelHeight,
        Consumer<GameShape> onShapeClick
    ) {
        this.panelWidth = panelWidth;
        this.panelHeight = panelHeight;
        if (GraphicsEnvironment.isHeadless()) {
            this.panel = null;
            this.frame = null;
            return;
        }

        this.panel = new GamePanel(panelWidth, panelHeight, onShapeClick);
        this.frame = new JFrame(title);

        JButton quit = new JButton("Quit");
        quit.addActionListener(e -> System.exit(0));

        frame.setLayout(new BorderLayout());
        frame.add(panel, BorderLayout.CENTER);
        frame.add(quit, BorderLayout.SOUTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
    }

    public void show() {
        if (frame == null) {
            return;
        }
        SwingUtilities.invokeLater(() -> frame.setVisible(true));
    }

    public void setShape(GameShape shape) {
        if (panel != null) {
            panel.setShape(shape);
        }
    }

    public void showWinMessage() {
        if (panel != null) {
            panel.showWinMessage("You Win!");
        }
    }

    public int getPanelWidth() {
        return panelWidth;
    }

    public int getPanelHeight() {
        return panelHeight;
    }

    private static class GamePanel extends JPanel {
        private final int width;
        private final int height;
        private final Consumer<GameShape> onShapeClick;
        private GameShape shape;
        private String message;

        GamePanel(int width, int height, Consumer<GameShape> onShapeClick) {
            this.width = width;
            this.height = height;
            this.onShapeClick = onShapeClick;
            setBackground(Color.WHITE);
            setPreferredSize(new Dimension(width, height));
            addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    if (shape != null && shape.contains(e.getX(), e.getY())) {
                        onShapeClick.accept(shape);
                    }
                }
            });
        }

        void setShape(GameShape shape) {
            this.shape = shape;
            this.message = null;
            repaint();
        }

        void showWinMessage(String message) {
            this.shape = null;
            this.message = message;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            if (message != null) {
                g2.setColor(Color.BLACK);
                g2.setFont(new Font("SansSerif", Font.BOLD, 36));
                FontMetrics fm = g2.getFontMetrics();
                int x = (width - fm.stringWidth(message)) / 2;
                int y = (height - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(message, x, y);
            } else if (shape != null) {
                shape.draw(g2);
            }
        }
    }
}
