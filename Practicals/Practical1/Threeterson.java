
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

        for(int i = 0; i < 3; i++) {
            level[i] = -1;
            victim[i] = -1;
        }
    }

   

    @Override
    public void lock() {
       int id = Integer.parseInt(Thread.currentThread().getName().split("-")[1]) % 3;

       victim[0] = id;
       printLock.lock();
       try{
           while(isThreadAtHigher(id,0)){
               printLock.unlock();
               try{
                   Thread.sleep(4);
               } catch (InterruptedException e) {
                   Thread.currentThread().interrupt();
               }
               printLock.lock();
           }
       } finally{
           printLock.unlock();
       }

       for(int i = 1; i < 3; i++)
       {
           printLock.lock();
           try {
               output.println(Thread.currentThread().getName() + " is a victim of L" + i);

               victim[i] = id;

               level[id] = i;
               output.println(Thread.currentThread().getName() + " at L" + i);

               while(isThreadAtHigher(id,i)){
                    printLock.unlock();
                    try{
                        Thread.sleep(4);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    printLock.lock();
               }

           } finally {
               printLock.unlock();
           }

       }

       printLock.lock();
       try{
           level[id] = 3;
           output.println(Thread.currentThread().getName() + " has the lock");
       } finally{
           printLock.unlock();
       }
    }

    @Override
    public void unlock() {
        int id = Integer.parseInt(Thread.currentThread().getName().split("-")[1]) % 3;

            if(level[id] == 3){
                output.println(Thread.currentThread().getName() + " un-locked the lock");
                level[id] = -1;
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

    private boolean isThreadAtHigher(int id, int currlevel){
        for(int i = 0; i < 3; i++) {
            if(i == id){
                continue;
            }
            else if(level[i] == currlevel+1 && victim[currlevel] == id){
                return true;
            }
        }
        return false;
    }

}
