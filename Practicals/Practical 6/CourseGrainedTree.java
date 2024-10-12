class CourseGrainedTree<T>{
    public Node<T> root;
    private Lock lock = new ReentrantLock();

    public CourseGrainedTree(){
        this.root = null;
    }

    public boolean isEmpty(){
        return root == null;
    }

    public boolean contains(T value){
        lock.lock();
        try{
            if(!isEmpty()){
                return recursive_contains(value,root);
            }
            return false;
        } finally{
            lock.unlock();
        }
    }

    // helper method does not aquire lock again since aquired by calling method
    private boolean recursive_contains(T value, Node<T> current){
        if(current != null){
            if(current.value == value){
                return true;
            } else if(current.value > value){
                return recursive_contains(value,current.left);
            } else {
                return recursive_contains(value,current.right);
            }
        }
        return false;
    }

    public boolean insert(T value){
        lock.lock();
        try{
            if(!contains(value)){
                if(isEmpty()){
                    this.root = new Node<T>(value);
                    return true;
                }
                return recursive_insert(value,root);
            }
            return false;
        } finally{
            lock.unlock();
        }
    }

    private boolean recursive_insert(T value, Node<T> current){
        if(current != null){
            if(current.value > value){
                if(current.left == null){
                    current.left = new Node<T>(value);
                    return true;
                } else {
                    return revursive_insert(value,current.left);
                }
            }
            if(current.value < value){
                if(current.right == null){
                    current.right = new Node<T>(value);
                    return true;
                } else {
                    return revursive_insert(value,current.right);
                }
            }
        }
        return false;
    }

    public boolean remove(T value){
        lock.lock();
        try{
            if(!isEmpty(){
                if(contains(value)){
                    if(root.value == value){
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
                            Node<T> left_sub_tree = root.right.left;
                            Node<T> left_max = getMax(root.left); 
                            left_max.right = left_sub_tree;
                            root.right.left = root.left;
                            root = root.right;
                            return true;
                        }
                    }
                    return recursive_remove(value,root);
                }
            }
            return false;
        } finally{
            lock.unlock();
        }
    }

    private boolean recursive_remove(T value, Node<T> current){
        if(current != null){
            if(current.left != null || current.right != null){
                if(current.value > value){
                    if(current.left != null){
                        if(current.left.value == value){
                            return removeNode(current.left);
                        } else{
                            return recursive_remove(value,current.left);
                        }
                    }
                    return false;
                } else{
                    if(current.right != null){
                        if(current.right.value == value){
                            return removeNode(current.right);
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

    public Node<T> getMax(Node<T> root){
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

    private boolean removeNode(Node<T> root){
        lock.lock();
        try{
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
                Node<T> left_sub_tree = root.right.left;
                Node<T> left_max = getMax(root.left); 
                left_max.right = left_sub_tree;
                root.right.left = root.left;
                root = root.right;
                return true;
            }
        } finally{
            lock.unlock();
        }
    }
}
