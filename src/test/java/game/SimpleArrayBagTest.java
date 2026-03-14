// Virginia Tech Honor Code Pledge:
//
// This is a submission for project 2 (Spring 2025)
// As a Hokie, I will conduct myself with honor and integrity at all times.
// I will not lie, cheat, or steal, nor will I accept the actions of
// those who do.
// -- Kyler O'Rourke (korourke) 


package game;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import bag.SimpleBagInterface;

/**
 * Test class for SimpleArrayBag.
 *
 * This class contains test cases to verify that the 
 * SimpleArrayBag class behaves as expected.
 *
 * @author Kyler O'Rourke (korourke)
 * @version 2025-09-30
 */
public class SimpleArrayBagTest {

    private SimpleBagInterface<Integer> bag;

    /**
     * sets up the test environment.
     */
    @Before
    public void setUp() {
        bag = new SimpleArrayBag<Integer>();
    }

    /**
     * test for the initial empty state of the bag.
     */
    @Test
    public void testIsEmptyInitially() {
        assertTrue("Bag should be empty initially", bag.isEmpty());
    }

    /**
     * test adding a non-null element to the bag.
     */
    @Test
    public void testAddNonNull() {
        assertTrue("Adding a non-null element should succeed", bag.add(42));
        assertFalse("Bag not be empty after adding element", bag.isEmpty());
        assertEquals("Current size should be 1", 1, bag.getCurrentSize());
    }

    /**
     * test adding a null element to the bag.
     */
    @Test
    public void testAddNull() {
        assertFalse("Adding null should return false", bag.add(null));
        assertTrue("Bag should beempty after trying add null", bag.isEmpty());
    }

    /**
     * test removing an existing item from the bag.
     */
    @Test
    public void testRemoveExistingItem() {
        bag.add(10);
        bag.add(20);
        bag.add(30);
        boolean removed = bag.remove(20);
        assertTrue("Removing an existing item should return true", removed);
        assertEquals("Current size 2 after removal", 2, bag.getCurrentSize());
    }

    /**
     * test removing a non-existing item from the bag.
     */
    @Test
    public void testRemoveNonExistingItem() {
        bag.add(10);
        bag.add(20);
        boolean removed = bag.remove(30);
        assertFalse("Removing a non-existing item return false", removed);
        assertEquals("Current size should remain 2", 2, bag.getCurrentSize());
    }

    /**
     * test picking an item from the bag.
     */
    @Test
    public void testPickReturnsItem() {
        bag.add(1);
        bag.add(2);
        bag.add(3);
        Integer picked = bag.pick();
        assertNotNull("Picked item should not be null", picked);
        assertTrue("Picked item should be one of the added elements",
                   (picked.equals(1) || picked.equals(2) || picked.equals(3)));
    }

    /**
     * test picking from an empty bag.
     */
    @Test
    public void testPickFromEmptyBag() {
        assertNull("Picking from an empty bag should return null", bag.pick());
    }

    /**
     * testss the bag's maximum capacity.
     */
    @Test
    public void testBagMaxCapacity() {
        // According to SimpleArrayBag, the maximum capacity is 18.
        for (int i = 0; i < 18; i++) {
            assertTrue("Should be able to add element " + i, bag.add(i));
        }
        assertFalse("Adding element when bag full return false", bag.add(100));
        assertEquals("Current size should be 18", 18, bag.getCurrentSize());
    }
}
