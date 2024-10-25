import java.util.Random;
import java.util.concurrent.TimeUnit;

public class EliminationArray<T extends Comparable<T>> {
    private static final int duration = 1000;
    LockFreeExchanger<T>[] exchanger;
    Random random;

    /**
     * Constructor that initilizes the exchanger array.
     * @param capacity The number of elements in the exchanger array.
     */
    public EliminationArray(int capacity) {
        exchanger = (LockFreeExchanger<T>[]) new LockFreeExchanger[capacity];    
         
        for(int i = 0; i < capacity; i++) {
            exchanger[i] = new LockFreeExchanger<T>();
        }

        random = new Random();
    }

    /**
     * This method is used to exchange the thread's current value with another thread's value.
     * Random;y selects an index in the array to exchange its value.
     * @param value The value being exchanged.
     * @param range The Inner rage of the array.
     */
    public T visit(T value, int range) throws TimeoutException{
        int slot = random.nextInt(range);
        return (exchanger[slot].exchange(value,duration,TimeUnit.MILLISECONDS));
    }
}
