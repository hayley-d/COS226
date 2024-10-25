
public class EliminationBackoffStack<T extends Comparable<T>> extends LockFreeStack<T> {
    public int capacity;

    private EliminationArray<T> eliminationArray;

    static ThreadLocal<RangePolicy> policy = new ThreadLocal<RangePolicy>() {
        protected synchronized RangePolicy initialValue() {
            return new RangePolicy();
        }
    };

    public EliminationBackoffStack(int c) {
        this.capacity = c;
        this.eliminationArray = new EliminationArray(c);
    }

    @Override
    public void push(T value) {
        //RangePolicy rangePolicy = policy.get();
        Node node = new Node(value);

        while(true) {
            // If successful push return
            if(tryPush(node)) {
                return;
            } else try{
                T otherValue = eliminationArray.visit(value,this.capacity);

                if(otherValue == null) {
                    //rangePolicy.recordEliminationSuccess();
                    return;
                }
            } catch (Exception e) {
                //rangePolicy.recordEliminationTimeout();
            }
        }
    }

    @Override
    public T pop() throws EmptyException{
        //RangePolicy rangePolicy = policy.get();

        while(true) {
            Node<T> returnNode = tryPop();

            if(returnNode != null) {
                return returnNode.value;
            } else try {
                T otherValue = eliminationArray.visit(null,this.capacity);
                if(otherValue != null) {
                    return otherValue;
                }
            } catch (Exception e) {

            }
        }
    }
}
