// Virginia Tech Honor Code Pledge:
//
// This is a submission for project 2 (Spring 2025)
// As a Hokie, I will conduct myself with honor and integrity at all times.
// I will not lie, cheat, or steal, nor will I accept the actions of
// those who do.
// -- Kyler O'Rourke (korourke)

package bag;

/**
 * simple node for a singly-linked structure.
 *
 * @param <T> the type stored in the node.
 */
public class Node<T> {
    private T data;
    private Node<T> next;

    /**
     * creates a node with data and no next.
     */
    public Node(T data) {
        this(data, null);
    }

    /**
     * creates a node with data and a next reference.
     */
    public Node(T data, Node<T> next) {
        this.data = data;
        this.next = next;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Node<T> getNext() {
        return next;
    }

    public void setNext(Node<T> next) {
        this.next = next;
    }
}
