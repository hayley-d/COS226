
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Threeterson implements Lock {
    private final ReentrantLock printLock;
    private final Output output;
    private final int[] level;
    private final int[] victim;

    public Threeterson(Output output) {
        this.printLock = new ReentrantLock();
        this.output = output;
        this.level = new int[3];
        this.victim = new int[3];
    }

   

    @Override
    public void lock() {
       int id = (int)(Thread.currentThread().getId() % 3);
       for(int i = 1; i < 3; i++)
       {
           level[id] = i;
           output.println(Thread.currentThread().getName() + " at L" + i);
           victim[i] = id;
           output.println(Thread.currentThread().getName() + " is a victim of L" + i);
           for(int j = 0; j < 3; j++){
               if(j==id) continue;
               while(level[j] >= i && victim[i] == id);
           }
       }
       output.println(Thread.currentThread().getName() + " has the lock");
    }

    @Override
    public void unlock() {
        int id = (int)(Thread.currentThread().getId() % 3);
        level[id] = 0;
        output.println(Thread.currentThread().getName() + " un-locked the lock");
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
