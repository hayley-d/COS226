
public class AtomicSRSWRegister<T> implements Register<T> {

    ThreadLocal<Long> lastStamp;
    ThreadLocal<StampedValue<T>> lastRead;
    StampedValue<T> r_value;

    public AtomicSRSWRegister(T init){
        r_value = new StampedValue<>(init);
        lastStamp = new ThreadLocal<>(){
            protected Long initialValue(){return 0L;}
        };
        lastRead = new ThreadLocal<>(){
            protected StampedValue<T> initialValue(){return r_value;}
        };
    }
    @Override
    public T read() {
        StampedValue<T> value = r_value;
        StampedValue<T> last = lastRead.get();
        StampedValue<T> result = StampedValue.max(value,last);
        lastRead.set(result);
        return result.value;
    }

    @Override
    public void write(T t) {
        long stamp = lastStamp.get();
        r_value = new StampedValue<>(stamp,t);
        lastStamp.set(stamp);
    }
}


