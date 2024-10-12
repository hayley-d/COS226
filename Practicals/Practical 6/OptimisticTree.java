import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Optimistic<T extends Comparable<T>>{
    public LockNode<T> root;
    private Lock lock = new ReentrantLock();

    public Optimistic(){
        this.root = null;
    }

    private boolean validate(LockNode<T> parent, LockNode<T> child) {
        LockNode<T> current = root;     
        while(current!=null) {
            if(current.value.compareTo(parent.value) == 0) {
                if(current.left.value.compareTo(child.value) == 0 || current.right.value.compareTo(child.value) == 0) {
                    return true;
                }
            } else if(current.value.compareTo(parent.value) > 0) {
                current = current.left;
            } else {
                current = current.right;
            }
        }
        return false;
    }

    private boolean validate(LockNode<T> node, LockNode<T> left, LockNode<T> right) {
        LockNode<T> current = root;     
        while(current!=null) {
            if(current.value.compareTo(node.value) == 0) {
                if(current.left == left && current.right == right) {
                    return true;
                }
            } else if(current.value.compareTo(node.value) > 0) {
                current = current.left;
            } else {
                current = current.right;
            }
        }
        return false;
    }

    public boolean isEmpty(){
        return root == null;
    }

    public boolean contains(T value){
        if(!isEmpty()){
            return recursive_contains(value,root);
        }
        return false;
    }

    private boolean recursive_contains(T value, LockNode<T> current){
        if(current != null){
            if(current.value.compareTo(value) == 0){
                if(validate(current,current.left,current.right)) {
                    current.lock();
                    try {
                        return true;
                    } finally {
                        current.unlock();
                    }
                } else {
                    return recursive_contains(value,root);
                }
            } else if(current.value.compareTo(value) > 0){
                return recursive_contains(value,current.left);
            } else {
                return recursive_contains(value,current.right);
            }
        }
        return false;
    }

    public boolean insert(T value){
        if(!contains(value)){
            if(isEmpty()){
                this.root = new LockNode<T>(value);
                return true;
            }
            return recursive_insert(value,root);
        }
        return false;
    }

    private boolean recursive_insert(T value, LockNode<T> current){
        if(current != null){
            if(current.value.compareTo(value) > 0){
                if(current.left == null){
                    current.lock();
                    try{
                        if(validate(current,current.left,current.right)) {
                            current.left = new LockNode<T>(value);
                            return true;
                        } else {
                            return recursive_insert(value,root);
                        }
                    }finally {
                        current.unlock();
                    }
                } else {
                    return recursive_insert(value,current.left);
                }
            }
            if(current.value.compareTo(value) < 0){
                if(current.right == null){
                    current.lock();
                    try{
                        if(validate(current,current.left,current.right)) {
                            current.right = new LockNode<T>(value);
                            return true;
                        } else {
                            return recursive_insert(value,root);
                        }
                    }finally {
                        current.unlock();
                    }
                } else {
                    return recursive_insert(value,current.right);
                }
            }
        }
        return false;
    }

    public boolean remove(T value){
        if(!isEmpty()){
            if(contains(value)){
                if(root.value.compareTo(value) == 0){
                    root.lock(); root.right.lock(); root.left.lock();
                    try {
                        if(validate(root,root.left,root.right)) {
                            if(root.right == null && root.left == null){
                                root = null;
                                return true;
                            } else if(root.right == null && root.left != null){
                                root = root.left;
                                return true;
                            } else if(root.right != null && root.left == null){
                                root = root.right;
                                return true;
                            } else{
                                LockNode<T> left_sub_tree = root.right.left;
                                LockNode<T> left_max = getMax(root.left); 
                                left_max.right = left_sub_tree;
                                root.right.left = root.left;
                                root = root.right;
                                return true;
                            }
                       }
                    } finally {
                        root.unlock();
                        root.left.unlock();
                    }
                }
                return recursive_remove(value,root);
            }
        }
        return false;
    }

    private boolean recursive_remove(T value, LockNode<T> current){
        if(current != null){
            if(current.left != null || current.right != null){
                if(current.value.compareTo(value) > 0){
                    if(current.left != null){
                        if(current.left.value.compareTo(value) == 0){
                            current.lock(); current.left.lock(); current.right.lock();
                            try{
                                if(validate(current,current.left,current.right)) {
                                    return removeNode(current,current.left);     
                                } else {
                                    return recursive_remove(value,root);
                                }
                            } finally {
                                current.unlock();
                                current.left.unlock();
                            }
                            
                        } else{
                            return recursive_remove(value,current.left);
                        }
                    }
                    return false;
                } else{
                    if(current.right != null){
                        if(current.right.value.compareTo(value) == 0){
                            current.lock(); current.left.lock(); current.right.lock();
                            try{
                                if(validate(current,current.left,current.right)) {
                                    return removeNode(current,current.right);     
                                } else {
                                    return recursive_remove(value,root);
                                }
                            } finally {
                                current.unlock();
                                current.right.unlock();
                            }
                        } else{
                            return recursive_remove(value,current.right);
                        }
                    }
                    return false;
                }
            }
        }
        
        return false;
    }

    public LockNode<T> getMax(LockNode<T> root){
       lock.lock();
        try{
            if(root != null){
                if(root.right == null) return root;
                if(root.right != null) return getMax(root.right);
            }
            return null;
        } finally{
            lock.unlock();
        }
    }

    private boolean removeNode(LockNode<T> current,LockNode<T> child){
        if(child.right == null && child.left == null){
            if(current.right == child) {
                current.right = null;
            } else {
                current.left = null;
            }
            return true;
        } else if(child.right == null && child.left != null){
            if(current.right == child) {
                current.right = child.left;
            } else {
                current.left = child.left;
            }
            return true;
        } else if(root.right != null && root.left == null){
            if(current.right == child) {
                current.right = child.right;
            } else {
                current.left = child.right;
            }
            return true;
        } else{
            LockNode<T> left_sub_tree = child.right.left;
            LockNode<T> left_max = getMax(child.left); 
            left_max.right = left_sub_tree;
            child.right.left = child.left;
            if(current.right == child) {
                current.right = child.right;
            } else {
                current.left = child.right;
            }               
            return true;
        }
    }
}
