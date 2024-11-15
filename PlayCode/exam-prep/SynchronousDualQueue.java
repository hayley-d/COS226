/**
 * Enum type to describe the type of node.
 */
enum NodeType {
    ITEM,
    RESERVATION
}

/**
 * Node for the synchronous dual queue.
 * @type NodeType the type of node.
 * @item The item being queued/dequeued.
 * @next Reference to the next node in the queue.
 */
class Node {
    volatile NodeType type;
    volatile AtomicReference<T> item;
    volatile AtomicReference<Node> next;

    Node(T myItem, NodeType myType) {
        item = new AtomicReference<T>(myItem);
        next = new AtomicReference<Node>(null);
        type = myType;
    }
}


class SynchronousDualQueue {
    // Sential node for the head and tail of the queue
    Node sentinel = new Node(null, NodeType.ITEM);

    // Head and tail of the queue are sentiel nodes
    // Queue is empty is head == tail
    head = new AtomicReference<Node>(sentinel);
    tail = new AtomicReference<Node>(sentinel);

    SynchronousDualQueue() {
        //empty
    }

    /**
     * Enqueue method of the queue.
     * @param item The item being added to the queue.
     */
    public void enq(T item) {
        // create a new node for the item being added
        Node offer = new Node(item,NodeType.ITEM);

        while(true) {
            // store the current head and tail references
            Node t = tail.get();
            Node h = head.get();

            // if the queue is empty or the tail is of type ITEM
            if(h == t || t.type == NodeType.ITEM) {
                Node n = tail.next.get(); 
               
                // if the tail has not changed
                if(t == tail.get()) {
                    // if the next node is not null
                    if(n != null) {
                        // change the tail to reference the current tail
                        tail.compareAndSet(t,n);
                    } else if(t.next.compareAndSet(n,offer) {
                        // add my item to the end of the queue
                        tail.compareAndSet(t,offer);
                        while(offer.item.get() == item) {
                            //spin until fulfilled
                        }

                        h = head.get();
                        if(offer == h.next.get()) {
                            head.compareAndSet(h,offer);
                        }
                        return;
                    }
                }
            } else {
                // queue contains dequeuer reservations
                // attempt to fulfill a reservation
                // get the head reservation
                Node n = h.next.get();
                
                if(t != tail.get() || h != head.get() || n == null) {
                    continue;
                }

                // try set null item to item being enqueued
                boolean success = n.item.compareAndSet(null,item);
                head.compareAndSet(h,n);
                if(success) return;
            }
        }
    }

    public void deq() {
    }
}
