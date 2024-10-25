import java.util.Stack;
import java.util.concurrent.locks.ReentrantLock;

public class SequentialStack<T extends Comparable<T>> {
    private Stack<Node<T>> stack = new Stack<Node<T>>();
    private ReentrantLock lock = new ReentrantLock();

    public void push(T value) {
        Node<T> node = new Node(value);

        lock.lock();
        try {
            stack.push(node);
        } finally {
            lock.unlock();
        }
    }

    public T pop() {
        lock.lock();
        try {
            if(!stack.isEmpty()) {
                Node<T> node = stack.pop();
                return node.value;
            }
            return null;
        } finally {
            lock.unlock();
        }
    }
}
