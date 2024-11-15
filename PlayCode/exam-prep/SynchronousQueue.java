
class SynchronousQueue<T> {
    
    T item = null;
    boolean enqueuing;
    Lock lock;
    Condition condition;

    public void enq(T item) {
        // acquire the lock
        lock.lock();
        try {
            // if another item is enqueueing wait
            while(enqueuing) {
                condition.await();
            }

            // signal that i am now enqueueing
            enqueuing = true;

            this->item = item;
            // wake any dequeres waiting for a notEmptyCondition
            condition.signalAll();

            // if the item is null wait until it is dequeued
            while(item != null) {
                condition.await();
            }

            enqueuing = false;
            // wake up waiting enqueuers
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public T deq() {
        lock.lock();
        try {
            while(item == null) {
                condition.await();
            }

            T t = item;
            item = null;
            // wake the waiting enqueuers
            condition.signalAll();

        } finally {
            lock.unlock();
        }
    }
}
