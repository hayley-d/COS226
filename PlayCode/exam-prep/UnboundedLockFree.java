class Node {
    public T value;
    public AtomicReference<Node> next;

    public Node(T val) {
        this.value = val;
        next = new AtomicReference<T>(null);
    }
}

class UnboundedLockFree {
    
    public enq(T item) {
        // create new node
        Node node = new Node(item);
        
        while(true) {
            // get the tail node
            Node last = tail.get();
            Node next = last.next.get();

            if(last == tail.get() {
                if( next == null) {
                    // Step 1: add node as last item in the queue
                    if(last.compareAndSet(next,node)) {
                        // Step 2: set new node as the tail
                        tail.compareAndSet(last,node);
                        return;
                    }
                } else {
                    // incomplete enq()
                    tail.compareAndSet(last,next);
                }
            }
        }
    }

    public T deq() throws EmptyException{

        while(true) {
            Node first = head.get();
            Node last = tail.get();
            
            // get next item in the queue
            Node next = head.next.get();

            if(first == last) {
                // queue is empty
                if(next == null) {
                    throw new EmptyException();
                }

                // attempt to update tail
                tail.compareAndSet(last,next);
            } else {
                T value = next.value;
                if(head.compareAndSet(first,next)) {
                    return value;
                }
            }
        }
    }
}
