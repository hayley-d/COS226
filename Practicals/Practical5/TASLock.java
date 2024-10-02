import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;

public class TASLock implements Lock{
    AtomicBoolean state = new AtomicBoolean(false);

    @Override
    public void lock(){
        while(state.getAndSet(true)){} //spin
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
