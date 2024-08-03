
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
       int id = (int)(Thread.currentThread().getId() % 3);
       boolean empty = true;
       for(int i = 1; i < 3; i++)
       {
           printLock.lock();
           try {
               output.println(Thread.currentThread().getName() + " is a victim of L" + i);
               victim[i] = id;

               for(int k = 0; k < 3; k++){
                   if(k == id) continue;

                   if(level[k] == i) {
                       empty = false;
                       break;
                   }
               }
               if(empty) {
                   level[id] = i;
                   output.println(Thread.currentThread().getName() + " at L" + i);
               }
           } finally {
               printLock.unlock();
           }

           if(empty) {
               continue;
           }

               boolean found = false;
               do{
                   found = false;
                   printLock.lock();
                   try {
                       for (int j = 0; j < 3; j++) {
                           if (j == id) {
                               continue;
                           }

                               if (level[j] >= i && victim[i] == id) {
                                   found = true;
                                   break;
                               }
                           }
                   } finally {
                       printLock.unlock();
                   }

               } while(found);

               printLock.lock();
            try{
               output.println(Thread.currentThread().getName() + " at L" + i);
               level[id] = i;
           } finally{
               printLock.unlock();
           }


          /* output.println(Thread.currentThread().getName() + " at L" + i);
           level[id] = i;*/
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
        int id = (int)(Thread.currentThread().getId() % 3);

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

}
