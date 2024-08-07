
public class StampedValue<T> {
    public long stamp;
    public T value;

    //initial value with 0 timestamp
    public StampedValue(T init){
        stamp = 0;
        value = init;
    }

    public StampedValue(long stamp, T value){
        this.stamp = stamp;
        this.value = value;
    }

    public static StampedValue max(StampedValue x, StampedValue y){
        if(x.stamp > y.stamp){
            return x;
        }
        return y;
    }

    public static StampedValue MIN_VALUE = new StampedValue(null);
}


