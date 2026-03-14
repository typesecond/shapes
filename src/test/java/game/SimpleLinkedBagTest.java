
// Virginia Tech Honor Code Pledge:
//
// This is a submission for project 2 (Spring 2025)
// As a Hokie, I will conduct myself with honor and integrity at all times.
// I will not lie, cheat, or steal, nor will I
//accept the actions of those who do.
// -- Kyler O'Rourke (korourke) 
 
package game;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;



/**
 * test class for simplelinkedbag.
 * this class contains test cases to verify that the
 * simplelinkedbag class behaves as expected.
 *
 * @author Kyler O'Rourke (korourke)
 * @version 2025-09-27
 */
public class SimpleLinkedBagTest {

    private SimpleLinkedBag<String> bag;

    /**
     * sets up the test environment by initializing a
     * new simplelinkedbag.
     */
    @Before
    public void setUp() {
        bag = new SimpleLinkedBag<>();
    }

    /**
     * test that the bag is initialized correctly (firstnode is null
     * and numberofentries is 0).
     */
    @Test
    public void testInitialBagIsEmpty() {
        assertTrue("bag should be empty upon initialization",
                   bag.isEmpty());
        assertEquals("initial size should be 0",
                     0, bag.getCurrentSize());
    }

    /**
     * test adding a non-null element.
     */
    @Test
    public void testAddNonNull() {
        boolean added = bag.add("apple");
        assertTrue("adding a non-null item should return true",
                   added);
        assertFalse("bag should not be empty after adding an item",
                    bag.isEmpty());
        assertEquals("size should be 1 after adding one element",
                     1, bag.getCurrentSize());
    }

    /**
     * test that adding null returns false.
     */
    @Test
    public void testAddNull() {
        assertFalse("adding null should return false",
                     bag.add(null));
        assertEquals("size should remain 0 after adding null",
                     0, bag.getCurrentSize());
    }

    /**
     * test pick() returns null when bag is empty.
     */
    @Test
    public void testPickFromEmptyBag() {
        assertNull("picking from an empty bag should return null",
                   bag.pick());
    }

    /**
     * test pick() returns one of the added elements.
     */
    @Test
    public void testPickReturnsValidItem() {
        bag.add("apple");
        bag.add("banana");
        bag.add("cherry");
        String picked = bag.pick();
        assertNotNull("should not return null when bag is not empty", picked);
        
        // since add adds to the front, the order isn’t guaranteed.
        // check that picked is one of the added strings.
        assertTrue("picked item should be one of the added elements",
                   picked.equals("apple") || picked.equals("banana") ||
                   picked.equals("cherry"));
    }

    /**
     * test remove() on an element not in the bag.
     */
    @Test
    public void testRemoveNonExistingItem() {
        bag.add("apple");
        bag.add("banana");
        boolean removed = bag.remove("cherry");
        assertFalse("removing item that is not in the bag should return false",
                    removed);
        assertEquals("remain unchanged remove", 2, bag.getCurrentSize());
    }

    /**
     * test remove() on an existing element.
     */
    @Test
    public void testRemoveExistingItem() {
        bag.add("apple");
        bag.add("banana");
        bag.add("cherry");
        int sizeBefore = bag.getCurrentSize();
        
        // remove "banana"
        boolean removed = bag.remove("banana");
        assertTrue("removing an existing item should return true",
                   removed);
        assertEquals("decrement by one", sizeBefore - 1, bag.getCurrentSize());
        
        // try to remove "banana" again; should return false
        // since it's no longer in the bag
        assertFalse("removing the same item a second time should return false",
                    bag.remove("banana"));
        assertEquals("remain unchanged", sizeBefore - 1, bag.getCurrentSize());
    }

    /**
     * test that removing all items results in an empty bag.
     */
    @Test
    public void testRemoveAllItems() {
        bag.add("apple");
        bag.add("banana");
        bag.add("cherry");

        // remove items one by one using pick() to select an element each time
        while (!bag.isEmpty()) {
            String item = bag.pick();
            assertNotNull("item picked should not be null", item);
            assertTrue("each removal should return true", bag.remove(item));
        }

        assertTrue("bag should be empty after removing all items",
                   bag.isEmpty());
        assertEquals("should  0 after items removed", 0, bag.getCurrentSize());
    }
}
