
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Threeterson implements Lock {
    private final ReentrantLock printLock;
    private final Output output;
    private final int[] level;
    private final int[] victim;
    private final boolean[] clearLevel;

    public Threeterson(Output output) {
        this.printLock = new ReentrantLock();
        this.output = output;
        this.level = new int[3];
        this.victim = new int[3];
        this.clearLevel = new boolean[3];

        for (int i = 0; i < 3; i++) {
            level[i] = -1;
            victim[i] = -1;
            clearLevel[i] = true;
        }
    }


    @Override
    public void lock() {
        int id = Integer.parseInt(Thread.currentThread().getName().split("-")[1]) % 3;
        printLock.lock();
        try {
            for (int i = 1; i < 3; i++) {
                /*printLock.lock();
                try {*/
                    output.println(Thread.currentThread().getName() + " is a victim of L" + i);
                    victim[i] = id;

                    /*while (isThreadAtHigher(id, i)) {
                        printLock.unlock();
                        try {
                            Thread.sleep(4);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                        printLock.lock();
                    }*/
                while(!clearLevel[i]) {
                    printLock.unlock();
                    try {
                        Thread.sleep(4);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    printLock.lock();
                }
                if(i-1 != 0)
                {
                    clearLevel[i-1] = true;
                }
                clearLevel[i] = false;
                level[id] = i;
                output.println(Thread.currentThread().getName() + " at L" + i);
               /* } finally {
                    printLock.unlock();
                }*/

            }

       /*printLock.lock();
       try{*/
            while(!clearLevel[0]) {
                printLock.unlock();
                try {
                    Thread.sleep(4);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                printLock.lock();
            }
            clearLevel[2] = true;
            clearLevel[0] = false;
            level[id] = 3;
            output.println(Thread.currentThread().getName() + " has the lock");
        } finally {
            printLock.unlock();
        }
    }

    @Override
    public void unlock() {
        int id = Integer.parseInt(Thread.currentThread().getName().split("-")[1]) % 3;
        printLock.lock();
        try {
            if (level[id] == 3) {
                clearLevel[0] = true;
                output.println(Thread.currentThread().getName() + " un-locked the lock");
                level[id] = -1;
            }
        } finally{
            printLock.unlock();
        }
        try {
            Thread.sleep(7);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
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

    private boolean isThreadAtHigher(int id, int currlevel) {
        for (int i = 0; i < 3; i++) {
            if (i == id) {
                continue;
            } else if (level[i] >= currlevel + 1 /*|| victim[currlevel] == id*/) {
                return true;
            }
        }
        return false;
    }

}
