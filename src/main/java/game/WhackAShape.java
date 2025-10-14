package game;

import bag.SimpleBagInterface;
import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.swing.JOptionPane;

public class WhackAShape {
    private static final String[] STRINGS = {
        "red circle", "blue circle",
        "red square", "blue square"
    };

    private static final int PANEL_WIDTH = 600;
    private static final int PANEL_HEIGHT = 600;
    private static final Random RAND = new Random();

    private SimpleBagInterface<GameShape> bag;
    private GameWindow window;
    private int score;
    private String targetWord;
    private List<GameShape> wordShapes;

    public WhackAShape() {
        setupGameWithWord();
        displayNextShape();
    }

    public WhackAShape(String[] inputs) {
        setupGameBase();
        for (String input : inputs) {
            try {
                GameShape shape = createShape(input);
                bag.add(shape);
            } catch (IllegalArgumentException e) {
                System.err.println("Invalid shape input: " + input);
            }
        }
        wordShapes = new ArrayList<>();
        displayNextShape();
    }

    private void setupGameBase() {
        bag = new SimpleArrayBag<GameShape>();
        window = new GameWindow(
            "Whack A Shape",
            PANEL_WIDTH,
            PANEL_HEIGHT,
            this::handleShapeClick
        );
        score = 0;
        window.setScore(score);
    }

    private void setupGameWithWord() {
        setupGameBase();
        targetWord = promptWord();
        wordShapes = buildWordShapes(targetWord);
        if (wordShapes.isEmpty()) {
            int size = RAND.nextInt(9) + 6; // randomly generate between 6 and 14 shapes
            for (int i = 0; i < size; i++) {
                String shapeType = STRINGS[RAND.nextInt(STRINGS.length)];
                GameShape shape = createShape(shapeType);
                bag.add(shape);
            }
        } else {
            for (GameShape shape : wordShapes) {
                bag.add(shape);
            }
        }
    }

    private GameShape createShape(String description) {
        int size = RAND.nextInt(101) + 100; // rand size between 100 and 200 pixels
        int maxX = Math.max(1, window.getPanelWidth() - size);
        int maxY = Math.max(1, window.getPanelHeight() - size);
        int x = RAND.nextInt(maxX);
        int y = RAND.nextInt(maxY);
        Color color = description.contains("red") ? Color.RED : Color.BLUE;

        GameShape.Type type;
        if (description.contains("circle")) {
            type = GameShape.Type.CIRCLE;
        } else if (description.contains("square")) {
            type = GameShape.Type.SQUARE;
        } else {
            throw new IllegalArgumentException("Invalid shape type: "
        + description);
        }
        return new GameShape(type, color, x, y, size);
    }

    public void handleShapeClick(GameShape shape) {
        bag.remove(shape);
        score++;
        window.setScore(score);
        displayNextShape();
    }

    private void displayNextShape() {
        GameShape nextShape = bag.pick();
        if (nextShape == null) {
            displayWinMessage();
        } else {
            window.setShape(nextShape);
        }
    }

    private void displayWinMessage() {
        if (!wordShapes.isEmpty()) {
            window.playWordAnimation(wordShapes, targetWord);
        } else {
            window.showWinMessage();
        }
    }

    public GameWindow getWindow() {
        return window;
    }

    public SimpleBagInterface<GameShape> getBag() {
        return bag;
    }

    public void show() {
        window.show();
    }

    private String promptWord() {
        String fallback = "WIN";
        if (GraphicsEnvironment.isHeadless()) {
            return fallback;
        }
        String input = JOptionPane.showInputDialog(
            null,
            "Type a word (A-Z, 0-9):",
            "Word to Build",
            JOptionPane.QUESTION_MESSAGE
        );
        if (input == null) {
            return fallback;
        }
        String cleaned = sanitizeWord(input);
        return cleaned.isEmpty() ? fallback : cleaned;
    }

    private String sanitizeWord(String input) {
        String upper = input.toUpperCase();
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < upper.length(); i++) {
            char c = upper.charAt(i);
            if ((c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9')) {
                out.append(c);
            }
            if (out.length() >= 12) {
                break;
            }
        }
        return out.toString();
    }

