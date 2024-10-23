import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

public class LockFreeExchanger<T extends Comparable<T>> {
    static fianl AtomicInteger EMPTY = new AtomicInteger(-1);
    static fianl AtomicInteger WAITING = new AtomicInteger(0);
    static fianl AtomicInteger BUSY = new AtomicInteger(1);

    AtomicStampedReference<T> slot = new AtomicStampedReference<T>(null,0);

    /**
     * Allows one thread to exchange a value with another thread.
     * The calling thread can specify the timeout for the method call if a patner is not found within a given duration.
     *
     * @param myItem The value being exchanged.
     * @param timeout The amout of time the calling thread is willing to wait.
     * @param unit The TimeUnit for the timeout.
     * @return A value of type T which was exchanged between threads.
     * @throws TimeoutException when the thread reaches its timeout without finding a patner.
     */
    public T exchange(T myItem, long timeout, TimeUnit unit) throws TimeoutException {
        long nano = unit.toNanos(timeout);
        long timeBound = System.nanoTime() + nano;

        int[] stampHolder = {EMPTY};

        while(true) {
            if(System.nanoTime() > timeBound) {
                // If the thread has reached its time limit
                throw new TimeoutException();
            }

            T otherValue = this.slot.get(stampHolder);
            int stamp = stampHolder[0];

            switch(stamp) {
                // If the state is EMPTY then try place item in the slot and set state to WAITING
                case EMPTY:
                    // If success compareAndSet() spin while waiting otherwise retry
                    if(slot.compareAndSet(otherValue,myItem,EMPTY,WAITING)) {
                        // Spin until time is up
                        while(System.nanoTime() < timeBound) {
                            // Check the slot
                            otherValue = this.slot.get(stampHolder);
                            // If state = BUSY = another thread has shown up
                            if(stampHolder[0] == BUSY) {
                                this.slot.set(null,EMPTY);
                                return otherValue;
                            }
                        }
                        // If I am still Waiting -> timeout
                        // Try and reset slot if success the timeout otherwise a thread showed up last minuet
                        if(this.slot.compareAndSet(myItem,null,WAITING,EMPTY)) {
                            throw new TimeoutException();
                        } else {
                            otherValue = slot.get(stampHolder);
                            slot.set(null,EMPTY);
                            return otherValue;
                        }
                    }
                    break;
                case WAITING:
                    // Some other thread is waiting and the slot contains the other item
                    // Take the item and replace it with my item
                    if(this.slot.compareAndSet(otherValue,myItem,WAITING,BUSY)) {
                        return otherValue;
                    }
                    break;
                case BUSY:
                    break;
                default:
                    break;
            }
        }
    }

}
