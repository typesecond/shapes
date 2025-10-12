
package game;

import bag.SimpleBagInterface;
import java.util.Random;

/**
 * A simple array-based implementation of a bag data structure.
 * This bag stores elements in an array with a fixed maximum capacity.
 *
 * @param <T> the type of elements stored in the bag.
 * @author Kyler O'Rourke (korourke)
 * @version 2025-09-30
 */
public class SimpleArrayBag<T> implements SimpleBagInterface<T> {

    private static final int MAX = 18;
    private static final Random RAND = new Random();
    private final T[] bag;
    private int numberOfEntries;

    /**
     * Constructs a new SimpleArrayBag with a fixed capacity.
     */
    @SuppressWarnings("unchecked")
    public SimpleArrayBag() {
        bag = (T[]) new Object[MAX];
        numberOfEntries = 0;
    }

    /**
     * Adds a non-null element to the bag if capacity allows.
     *
     * @param anEntry the element to add.
     * @return true if the element was successfully added,
     *         false if the element is null or the bag is full.
     */
    @Override
    public boolean add(T anEntry) {
        if (anEntry == null || numberOfEntries >= MAX) {
            return false;
        }
        bag[numberOfEntries++] = anEntry;
        return true;
    }

    /**
     * picks and returns an element at random from the bag.
     *
     * @return a randomly selected element, or null if the bag is empty.
     */
    @Override
    public T pick() {
        if (isEmpty()) {
            return null;
        }
        int index = RAND.nextInt(numberOfEntries);
        return bag[index];
    }

    /**
     * rremoves the specified element from the bag, if it exists.
     *
     * @param anEntry the element to remove.
     * @return true if the element was found and removed,
     *         false otherwise.
     */
    @Override
    public boolean remove(T anEntry) {
        int index = getIndexOf(anEntry);
        if (index == -1) {
            return false;
        }
        bag[index] = bag[numberOfEntries - 1];
        bag[numberOfEntries - 1] = null;
        numberOfEntries--;
        return true;
    }

    /**
     * returns the current number of elements in the bag.
     *
     * @return the number of elements currently in the bag.
     */
    @Override
    public int getCurrentSize() {
        return numberOfEntries;
    }

    /**
     * checks whether the bag is empty.
     *
     * @return true if the bag contains no elements,
     *         false otherwise.
     */
    @Override
    public boolean isEmpty() {
        return numberOfEntries == 0;
    }

    /**
     * searches for the specified element in the bag.
     *
     * @param anEntry the element to find.
     * @return the index of the element if found; -1 otherwise.
     */
    private int getIndexOf(T anEntry) {
        for (int i = 0; i < numberOfEntries; i++) {
            if (bag[i].equals(anEntry)) {
                return i;
            }
        }
        return -1;
    }
}
