import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.Random;

class Main{
    private static int[] numbers = {
            5, 12, 15, 23, 42, 56, 64, 72, 88, 95, 102, 116, 124, 137, 149, 158, 168, 179, 182, 196, 
            204, 216, 223, 234, 246, 253, 265, 276, 289, 297, 305, 316, 327, 335, 345, 359, 368, 379, 
            389, 396, 405, 418, 423, 434, 445, 459, 467, 475, 489, 495, 502, 515, 523, 534, 546, 559,
            568, 579, 589, 597, 605, 619, 623, 635, 647, 653, 668, 678, 689, 699, 704, 716, 729, 735,
            746, 758, 769, 777, 785, 796, 803, 815, 826, 837, 849, 853, 867, 879, 888, 899, 908, 916,
            929, 945, 956, 967, 973, 986, 995, 1004, 1016, 1034, 1045, 1059, 1068, 1079, 1095, 1103,
            1115, 1126, 1137, 1149, 1159, 1176, 1185, 1199, 1208, 1215, 1226, 1239, 1251, 1267, 1273,
            1285, 1298, 1309, 1317, 1332, 1345, 1353, 1368, 1379, 1390, 1403, 1415, 1428, 1439, 1450,
            1465, 1476, 1489, 1505, 1518, 1527, 1539, 1550, 1563, 1574, 1589, 1602, 1615, 1629, 1641,
            1655, 1669, 1679, 1692, 1705, 1716, 1728, 1739, 1753, 1765, 1779, 1790, 1805, 1817, 1832,
            1849, 1855, 1869, 1883, 1897, 1909, 1918, 1932, 1945, 1959, 1967, 1980, 1995, 2002, 2015
        };

    public static void main(String[] args){

       courseTest(); 
       fineTest();
        optimisticTest();
    }

    private static void courseTest(){
        CourseGrainedTree<Integer> tree = new CourseGrainedTree<Integer>();
        System.out.println("Testing Course-Grained Synchronization:");
        System.out.println("\n5 Threads:");
        testInsertions(tree,5);
        testLookups(tree,5);
        testRemoval(tree,5);

        tree = new CourseGrainedTree<Integer>();
        System.out.println("\n10 Threads:");
        testInsertions(tree,10);
        testLookups(tree,10);
        testRemoval(tree,10);

        tree = new CourseGrainedTree<Integer>();
        System.out.println("\n15 Threads:");
        testInsertions(tree,15);
        testLookups(tree,15);
        testRemoval(tree,15);

        CourseGrainedTree<Integer> tree2 = new CourseGrainedTree<Integer>();
        System.out.println("\n20 Threads:");
        testInsertions(tree2,20);
        testLookups(tree2,20);
        testRemoval(tree2,20);
    }

    private static void fineTest(){
        FineGrainedTree<Integer> tree = new FineGrainedTree<Integer>();
        System.out.println("\nTesting Fine-Grained Synchronization:");
        System.out.println("\n5 Threads:");
        testInsertions(tree,5);
        testLookups(tree,5);
        testRemoval(tree,5);

        tree = new FineGrainedTree<Integer>();
        System.out.println("\n10 Threads:");
        testInsertions(tree,10);
        testLookups(tree,10);
        testRemoval(tree,10);

        tree = new FineGrainedTree<Integer>();
        System.out.println("\n15 Threads:");
        testInsertions(tree,15);
        testLookups(tree,15);
        testRemoval(tree,15);

        FineGrainedTree<Integer> tree2 = new FineGrainedTree<Integer>();
        System.out.println("\n20 Threads");
        testInsertions(tree2,20);
        testLookups(tree2,20);
        testRemoval(tree2,20);
    }

    private static void optimisticTest(){
        OptimisticTree<Integer> tree = new OptimisticTree<Integer>();
        System.out.println("Testing Optimistic Synchronization:");
        System.out.println("\n5 Threads:");
        testInsertions(tree,5);
        testLookups(tree,5);
        testRemoval(tree,5);

        tree = new OptimisticTree<Integer>();
        System.out.println("\n10 Threads:");
        testInsertions(tree,10);
        testLookups(tree,10);
        testRemoval(tree,10);

        tree = new OptimisticTree<Integer>();
        System.out.println("\n15 Threads:");
        testInsertions(tree,15);
        testLookups(tree,15);
        testRemoval(tree,15);

        OptimisticTree<Integer> tree2 = new OptimisticTree<Integer>();
        System.out.println("\n20 Threads:");
        testInsertions(tree2,20);
        testLookups(tree2,20);
        testRemoval(tree2,20);
    }

    private static void testInsertions(OptimisticTree<Integer> tree, int numThreads) {
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        Random random = new Random();
        long startTime = System.nanoTime();
        
        for (int i = 0; i < 10; i++) {
            executor.submit(() -> {
                //each thead inserts 10 random integers from the array
                for (int j = 0; j < 10; j++) { 
                    int index = random.nextInt(numbers.length);
                    int value = numbers[index];
                    tree.insert(value);
                }
            });
        }

        shutdownExecutor(executor);

        long endTime = System.nanoTime();
        System.out.println("Insertion test completed in: " + (endTime - startTime) + " nanoseconds");
    }

