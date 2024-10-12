import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.atomic.AtomicBoolean;

class LockNode<T extends Comparable<T>>{
    private Lock lock = new ReentrantLock();
    private AtomicBoolean locked;
    public T value;
    public LockNode<T> left;
    public LockNode<T> right;


    public LockNode(T value){
        this.left = null;
        this.right = null;
        this.value = value;
        this.locked.set(false);
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
