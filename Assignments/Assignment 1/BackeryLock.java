
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class BackeryLock implements Lock {
     boolean[] flag;
     BoundedTimestamp[] label;
     int maximumNumberOfThreads;
    private  ReadWriteLock rwLockFlag = new ReentrantReadWriteLock();
    private  ReadWriteLock rwLockLabel = new ReentrantReadWriteLock();

    public BackeryLock(int n) { 
        maximumNumberOfThreads = n;
        flag = new boolean[n];
        label = new BoundedTimestamp[n];
        for (int i = 0; i < n; i++) {
            BoundedTimestamp newTimestamp = new BoundedTimestamp(n-1);
            label[i] = newTimestamp ;
            flag[i] = false;
        }
    }

    @Override
    public void lock() {
        int i = Integer.parseInt(Thread.currentThread().getName().split("-")[1]) % maximumNumberOfThreads;
        rwLockFlag.writeLock().lock();
        flag[i] = true;
        rwLockFlag.writeLock().unlock();

        rwLockLabel.readLock().lock();
        //You should implement the getNext method in BoundedTimestamp
        BoundedTimestamp next = BoundedTimestamp.getNext(scan(),(Integer.parseInt(Thread.currentThread().getName().split("-")[1])%maximumNumberOfThreads) );
        rwLockLabel.readLock().unlock();


        rwLockLabel.writeLock().lock();
        label(next, i);
        rwLockLabel.writeLock().unlock();

        while (existsAKWhere(i)) {
            //output.println("\tThread-" + i + " with label " + label[i] + " and flag " + flag[i]+" is waiting\n");
        }
        ; // wait if k has its flag up and has a smaller label. Break ties with thread id

    }

    @Override
    public void unlock() {

        flag[Integer.parseInt(Thread.currentThread().getName().split("-")[1]) % maximumNumberOfThreads] = false;
        flag = flag;
    }

    public boolean existsAKWhere(int i) {
        for (int k = 0; k < flag.length; k++) {
            if (k == i) {
                continue;
            }
            rwLockFlag.readLock().lock();
            rwLockLabel.readLock().lock();
            if (flag[k] && lexOrder(label[i], i, label[k], k)) {
                //output.println("Thread-"+i+" Found Thread-"+k+ " with label " + label[k] + " and flag " + flag[k]);
                rwLockFlag.readLock().unlock();
                rwLockLabel.readLock().unlock();
                return true;
            }
            rwLockLabel.readLock().unlock();
            rwLockFlag.readLock().unlock();
        }
        return false;
    }

    public boolean lexOrder(BoundedTimestamp timestampI, int i, BoundedTimestamp timestampK, int k) {
        if (timestampI.compare(timestampK) == -1) {
            return true; // i must wait for k
        } else if (timestampI.compare(timestampK) == 1) {
            return false; // i must not wait for k
        }
        return i < k; //if same timestamp, i must not wait for k if it's smaller
    }

    public BoundedTimestamp[] scan() {
        BoundedTimestamp[] snapshot = new BoundedTimestamp[label.length];
        for (int i = 0; i < label.length; i++) {
            snapshot[i] = label[i];
        }
        return snapshot;
    }

    public void label(BoundedTimestamp timestamp, int i) {
        label[i] = (BoundedTimestamp) timestamp;
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'lockInterruptibly'");
    }

    @Override
    public boolean tryLock() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'tryLock'");
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'tryLock'");
    }

    @Override
    public Condition newCondition() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'newCondition'");
    }

}
