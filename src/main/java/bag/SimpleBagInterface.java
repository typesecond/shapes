// Virginia Tech Honor Code Pledge:
//
// This is a submission for project 2 (Spring 2025)
// As a Hokie, I will conduct myself with honor and integrity at all times.
// I will not lie, cheat, or steal, nor will I accept the actions of
// those who do.
// -- Kyler O'Rourke (korourke) 


package bag;

/**
 * an interface that defines basic operations for a bag data structure.
 * a bag is a collection where the order of elements does not matter.
 *
 * @param <T> the type of elements stored in the bag.
 * @author Kyler O'Rourke (korourke)
 * @version 2025-09-30
 */
public interface SimpleBagInterface<T> {

    /**
     * adds the specified element to the bag.
     *
     * @param anEntry the element to add.
     * @return true if the element was successfully added, false otherwise.
     */
    boolean add(T anEntry);

    /**
     * picks element from the bag at random.
     *
     * @return a randomly selected element, or null if the bag is empty.
     */
    T pick();

    /**
     * removes the specified element from the bag.
     *
     * @param anEntry the element to remove.
     * @return true if the element was found and removed, false otherwise.
     */
    boolean remove(T anEntry);

    /**
     * returns current number of elements in the bag.
     *
     * @return the number of elements in the bag.
     */
    int getCurrentSize();

    /**
     * Checks whether the bag is empty.
     *
     * @return true if the bag is empty, false otherwise.
     */
    boolean isEmpty();
}
