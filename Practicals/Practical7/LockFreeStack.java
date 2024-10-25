import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

public class LockFreeStack<T extends Comparable<T>>{
    AtomicReference<Node<T>> top = new AtomicReference<Node<T>>(null);
    static final int MIN_DELAY = 10;
    static final int MAX_DELAY = 1000;
    Backoff backoff = new Backoff(MIN_DELAY,MAX_DELAY);

    protected boolean tryPush(Node<T> node){
        Node<T> old = this.top.get();
        node.next = old;
        return(top.compareAndSet(old,node));
    }

    public void push(T value) {
        Node<T> node = new Node<T>(value);
        while(true){
            if(tryPush(node)){
                return;
            } else {
                try {
                    backoff.backoff();
                } catch (Exception e) {

                }
            }
        }
    }

    protected Node<T> tryPop() throws EmptyException {
        Node<T> old = this.top.get();
        if(old == null) throw new EmptyException();

        Node<T> newTop = old.next;
        if(this.top.compareAndSet(old,newTop)){
            return old;
        } else {
            return null;
        }
    }

    public T pop() throws EmptyException, InterruptedException {
        while(true) {
            try {
                Node<T> returnNode = tryPop();
                if(returnNode != null) return returnNode.value;
                backoff.backoff();
            } catch (InterruptedException e) {

            } catch (Exception e) {
                throw e;
            }
        }
    }
}
