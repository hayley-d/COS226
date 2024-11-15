
class SimpleReadWriteLock implements ReadWriteLock {
    Lock readLock; // used to synchronize readers
    Lock writeLock;     // Used to synchronize writers
    Lock lock;  // used for synchronization
    int readers;    // counter the number of current readers
    boolean writer; // true if ther is currently a writer
    Condition condition;    // singnal if there are no readers the writer can enter
    //
    
    public SimpleReadWriteLock() {
        writer = false;
        readers = 0;
        lock = new ReentrantLock();
        writeLock = new ReentrantLock();
        readLock = new ReentrantLock();
        condition = lock.newCondition();
    }

    public Lock readLock() {
        return readLock;
    }

    public Lock writeLock() {
        return writeLock;
    }

    public class ReadLock implements Lock {

        @Override
        public void lock() {
            lock.lock();
            try {
                while(writer) {
                    condition.await();
                }

                readers++;
            } finally {
                lock.unlock();
            }
        }

        @Override
        public void unlock() {
            lock.lock();
            try {
                readers--;
                if (readers == 0) {
                    condition.signalAll(); //wake up waiting writers 
                }
            } finally {
                lock.unlock();
            }
        }
    }

    public class WriteLock implements Lock {

        @Override
        public void lock() {
            lock.lock();
            try {
                while(readers > 0 || writer) {
                    condition.await();
                }
                writer = true;
            } finally { 
                lock.unlock();
            }
        }

        @Override
        public void unlock() {
            lock.lock();
            try {
                writer = false;
                condition.signalAll();
            } finally { 
                lock.unlock();
            }
        }
    }

}


