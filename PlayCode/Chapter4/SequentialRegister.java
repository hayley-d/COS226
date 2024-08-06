

public class SequentialRegister<T> implements Register {
    private T value;

    @Override
    public T read() {
        return value;
    }

    @Override
    public void write(Object t) {
        this.value = (T) t;
    }
}