    private static void testLookups(OptimisticTree<Integer> tree, int numThreads) {
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        Random random = new Random();
        long startTime = System.nanoTime();
        
        for (int i = 0; i < 10; i++) {
            executor.submit(() -> {
                //each thead searches 10 random integers from the array
                for (int j = 0; j < 10; j++) { 
                    int index = random.nextInt(numbers.length);
                    int value = numbers[index];
                    tree.contains(value);
                }
            });
        }

        shutdownExecutor(executor);

        long endTime = System.nanoTime();
        System.out.println("Lookup test completed in: " + (endTime - startTime) + " nanoseconds");
    }

    private static void testRemoval(OptimisticTree<Integer> tree, int numThreads) {
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        Random random = new Random();
        long startTime = System.nanoTime();
        
        for (int i = 0; i < 10; i++) {
            executor.submit(() -> {
                //each thead removes 10 random integers from the array
                for (int j = 0; j < 10; j++) { 
                    int index = random.nextInt(numbers.length);
                    int value = numbers[index];
                    tree.insert(value);
                }
            });
        }

        shutdownExecutor(executor);

        long endTime = System.nanoTime();
        System.out.println("Removal test completed in: " + (endTime - startTime) + " nanoseconds");
    }

    private static void testInsertions(FineGrainedTree<Integer> tree, int numThreads) {
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        Random random = new Random();
        long startTime = System.nanoTime();
        
        for (int i = 0; i < 10; i++) {
            executor.submit(() -> {
                //each thead inserts 10 random integers from the array
                for (int j = 0; j < 10; j++) { 
                    int index = random.nextInt(numbers.length);
                    int value = numbers[index];
                    tree.insert(value);
                }
            });
        }

        shutdownExecutor(executor);

        long endTime = System.nanoTime();
        System.out.println("Insertion test completed in: " + (endTime - startTime) + " nanoseconds");
    }

    private static void testLookups(FineGrainedTree<Integer> tree, int numThreads) {
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        Random random = new Random();
        long startTime = System.nanoTime();
        
        for (int i = 0; i < 10; i++) {
            executor.submit(() -> {
                //each thead searches 10 random integers from the array
                for (int j = 0; j < 10; j++) { 
                    int index = random.nextInt(numbers.length);
                    int value = numbers[index];
                    tree.contains(value);
                }
            });
        }

        shutdownExecutor(executor);

        long endTime = System.nanoTime();
        System.out.println("Lookup test completed in: " + (endTime - startTime) + " nanoseconds");
    }

    private static void testRemoval(FineGrainedTree<Integer> tree, int numThreads) {
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        Random random = new Random();
        long startTime = System.nanoTime();
        
        for (int i = 0; i < 10; i++) {
            executor.submit(() -> {
                //each thead removes 10 random integers from the array
                for (int j = 0; j < 10; j++) { 
                    int index = random.nextInt(numbers.length);
                    int value = numbers[index];
                    tree.insert(value);
                }
            });
        }

        shutdownExecutor(executor);

        long endTime = System.nanoTime();
        System.out.println("Removal test completed in: " + (endTime - startTime) + " nanoseconds");
    }

    private static void testInsertions(CourseGrainedTree<Integer> tree, int numThreads) {
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        Random random = new Random();
        long startTime = System.nanoTime();
        
        for (int i = 0; i < 10; i++) {
            executor.submit(() -> {
                //each thead inserts 10 random integers from the array
                for (int j = 0; j < 10; j++) { 
                    int index = random.nextInt(numbers.length);
                    int value = numbers[index];
                    tree.insert(value);
                }
            });
        }

        shutdownExecutor(executor);

        long endTime = System.nanoTime();
        System.out.println("Insertion test completed in: " + (endTime - startTime) + " nanoseconds");
    }

    private static void testLookups(CourseGrainedTree<Integer> tree, int numThreads) {
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        Random random = new Random();
        long startTime = System.nanoTime();
        
        for (int i = 0; i < 10; i++) {
            executor.submit(() -> {
                //each thead searches 10 random integers from the array
                for (int j = 0; j < 10; j++) { 
                    int index = random.nextInt(numbers.length);
                    int value = numbers[index];
                    tree.contains(value);
                }
            });
        }

        shutdownExecutor(executor);

        long endTime = System.nanoTime();
        System.out.println("Lookup test completed in: " + (endTime - startTime) + " nanoseconds");
    }

    private static void testRemoval(CourseGrainedTree<Integer> tree, int numThreads) {
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        Random random = new Random();
        long startTime = System.nanoTime();
        
        for (int i = 0; i < 10; i++) {
            executor.submit(() -> {
                //each thead removes 10 random integers from the array
                for (int j = 0; j < 10; j++) { 
                    int index = random.nextInt(numbers.length);
                    int value = numbers[index];
                    tree.insert(value);
                }
            });
        }

        shutdownExecutor(executor);

        long endTime = System.nanoTime();
        System.out.println("Removal test completed in: " + (endTime - startTime) + " nanoseconds");
    }


    private static void shutdownExecutor(ExecutorService executor) {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException ex) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }



    public static void printTree(Node<Integer> root) {
        printTree(root, 0);
    }

    private static void printTree(Node<Integer> node, int depth) {
        if (node == null) {
            return;
        }
        
        printTree(node.right, depth + 1);
        
        System.out.println("  ".repeat(depth) + node.value);
        
        printTree(node.left, depth + 1);
    }

}
