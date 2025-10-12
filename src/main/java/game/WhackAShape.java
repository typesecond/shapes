package game;

import bag.SimpleBagInterface;
import java.awt.Color;
import java.util.Random;

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

    public WhackAShape() {
        this(RAND);
    }

    public WhackAShape(String[] inputs) {
        setupGame();
        for (String input : inputs) {
            try {
                GameShape shape = createShape(input);
                bag.add(shape);
            } catch (IllegalArgumentException e) {
                System.err.println("Invalid shape input: " + input);
            }
        }
        displayNextShape();
    }

    private WhackAShape(Random rand) {
        setupGame();
        int size = rand.nextInt(9) + 6; // randomly generate between 6 and 14 shapes
        for (int i = 0; i < size; i++) {
            String shapeType = STRINGS[rand.nextInt(STRINGS.length)];
            GameShape shape = createShape(shapeType);
            bag.add(shape);
        }
        displayNextShape();
    }

    private void setupGame() {
        bag = new SimpleArrayBag<GameShape>();
        window = new GameWindow(
            "Whack A Shape",
            PANEL_WIDTH,
            PANEL_HEIGHT,
            this::handleShapeClick
        );
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
        window.showWinMessage();
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
}
