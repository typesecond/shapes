// Virginia Tech Honor Code Pledge:
//
// This is a submission for project 2 (Spring 2025)
// As a Hokie, I will conduct myself with honor and integrity at all times.
// I will not lie, cheat, or steal, nor will I accept the actions of
// those who do.
// -- Kyler O'Rourke (korourke) 



package game; 

import static org.junit.Assert.*;
import org.junit.Test;
import game.WhackAShape;
import game.GameShape;

public class WhackAShapeTest {

    @Test
    public void testConstructorWithValidInputs() {
        String[] inputs = {"red circle", "blue square",
        		"red square", "blue circle"};
        WhackAShape game = new WhackAShape(inputs);
        // The constructor calls displayNextShape,
        // which does not remove a shape from the bag.
        int expectedSize = inputs.length;
        assertEquals("Bag should have the expected number"
                  + " of shapes after initialization",
                       expectedSize, game.getBag().getCurrentSize());
    }

    @Test
    public void testConstructorWithInvalidInputs() {
        String[] inputs = {"invalid shape", "another invalid"};
        WhackAShape game = new WhackAShape(inputs);
        // Since the inputs are invalid, no shapes should be added.
        assertEquals("Bag should be empty when all inputs are"
             + " invalid", 0, game.getBag().getCurrentSize());
    }

    @Test
    public void testHandleShapeClickRemovesShape() {
        String[] inputs = {"red circle", "blue square"};
        WhackAShape game = new WhackAShape(inputs);
        int initialSize = game.getBag().getCurrentSize();
        // simulate a clik on one shape from the bag.
        GameShape shape = game.getBag().pick();
        assertNotNull("A valid shape should be picked from the bag", shape);
        game.handleShapeClick(shape);
        int newSize = game.getBag().getCurrentSize();
        assertEquals("Bag size should decrease one after a shape is clicked",
                     initialSize - 1, newSize);
    }
}
