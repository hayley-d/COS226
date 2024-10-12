class Node<T extends Comparable<T>>{
    public T value;
    public Node<T> left;
    public Node<T> right;

    public Node(T value){
        this.left = null;
        this.right = null;
        this.value = value;
    }
}
