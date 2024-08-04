
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Threeterson implements Lock {
    private final ReentrantLock printLock;
    private final Condition condition;
    private final Output output;
    private final int[] level;
    private final int[] victim;
    private final boolean[] occupiedLevel;

    public Threeterson(Output output) {
        this.printLock = new ReentrantLock();
        this.condition = printLock.newCondition();
        this.output = output;
        this.level = new int[4];
        this.victim = new int[4];
        this.occupiedLevel = new boolean[4];

        for (int i = 0; i < 4; i++) {
            level[i] = -1;
            victim[i] = -1;
            occupiedLevel[i] = false;
        }
    }


    @Override
    public void lock() {
        int id = Integer.parseInt(Thread.currentThread().getName().split("-")[1]) % 3;

        for (int i = 1; i < 4; i++) {
            printLock.lock();
            try {
                level[id] = i;
                occupiedLevel[i - 1] = false;
                occupiedLevel[i] = true;
                if (i != 3) {
                    output.println(Thread.currentThread().getName() + " at L" + i);
                    victim[i] = id;
                    output.println(Thread.currentThread().getName() + " is a victim of L" + i);

                    while (occupiedLevel[i + 1]) {
                        try {
                            condition.await(); // Wait until signaled
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt(); // Preserve interrupt status
                        }
                    }
                } else {
                    output.println(Thread.currentThread().getName() + " has the lock");
                    break;
                }
           } finally {
                printLock.unlock();
            }
        }


    }

    @Override
    public void unlock() {
        int id = Integer.parseInt(Thread.currentThread().getName().split("-")[1]) % 3;

        printLock.lock();
        try {
            if (level[id] == 3) {
                output.println(Thread.currentThread().getName() + " un-locked the lock");
                occupiedLevel[3] = false;
                level[id] = -1;
                condition.signalAll();
            }
        } finally {
            printLock.unlock();
        }
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean tryLock() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Condition newCondition() {
        throw new UnsupportedOperationException("Not supported yet.");
    }


}
