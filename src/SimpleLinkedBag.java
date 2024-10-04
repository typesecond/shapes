// Virginia Tech Honor Code Pledge:
//
// This is a submission for project 2 (Spring 2025)
// As a Hokie, I will conduct myself with honor and integrity at all times.
// I will not lie, cheat, or steal, nor will I accept the actions of
// those who do.
// -- Kyler O'Rourke (korourke) 

package game;

import bag.SimpleBagInterface;
import bag.Node;
import java.util.Random;

/**
 * simple linked bag that stores stuff using a linked list.
 * can add, remove, and pick random stuff.
 * 
 * @param <T> the type of elements stored in the bag
 * @author Kyler O'Rourke (korourke)
 * @version 2025-09-28
 */
public class SimpleLinkedBag<T> implements SimpleBagInterface<T> {
    private Node<T> firstNode;
    private int numberOfEntries;
    private static final Random RAND = new Random();
    
    /**
     * makes an empty bag. starts out with no nodes
     */
    public SimpleLinkedBag() {
        firstNode = null;
        numberOfEntries = 0;
    }

    /**
     * adds an item to the bag. the item cannot be null
     * 
     * @param anEntry the item to add to the bag
     * @return true if the item was added, false if it was null
     */
    @Override
    public boolean add(T anEntry) {
        if (anEntry == null) {
            return false; 
        }
        Node<T> newNode = new Node<>(anEntry);
        newNode.setNext(firstNode); // stick new node at front
        firstNode = newNode;
        numberOfEntries++;
        return true;
    }

    /**
     * picks a random item from the bag. if the bag is empty, returns null
     * 
     * @return a random item from the bag or null if empty
     */
    @Override
    public T pick() {
        if (isEmpty()) {
            return null; // nothing to pick
        }
        int index = RAND.nextInt(numberOfEntries);
        Node<T> currentNode = firstNode;
        for (int i = 0; i < index; i++) {
            currentNode = currentNode.getNext();
        }
        return currentNode.getData();
    }

    /**
     * removes an item from the bag. if the item is not found, returns false
     * 
     * @param anEntry the item to remove from the bag
     * @return true if the item was removed, false if it wasnt found
     */
    @Override
    public boolean remove(T anEntry) {
        Node<T> nodeToRemove = getReferenceTo(anEntry);
        if (nodeToRemove == null) {
            return false; // not here, can't remove
        }
        nodeToRemove.setData(firstNode.getData()); // overwrite
        firstNode = firstNode.getNext();
        numberOfEntries--;
        return true;
    }

    /**
     * gets reference to a node containing the entry, or null if not found
     * 
     * @param anEntry the item to look for in the bag
     * @return the node containing the item or null if not found
     */
    private Node<T> getReferenceTo(T anEntry) {
        Node<T> currentNode = firstNode;
        while (currentNode != null) {
            if (currentNode.getData().equals(anEntry)) {
                return currentNode; // found it!
            }
            currentNode = currentNode.getNext();
        }
        return null; // not found
    }

    /**
     * gets the current number of items in the bag
     * 
     * @return the number of items in the bag
     */
    @Override
    public int getCurrentSize() {
        return numberOfEntries;
    }

    /**
     * checks if the bag is empty or has any items
     * 
     * @return true if the bag is empty, false otherwise
     */
    @Override
    public boolean isEmpty() {
        return numberOfEntries == 0;
    }
}
