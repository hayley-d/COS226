import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.atomic.AtomicBoolean;

class LockNode<T>{
    private Lock lock = new ReentrantLock();
    private AtomicBoolean locked;
    public T value;
    public Node<T> left;
    public Node<T> right;


    public Node(T value){
        this.next = null;
        this.next = null;
        this.value = value;
        this.locked = false;
    }

    public void lock(){
        this.lock.lock();
        this.locked.set(true);
    }

    public void unlock(){
        this.lock.unlock();
        this.locked.set(false);
    }

    public boolean isLocked(){
        return this.locked.get();
    }
}
