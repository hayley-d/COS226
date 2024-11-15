
class FairReaderWriterLock implements ReadWriteLock {
    Lock readLock; // used to synchronize readers
    Lock writeLock;     // Used to synchronize writers
    Lock lock;  // used for synchronization
    int read_aquires;    // counter the number of current readers
    int read_releases;
    boolean writer; // true if ther is currently a writer
    Condition condition;    // singnal if there are no readers the writer can enter
    //
    
    public SimpleReadWriteLock() {
        writer = false;
        read_aquires = 0;
        read_releases = 0;
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

                read_aquires++;
            } finally {
                lock.unlock();
            }
        }

        @Override
        public void unlock() {
            lock.lock();
            try {
                read_releases++;
                if (read_releases == read_aquires) {
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
                while(writer) {
                    condition.await();
                }
                writer = true;
                while(read_aquires != read_releases) {
                    condition.await();
                }
            } finally { 
                lock.unlock();
            }
        }

        @Override
        public void unlock() {
            writer = false;
            condition.signalAll();
        }
    }

}


