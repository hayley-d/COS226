public class AtomicMRMWRegister<T> implements Register<T>{

    private StampedValue<T> [] a_table; //array of atomic registers

    public AtomicMRMWRegister(int capacity, T init) {
        a_table = new StampedValue[capacity];
        StampedValue<T> value = new StampedValue<>(init);
        for(int i = 0; i < capacity; i++) {
            a_table[i] = value;
        }
    }

    @Override
    public T read(){
        StampedValue<T> max = StampedValue.MIN_VALUE;
        for(int i = 0; i <a_table.length; i++) {
            max = StampedValue.max(max, a_table[i]);
        }
        return max.value;
    }

    @Override
    public void write(T value){
        int me = (int) Thread.currentThread().getId();
        StampedValue<T> max=  StampedValue.MIN_VALUE;
        for(int i = 0; i <a_table.length; i++) {
            max = StampedValue.max(max, a_table[i]);
        }
        a_table[me] = new StampedValue(max.stamp+1,value);
    }
}
