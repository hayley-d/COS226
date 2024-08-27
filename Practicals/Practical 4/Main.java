import java.util.*;

public class Main {
    public static void main(String[] args) {
        Random r = new Random(100);
        int bound = 50000;
        int[] tests = { 2, 2 * 2, 2 * 2 * 2, 2 * 2 * 2 * 2, 2 * 2 * 2 * 2 * 2 };
        for (int i = 1; i <= 5; i++) {
            System.out.println("###");
            HashSet<Integer> seenSeeds = new HashSet<>();
            for (int j = 0; j < tests[i - 1]; j++) {
                Integer nextSeed = r.nextInt(bound);
                while (seenSeeds.contains(nextSeed)) {
                    nextSeed = r.nextInt(bound);
                }
                seenSeeds.add(nextSeed);
                tree(i, nextSeed);
            }
        }
        System.out.println("###");

    }
    
    public static void tree(int size, int seed){
        int n = 2; //num operations per thread
        List<Character> threads = List.of('A','B');
        ExecutionTree e = new ExecutionTree(threads, n, seed);
        e.assignLabels();
        e.root.printTree("a",true);
    }

    public static void myTreeTest(){
        List<Character> threads = List.of('A','B','C');
        int numOps = 3;
        Random r = new Random(100);
        Integer seed = r.nextInt(50000);

        ExecutionTree myTree = new ExecutionTree(threads,numOps,seed);
        myTree.assignLabels();
        myTree.root.printTree(" ",true);
    }
}
