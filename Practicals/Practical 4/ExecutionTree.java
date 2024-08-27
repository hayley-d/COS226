import java.util.*;

class Node {
    Integer value;
    String path;
    List<Node> children;
    List<Label> labels;
    HashSet<Integer> childrenValues;
    Integer childValue = 0;

    public Node(Integer value, String path) {
        this.value = value;
        this.path = path;
        this.children = new ArrayList<>();
        this.labels = new ArrayList<>();
        this.childrenValues = new HashSet<>();
    }

    public Node(String path) {
        this.value = null;
        this.path = path;
        this.children = new ArrayList<>();
        this.labels = new ArrayList<>();
        this.childrenValues = new HashSet<>();
    }

    public void addChild(Node child) {
        this.children.add(child);
    }

    public void addLabel(Label l) {
        this.labels.add(l);
    }

    // Maybe adding a method here would wake it easier ¯\_(ツ)_/¯

    public void printTree(String prefix, boolean isTail) {

        System.out.println(
                prefix + (isTail ? "└── " : "├── ") + path + " Labels:" + labels + " value: (" + value + ")");
        for (int i = 0; i < children.size() - 1; i++) {
            children.get(i).printTree(prefix + (isTail ? "    " : "│   "), false);
        }
        if (children.size() > 0) {
            children.get(children.size() - 1).printTree(prefix + (isTail ? "    " : "│   "), true);
        }
    }

}

public class ExecutionTree {
    public Random r;
    public Node root;

    public void assignLabels() {
        // This is yours to fill in
        if(root == null) return;
        populateValues(root);
        postOrder(root); 
        criticalCheck(root);    
    }
    
    private void populateValues(Node current){
        if(current == null) return;
        
        for(Node n: current.children){
            populateValues(n);
        }
        
        if(current.children.isEmpty()){
            current.childValue = current.value;
        }
        else if(current.children.size() == 1){
            current.childValue = current.children.get(0).childValue;
        }else{
            boolean sameValue = true;
            for(int i = 0; i < current.children.size()-1;++i){
                for(int j = i + 1; j < current.children.size();++j){
                    if(current.children.get(i).childValue != current.children.get(j).childValue){
                        sameValue = false;
                        break;
                    }
                }
                if(!sameValue) break;
            }
            if(sameValue) current.childValue = current.children.get(0).childValue;
            if(!sameValue) current.childValue = null;
        }

    }

/*    private void postOrder(Node current){
        if(current == null) return;
        
        for(Node n: current.children){
            postOrder(n);
        }

        if(current.children.isEmpty()){
            current.addLabel(Label.FINAL);
            return;
        } else if(current.children.size() == 1){
            current.addLabel(Label.UNIVALENT);
            return;
        } else{
            if(current.childrenValues.size() > 1){
                current.addLabel(Label.BIVALENT);
            } else {
                 current.addLabel(Label.UNIVALENT);
            }
            return;
        }
    }*/
    private void postOrder(Node current){
        if(current == null) return;
        
        for(Node n: current.children){
            postOrder(n);
        }

        if(current.children.isEmpty()){
            current.addLabel(Label.FINAL);
            return;
        } else if(current.childValue != null){
            current.addLabel(Label.UNIVALENT);
            return;
        } else{
            current.addLabel(Label.BIVALENT);
            return;
        }
    }



    private void dfs(Node current){
        if(current == null){
          return;
        }

        if(current.children.isEmpty()){
            current.labels.add(Label.FINAL);
            return;
        } else{
            boolean bivalent = isBivalent(current,current.value); 
            if(bivalent){
                 current.labels.add(Label.BIVALENT);
            } else{
                 current.labels.add(Label.UNIVALENT);
            }
        }
        
        for(Node n: current.children){
            dfs(n);
        }
    }

    private void criticalCheck(Node current){
        if(current == null) return;
        if(current.labels.contains(Label.BIVALENT)){
            boolean isCrit = true;
            for(Node n: current.children){
               if(n.labels.contains(Label.BIVALENT)){
                   isCrit = false;
                  break;
                } 
            }
            if(isCrit){
                current.labels.add(Label.CRITICAL);
            } else{ 
                for(Node n : current.children){
                    criticalCheck(n);
                }
            } 
        }
    }

    private boolean isBivalent(Node current, Integer value){
        if(current == null){
            return false;
        }
        
        if(current.value != value){
            return true;
        }

        for(Node n : current.children){
            if(isBivalent(n,value)) return true;
        }
        return false;
    }

    // I wouldn't change this if I were you
    public ExecutionTree(List<Character> threads, int n, int seed) {
        this.r = new Random(seed);
        int[] counts = new int[threads.size()]; // Array to keep track of counts

        this.root = new Node("");

        // Start the generation with each character
        for (int i = 0; i < threads.size(); i++) {
            counts[i]++;
            root.addChild(generateTree(threads.get(i) + "", counts, n, threads));
            counts[i]--; // Backtrack
        }

        root.addLabel(Label.INITIAL);
//        root.addLabel(Label.BIVALENT);

    }

    public Node generateTree(String path, int[] counts, int n, List<Character> chars) {
        boolean isComplete = true;
        for (int count : counts) {
            if (count < n) {
                isComplete = false;
                break;
            }
        }
        if (isComplete) {
            return new Node(r.nextInt(counts.length), path);
        }

        Node node = new Node(path);

        for (int i = 0; i < chars.size(); i++) {
            if (counts[i] < n) {
                counts[i]++;
                node.addChild(generateTree(path + chars.get(i), counts, n, chars));
                counts[i]--; // Backtrack
            }
        }
        return node;
    }

}