    private List<GameShape> buildWordShapes(String word) {
        Map<Character, String[]> font = buildFont();
        List<GameShape> shapes = new ArrayList<>();
        if (word == null || word.isEmpty()) {
            return shapes;
        }

        int rows = 7;
        int cols = 0;
        for (int i = 0; i < word.length(); i++) {
            cols += 5;
            if (i < word.length() - 1) {
                cols += 1;
            }
        }

        int margin = 40;
        int cellSize = Math.min(
            (PANEL_WIDTH - margin * 2) / Math.max(cols, 1),
            (PANEL_HEIGHT - margin * 2) / rows
        );
        cellSize = Math.max(10, Math.min(cellSize, 28));

        int totalWidth = cols * cellSize;
        int totalHeight = rows * cellSize;
        int startX = (PANEL_WIDTH - totalWidth) / 2;
        int startY = (PANEL_HEIGHT - totalHeight) / 2;

        int xCursor = startX;
        Color[] palette = {
            new Color(255, 86, 86),
            new Color(86, 160, 255),
            new Color(255, 196, 86),
            new Color(124, 216, 124)
        };

        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            String[] glyph = font.get(c);
            if (glyph == null) {
                xCursor += cellSize * 6;
                continue;
            }
            Color color = palette[i % palette.length];
            for (int row = 0; row < rows; row++) {
                String line = glyph[row];
                for (int col = 0; col < 5; col++) {
                    if (line.charAt(col) == '1') {
                        int x = xCursor + col * cellSize;
                        int y = startY + row * cellSize;
                        shapes.add(new GameShape(
                            GameShape.Type.SQUARE,
                            color,
                            x,
                            y,
                            cellSize - 2
                        ));
                    }
                }
            }
            xCursor += cellSize * 6;
        }
        return shapes;
    }

    private Map<Character, String[]> buildFont() {
        Map<Character, String[]> font = new HashMap<>();
        font.put('A', new String[] {
            "01110",
            "10001",
            "10001",
            "11111",
            "10001",
            "10001",
            "10001"
        });
        font.put('B', new String[] {
            "11110",
            "10001",
            "10001",
            "11110",
            "10001",
            "10001",
            "11110"
        });
        font.put('C', new String[] {
            "01110",
            "10001",
            "10000",
            "10000",
            "10000",
            "10001",
            "01110"
        });
        font.put('D', new String[] {
            "11110",
            "10001",
            "10001",
            "10001",
            "10001",
            "10001",
            "11110"
        });
        font.put('E', new String[] {
            "11111",
            "10000",
            "10000",
            "11110",
            "10000",
            "10000",
            "11111"
        });
        font.put('F', new String[] {
            "11111",
            "10000",
            "10000",
            "11110",
            "10000",
            "10000",
            "10000"
        });
        font.put('G', new String[] {
            "01110",
            "10001",
            "10000",
            "10111",
            "10001",
            "10001",
            "01110"
        });
        font.put('H', new String[] {
            "10001",
            "10001",
            "10001",
            "11111",
            "10001",
            "10001",
            "10001"
        });
        font.put('I', new String[] {
            "11111",
            "00100",
            "00100",
            "00100",
            "00100",
            "00100",
            "11111"
        });
        font.put('J', new String[] {
            "11111",
            "00010",
            "00010",
            "00010",
            "10010",
            "10010",
            "01100"
        });
        font.put('K', new String[] {
            "10001",
            "10010",
            "10100",
            "11000",
            "10100",
            "10010",
            "10001"
        });
        font.put('L', new String[] {
            "10000",
            "10000",
            "10000",
            "10000",
            "10000",
            "10000",
            "11111"
        });
        font.put('M', new String[] {
            "10001",
            "11011",
            "10101",
            "10101",
            "10001",
            "10001",
            "10001"
        });
        font.put('N', new String[] {
            "10001",
            "11001",
            "10101",
            "10011",
            "10001",
            "10001",
            "10001"
        });
        font.put('O', new String[] {
            "01110",
            "10001",
            "10001",
            "10001",
            "10001",
            "10001",
            "01110"
        });
        font.put('P', new String[] {
            "11110",
            "10001",
            "10001",
            "11110",
            "10000",
            "10000",
            "10000"
        });
        font.put('Q', new String[] {
            "01110",
            "10001",
            "10001",
            "10001",
            "10101",
            "10010",
            "01101"
        });
        font.put('R', new String[] {
            "11110",
            "10001",
            "10001",
            "11110",
            "10100",
            "10010",
            "10001"
        });
        font.put('S', new String[] {
            "01111",
            "10000",
            "10000",
            "01110",
            "00001",
            "00001",
            "11110"
        });
        font.put('T', new String[] {
            "11111",
            "00100",
            "00100",
            "00100",
            "00100",
            "00100",
            "00100"
        });
        font.put('U', new String[] {
            "10001",
            "10001",
            "10001",
            "10001",
            "10001",
            "10001",
            "01110"
        });
        font.put('V', new String[] {
            "10001",
            "10001",
            "10001",
            "10001",
            "10001",
            "01010",
            "00100"
        });
        font.put('W', new String[] {
            "10001",
            "10001",
            "10001",
            "10101",
            "10101",
            "10101",
            "01010"
        });
        font.put('X', new String[] {
            "10001",
            "10001",
            "01010",
            "00100",
            "01010",
            "10001",
            "10001"
        });
        font.put('Y', new String[] {
            "10001",
            "10001",
            "01010",
            "00100",
            "00100",
            "00100",
            "00100"
        });
        font.put('Z', new String[] {
            "11111",
            "00001",
            "00010",
            "00100",
            "01000",
            "10000",
            "11111"
        });
        font.put('0', new String[] {
            "01110",
            "10001",
            "10011",
            "10101",
            "11001",
            "10001",
            "01110"
        });
        font.put('1', new String[] {
            "00100",
            "01100",
            "00100",
            "00100",
            "00100",
            "00100",
            "01110"
        });
        font.put('2', new String[] {
            "01110",
            "10001",
            "00001",
            "00010",
            "00100",
            "01000",
            "11111"
        });
        font.put('3', new String[] {
            "11110",
            "00001",
            "00001",
            "01110",
            "00001",
            "00001",
            "11110"
        });
        font.put('4', new String[] {
            "00010",
            "00110",
            "01010",
            "10010",
            "11111",
            "00010",
            "00010"
        });
        font.put('5', new String[] {
            "11111",
            "10000",
            "10000",
            "11110",
            "00001",
            "00001",
            "11110"
        });
        font.put('6', new String[] {
            "01110",
            "10000",
            "10000",
            "11110",
            "10001",
            "10001",
            "01110"
        });
        font.put('7', new String[] {
            "11111",
            "00001",
            "00010",
            "00100",
            "01000",
            "01000",
            "01000"
        });
        font.put('8', new String[] {
            "01110",
            "10001",
            "10001",
            "01110",
            "10001",
            "10001",
            "01110"
        });
        font.put('9', new String[] {
            "01110",
            "10001",
            "10001",
            "01111",
            "00001",
            "00001",
            "01110"
        });
        return font;
    }
}
