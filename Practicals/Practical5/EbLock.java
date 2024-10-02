import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;

public class EbLock implements Lock{
    private AtomicBoolean state = new AtomicBoolean(false);
    private static final int MAX_DELAY = 1000; // delay at most 1 second
    private static final int MIN_DELAY = 1; // delay at least 1 milisecond

    @Override
    public void lock() {
        try{
            Backoff backoff = new Backoff(MIN_DELAY,MAX_DELAY);
            while(true){
                if(!state.get() && state.compareAndSet(false,true)){
                    return;
                } else {
                    backoff.backoff();
                }
            }
        } catch (InterruptedException e){
            Thread.currentThread().interrupt();
            return;
        }
        
    }

    @Override
    public void unlock(){
        state.set(false);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        throw new UnsupportedOperationException("lockInterruptibly not supported");
    }

    @Override
    public boolean tryLock() {
        return state.compareAndSet(false, true);
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        throw new UnsupportedOperationException("tryLock with timeout not supported");
    }

    @Override
    public Condition newCondition() {
        throw new UnsupportedOperationException("newCondition not supported");
    }
}
