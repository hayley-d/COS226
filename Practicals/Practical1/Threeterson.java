
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Threeterson implements Lock {
    private final ReentrantLock printLock;
    private final Condition[] conditions;
    private final Output output;
    private int[] level;
    private int[] victim;
    private boolean[] occupiedLevel;

    public Threeterson(Output output) {
        this.printLock = new ReentrantLock();
        this.conditions = new Condition[3];
        this.output = output;
        this.level = new int[3];
        this.victim = new int[3];
        this.occupiedLevel = new boolean[4];

        for (int i = 0; i < 3; i++) {
            level[i] = -1;
            victim[i] = -1;
            occupiedLevel[i] = false;
            conditions[i] = printLock.newCondition();
        }
        occupiedLevel[3] = false;
    }


    @Override
    public void lock() {
        int id = Integer.parseInt(Thread.currentThread().getName().split("-")[1]) % 3;

        for (int i = 0; i < 4; i++) {

            int currentLevel = i;

            printLock.lock();
            try {
                level[id] = currentLevel;
                if(currentLevel == 0){
                    while (moveLevel(id, currentLevel)) {
                        try {
                            conditions[i].await();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
                else if (currentLevel < 3 && currentLevel > 0) {
                    output.println(Thread.currentThread().getName() + " at L" + currentLevel);
                    victim[currentLevel] = id;
                    output.println(Thread.currentThread().getName() + " is a victim of L" + currentLevel);

                    while (moveLevel(id, currentLevel)) {
                        try {
                            conditions[i].await();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }

                    occupiedLevel[i-1] = false;
                    occupiedLevel[currentLevel] = true;
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
                for (int i = 0; i < 3; i++) {
                    conditions[i].signalAll();
                }
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
        if (printLock.isHeldByCurrentThread()) {
            return false;
        }
        /*throw new UnsupportedOperationException("Not supported yet.");*/
        return true;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Condition newCondition() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private boolean moveLevel(int id, int currentLevel) {
        for (int i = 0; i < 3; i++) {
            if (i != id && level[i] >= currentLevel && victim[currentLevel] == id) {
                return true;
            }
        }
        return false;
    }


}
