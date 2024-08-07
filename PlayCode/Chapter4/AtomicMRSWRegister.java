public class AtomicMRSWRegister<T> implements Register<T> {
    ThreadLocal<Long> lastStamp;
    private StampedValue<T>[][] a_table; //each entry is a SRSW atomic register

    public AtomicMRSWRegister(T init, int readers){
        lastStamp = new ThreadLocal<>(){
            protected Long initialValue() { return 0L;}
        };
        a_table = new StampedValue[readers][readers];
        StampedValue<T> value = new StampedValue<>(init);
        for(int i = 0; i < readers; ++i){
            for(int j = 0; j < readers; ++j){
                a_table[i][j] = value;
            }
        }
    }

    @Override
    public T read(){
        int me = (int) Thread.currentThread().getId();
        StampedValue<T> value = a_table[me][me]; //read the last write timestamp
        //check the column for a greater timestamp
        for(int i = 0; i < a_table[me].length; ++i){
            value = StampedValue.max(value, a_table[i][me]);
        }
        //write the largest value to the row
        for(int i = 0; i < a_table[me].length; ++i){
            if(i==me) continue;
            a_table[me][i] = value;
        }
        return value.value;
    }

    @Override
    public void write(T v){
        long stamp = lastStamp.get() + 1;
        lastStamp.set(stamp);
        StampedValue<T> value = new StampedValue<>(stamp,v);
        //write the diagonals
        for(int i = 0; i < a_table.length; ++i){
            a_table[i][i] = value;
        }
    }
}


