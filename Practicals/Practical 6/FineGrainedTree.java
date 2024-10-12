import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class FineGrainedTree<T extends Comparable<T>>{
    public LockNode<T> root;


    public FineGrainedTree(){
        this.root = null;
    }

    public boolean isEmpty(){
        root.lock();
        try{
            return root == null;
        } finally{
            root.unlock();
        }
    }

    public boolean contains(T value){
        if(!isEmpty()){
            LockNode<T> current = root;
            LockNode<T> child = null;
            current.lock();
            try{
                if(current.value.compareTo(value) == 0){
                    return true;
                } else if(current.value.compareTo(value) > 0){
                    child = current.left;
                } else {
                    child = current.right;
                }
                if(child == null){
                    return false;
                }
                child.lock();
            } finally {
                current.unlock();
            }
            return recursive_contains(value,child);
        }
        return false;
    }

    private boolean recursive_contains(T value, LockNode<T> current){
        LockNode<T> child = null;
        if(current == null){
            return false;
        }
        if(current.value.compareTo(value) == 0){
            //unlock current node
            current.unlock();
            return true;
        } else if(current.value.compareTo(value) > 0) {
            child = current.left;
        } else {
            child = current.right;
        }
        if(child == null){
            current.unlock();
            return false;
        }
        child.lock();
        current.unlock();

        return recursive_contains(value,child);
    }

    public boolean insert(T value){
        LockNode<T> current = root;
        LockNode<T> child = null;
        current.lock();
        try{
            if(!contains(value)){
                if(isEmpty()){
                    this.root = new LockNode<T>(value);
                    return true;
                } else {
                    if(current.value.compareTo(value) > 0) {
                        child = current.left;
                        if(child == null){
                            current.left = new LockNode<T>(value);
                            return true;
                        }
                    } else {
                        child = current.right;
                        if(child == null){
                            current.right = new LockNode<T>(value);
                            return true;
                        }
                    }
                    child.lock();
                }
            } else {
                return false;
            }
        } finally{
            current.unlock();
        }
        return recursive_insert(value,child);
    }

    private boolean recursive_insert(T value, LockNode<T> current){
        LockNode<T> child = null;
        if(current != null){
            if(current.value.compareTo(value) > 0){
                child = current.left;
                if(child == null){
                    current.left = new LockNode<T>(value);
                    current.unlock();
                    return true;
                }
            } else {
                child = current.right;
                if(child == null){
                    current.right = new LockNode<T>(value);
                    current.unlock();
                    return true;
                }
            }
        } else {
            return false;
        }
        child.lock();
        current.unlock();
        return recursive_insert(value,child);
    }

    public boolean remove(T value){
        LockNode<T> child = null;
        LockNode<T> current = root;
        //if empty or value not in the tree -> fail
        if(isEmpty()) return false;
        if(!contains(value)) return false;
         
        current.lock();
        try{
            while(current != null){
                child = current.value.compareTo(value) > 0 ? current.left : current.right;

                if(child == null) return false;
               
                child.lock();
                if(child.value.compareTo(value) == 0) {
                    removeChild(current,child);
                }
                current.unlock();
                current = child;
            }
        } finally {
            current.unlock();
        }
        return false;
    }

    public LockNode<T> getMax(LockNode<T> root){
        LockNode<T> current = root;
        current.lock();
        try {
            while(current != null) {
                if(current.right == null) return current;
                current.right.lock();
                current.unlock();
                current = current.right;
            }

            return root;
        } finally {
            current.unlock();
        }
    }

    private boolean removeChild(LockNode<T> current, LockNode<T> child){
        if(child.right == null && child.left == null){
            if(current.left == child){
                current.left = null;
            } else {
                current.right = null;
            }
            child.unlock();
            return true;
        } else if(child.right == null && child.left != null){
            child.left.lock();
            if(current.left == child){
                current.left = child.left;
                child.unlock();
                child = current.left;
            } else {
                current.right = child.left;
                child.unlock();
                child = current.right;
            }
            child.unlock(); 
            return true;
        } else if(child.right != null && child.left == null){
            child.right.lock();
            if(current.left == child){
                current.left = child.right;
                child.unlock();
                child = current.left;
            } else {
                current.right = child.right;
                child.unlock();
                child = current.right;
            }
            child.unlock(); 
            return true; 
        } else {
            child.right.lock();
            LockNode<T> left_sub_tree = child.right.left;

            child.left.lock();
            try {
                LockNode<T> left_max = getMax(child.left); 
                if(left_max == null) {
                    left_max = child.left;
                    left_max.right = left_sub_tree;
                } else {
                    left_max.lock();
                    try {
                        left_max.right = left_sub_tree;
                    } finally {
                        left_max.unlock();
                    }
                }
                child.right.left = child.left;
            } finally {
                child.left.unlock();
            }
            
            if(current.left == child){
                current.left = child.right;
                child.unlock();
                child = current.left;
            } else {
                current.right = child.right;
                child.unlock();
                child = current.right;
            }
            child.unlock();
            return true;
      }
    }
}
