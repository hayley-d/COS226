
public class Node<T extends Comparable<T>> {
    public T value;
    public Node<T> next;

    public Node(T value) {
        this.value = value;
        this.next = null;
    }

    public T getValue() {
        return this.value;
    }
}
