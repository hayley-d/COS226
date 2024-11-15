
class EliminationBackoffStack<T> {
    static final int capacity = 5;
    EliminationArray<T> arr = new EliminationArray<T>(capacity);

    /**
     * Attempts to add a new node to the top of the stack.
     * @param node The new node to be added to the stack.
     */
    protected boolean tryPush(Node node)  {
        Node oldTop = top.get();
        node.next = oldTop;
        return top.compareAndSet(oldTop,node);
    }

    /**
     * Attempts to add a new value to the stack.
     * @param Value to be added to the stack.
     */
    public void push(T value) {
        Node node  = new Node(value);
        while(true) {
            // if push was success then return
            if(tryPush(node)) {
                return;
            } else try{
                T otherValue = arr.visit(value,5);
                if(otherValue == null) {
                    return;
                }
            } catch (Exception e) {
                //timeout
            }
        }
    }

    protected Node tryPop() throws EmptyException{
        Node oldTop = top.get();

        // check if empty
        if(oldTop == null) {
            throw new EmptyException();
        } else {
            Node newTop = oldTop.next();

            if(top.compareAndSet(oldTop,newTop)) {
                return oldTop;
            } else {
                return null;
            }
        }
    }

    public T pop() throws EmptyException{
        Node myNode = null;
        while(true) {
            node = tryPop();
            if(node != null)  {
                return node.value;
            } else {
                T otherValue = arr.visit(null,5);
                if(otherValue != null) {
                    return otherValue;
                }
            } catch (Exception e) {
                //fail
            }
        }
    }
}

class EliminationArray {
    private static final int duration = 1000;

    LockFreeExcheanger<T>[] exchanger;

    Random random;

    public EliminationArray(int capacity) {
        exchanger = new LockFreeExchanger[capacity];
        for(LockFreeExchanger<T> e : exchanger) {
            e = new LockFreeExchanger<T>();
        }

        random = new Random();
    }
    
    public T visit(T value, int range) throws TimeoutException{
        int slot = random.nextInt(range);
        return exchanger[slot].exchange(value,duration,TimeUnit.MILLISECONDS);
    }
}

class LockFreeExchanger<T> {
    static final int EMPTY = 0;
    static final int WAITING = 1;
    static final int BUSY = 2;

    AtomicReference<T> slot = new AtomicReference<T>(null);

    public T exchange(T myItem, long timeout, TimeUnit unit)  throws TimeoutException{
        long nanos = unit.toNanos(timeout);
        long timeBound = System.nanoTime() + nanos;
        int [] stampHolder = {EMPTY};

        while(true) {
            // Run out of time
            if(Sytem.nanoTime() > timeBound) {
                throw new TimeoutException();
            }

            T otherItem = slot.get(stampHolder);
            int stamp = stampHolder[0];

            switch(stamp) {
                case EMPTY:
                   if(slot.compareAndSet(otherItem,myItem,EMPTY,WAITING)) {
                        while(System.nanoTime() < timeBound) {
                            otherItem = slot.get(stampHolder);
                            if(stampHolder[0] == BUSY) {
                                slot.set(null,EMPTY);
                                return otherItem;
                            }
                        }
                        // if I have timed out i need to reset the exchanger 
                        if(slot.compareAndSet(myItem, null, WAITING,EMPTY) {
                            throw new TimeoutException();
                        } else {
                            otherItem = slot.get(stampHolder);
                            slot.set(null,EMPTY);
                            return otherItem;
                        }
                    } 
                    break;
                case WAITING:
                    if(slot.compareAndSet(otherItem,myItem,WAITING,BUSY) {
                        return otherItem;
                    }
                    break;
                case BUSY:
                    break;
            }
        }
    }
}
