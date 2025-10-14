
package game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

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

    public void setScore(int score) {
        if (panel != null) {
            panel.setScore(score);
        }
    }

    public void playWordAnimation(List<GameShape> shapes, String word) {
        if (panel != null) {
            panel.startWordAnimation(shapes, word);
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
        private enum Mode {
            PLAYING,
            WIN,
            ANIMATING
        }

        private static final Color BG = new Color(245, 246, 250);
        private static final Color SHADOW = new Color(0, 0, 0, 40);
        private static final Color SACK = new Color(176, 124, 76);
        private static final Color SACK_DARK = new Color(146, 98, 56);
        private static final int SACK_WIDTH = 140;
        private static final int SACK_HEIGHT = 120;

        private final int width;
        private final int height;
        private final Consumer<GameShape> onShapeClick;
        private final Random rand;
        private GameShape shape;
        private String message;
        private Mode mode;
        private int score;
        private String word;
        private List<AnimatedShape> animatedShapes;
        private Timer timer;
        private long animationStart;

        GamePanel(int width, int height, Consumer<GameShape> onShapeClick) {
            this.width = width;
            this.height = height;
            this.onShapeClick = onShapeClick;
            this.rand = new Random();
            this.mode = Mode.PLAYING;
            this.animatedShapes = new ArrayList<>();
            setBackground(Color.WHITE);
            setPreferredSize(new Dimension(width, height));
            addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    if (mode == Mode.PLAYING && shape != null
                        && shape.contains(e.getX(), e.getY())) {
                        onShapeClick.accept(shape);
                    }
                }
            });
        }

        void setShape(GameShape shape) {
            this.shape = shape;
            this.message = null;
            this.mode = Mode.PLAYING;
            repaint();
        }

        void showWinMessage(String message) {
            this.shape = null;
            this.message = message;
            this.mode = Mode.WIN;
            repaint();
        }

        void setScore(int score) {
            this.score = score;
            repaint();
        }

        void startWordAnimation(List<GameShape> shapes, String word) {
            this.word = word;
            this.shape = null;
            this.message = null;
            this.mode = Mode.ANIMATING;
            this.animatedShapes = new ArrayList<>();
            int mouthX = width / 2;
            int mouthY = 60;
            int maxDelay = 0;
            for (int i = 0; i < shapes.size(); i++) {
                GameShape shape = shapes.get(i);
                int delay = rand.nextInt(500);
                maxDelay = Math.max(maxDelay, delay);
                animatedShapes.add(new AnimatedShape(
                    shape,
                    mouthX - shape.getSize() / 2,
                    mouthY,
                    shape.getX(),
                    shape.getY(),
                    delay
                ));
            }
            final int maxDelayFinal = maxDelay;
            animationStart = System.currentTimeMillis();
            if (timer != null) {
                timer.stop();
            }
            timer = new Timer(16, e -> tickAnimation(maxDelayFinal));
            timer.start();
        }

        private void tickAnimation(int maxDelay) {
            long now = System.currentTimeMillis();
            boolean allSettled = true;
            for (AnimatedShape anim : animatedShapes) {
                if (now - animationStart < anim.delayMs) {
                    allSettled = false;
                    continue;
                }
                if (!anim.settled) {
                    anim.velocity += 0.8f;
                    anim.y += anim.velocity;
                    anim.x += (anim.targetX - anim.x) * 0.08f;
                    if (anim.y >= anim.targetY) {
                        anim.y = anim.targetY;
                        anim.velocity = 0f;
                        anim.settled = true;
                    } else {
                        allSettled = false;
                    }
                }
            }
            if (allSettled && now - animationStart > maxDelay + 400) {
                timer.stop();
                mode = Mode.WIN;
            }
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
            );
            paintBackground(g2);

            if (mode == Mode.ANIMATING) {
                paintSack(g2);
                for (AnimatedShape anim : animatedShapes) {
                    anim.shape.drawShadowAt(g2, (int) anim.x, (int) anim.y, 6, SHADOW);
                    anim.shape.drawAt(g2, (int) anim.x, (int) anim.y);
                }
                paintScore(g2);
                return;
            }

            if (message != null) {
                g2.setColor(Color.BLACK);
                g2.setFont(new Font("SansSerif", Font.BOLD, 36));
                FontMetrics fm = g2.getFontMetrics();
                int x = (width - fm.stringWidth(message)) / 2;
                int y = (height - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(message, x, y);
                if (word != null) {
                    g2.setFont(new Font("SansSerif", Font.PLAIN, 18));
                    FontMetrics small = g2.getFontMetrics();
                    int wx = (width - small.stringWidth(word)) / 2;
                    g2.drawString(word, wx, y + 32);
                }
            } else if (shape != null) {
                shape.drawShadow(g2, 6, SHADOW);
                shape.draw(g2);
            }
            paintScore(g2);
        }

        private void paintBackground(Graphics2D g2) {
            g2.setColor(BG);
            g2.fillRect(0, 0, width, height);
        }

        private void paintScore(Graphics2D g2) {
            g2.setColor(new Color(40, 50, 70));
            g2.setFont(new Font("SansSerif", Font.BOLD, 16));
            g2.drawString("Score: " + score, 14, 24);
        }

        private void paintSack(Graphics2D g2) {
            int x = (width - SACK_WIDTH) / 2;
            int y = 8;
            g2.setColor(SACK_DARK);
            g2.fillOval(x, y + 18, SACK_WIDTH, SACK_HEIGHT);
            g2.setColor(SACK);
            g2.fillRoundRect(x, y + 12, SACK_WIDTH, SACK_HEIGHT, 24, 24);
            g2.setColor(new Color(120, 78, 42));
            g2.fillRect(x + 10, y + 10, SACK_WIDTH - 20, 14);
        }

        private static class AnimatedShape {
            private final GameShape shape;
            private float x;
            private float y;
            private final int targetX;
            private final int targetY;
            private final int delayMs;
            private float velocity;
            private boolean settled;

            AnimatedShape(GameShape shape, int startX, int startY, int targetX, int targetY, int delayMs) {
                this.shape = shape;
                this.x = startX;
                this.y = startY;
                this.targetX = targetX;
                this.targetY = targetY;
                this.delayMs = delayMs;
                this.velocity = 0f;
                this.settled = false;
            }
        }
    }
}
