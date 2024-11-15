
class LockFreeStack<T> {
    AtomicReference<Node> top = new AtomicReference<Node>(null);
    static final int MIN_DELAY = 100;
    static final int MAX_DELAY = 10000;
    Backoff backoff = new Backoff(MIN_DELAY,MAX_DELAY);

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
            } else {
                // otherwise backoff for a bit and retry
                backoff.backoff();
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
                backoff.backoff();
            }
        }
    }
}
