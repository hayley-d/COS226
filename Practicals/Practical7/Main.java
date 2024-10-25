import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.Random;

 class Main {
     public static void main(String[] args) {
         LockFreeStack<Integer> lock_free = new LockFreeStack();
         EliminationBackoffStack<Integer> elim_stack = new EliminationBackoffStack<Integer>(5);
         SequentialStack<Integer> stack = new SequentialStack<Integer>();
         
         int[] threadCounts = {1, 5, 10, 15, 20};

         try{
            for (int threadCount : threadCounts) {
                System.out.println("\n" + threadCount + " Thread Test:");
                testLockFree(lock_free, threadCount);
                System.out.println("\n" + threadCount + " Thread Test:");
                testEliminationStack(elim_stack, threadCount);
                System.out.println("\n" + threadCount + " Thread Test:");
                testSequential(stack,threadCount);
             }
         } catch (Exception e) {
         }
     }

     protected static void testLockFree(LockFreeStack<Integer> lock_free, int threadCount) {
         ExecutorService executor = Executors.newFixedThreadPool(threadCount);
         Random random = new Random();
         long startTime = System.nanoTime();

         try {
             for(int i = 0; i < threadCount; i++) {
                 executor.submit(() -> {
                     for(int j = 0; j < 3; j++) {
                        Integer value = random.nextInt();
                        lock_free.push(value);
                     }
                 });
             }

            for(int i = 0; i < 10; i++) {
                 executor.submit(() -> {
                     for(int j = 0; j < 3; j++) {
                        try {
                            Integer value = lock_free.pop();
                        } catch (Exception e) {
                        }
                     }
                 });
             }
         } catch (Exception e) {
            return;
         } 

         long endTime = System.nanoTime();
         System.out.println("Lock-Free Stack Push Time: " + (endTime-startTime));

         startTime = System.nanoTime();
         try {
             for(int i = 0; i < threadCount; i++) {
                 executor.submit(() -> {
                     for(int j = 0; j < 3; j++) {
                        Integer value = random.nextInt();
                        lock_free.push(value);
                     }
                 });
             }

            for(int i = 0; i < 10; i++) {
                 executor.submit(() -> {
                     for(int j = 0; j < 3; j++) {
                        try {
                            Integer value = lock_free.pop();
                        } catch (Exception e) {
                        }
                     }
                 });
             }
         } catch (Exception e) {
            return;
         }finally {
             endTime = System.nanoTime();
             System.out.println("Lock-Free Stack Pop Time: " + (endTime-startTime));
             shutdownExecutor(executor);
         }
     }

     protected static void testEliminationStack(EliminationBackoffStack<Integer> stack, int threadCount) {
         ExecutorService executor = Executors.newFixedThreadPool(threadCount);
         Random random = new Random();
         long startTime = System.nanoTime();

         try {
             for(int i = 0; i < threadCount; i++) {
                 executor.submit(() -> {
                     for(int j = 0; j < 3; j++) {
                        Integer value = random.nextInt();
                        stack.push(value);
                     }
                 });
             }

            for(int i = 0; i < 10; i++) {
                 executor.submit(() -> {
                     for(int j = 0; j < 3; j++) {
                        try {
                            Integer value = stack.pop();
                        } catch (Exception e) {
                        }
                     }
                 });
             }
         } catch (Exception e) {
            return;
         } 

         long endTime = System.nanoTime();
         System.out.println("Elimination Backoff Stack Push Time: " + (endTime-startTime));

         startTime = System.nanoTime();
         try {
             for(int i = 0; i < threadCount; i++) {
                 executor.submit(() -> {
                     for(int j = 0; j < 3; j++) {
                        Integer value = random.nextInt();
                        stack.push(value);
                     }
                 });
             }

            for(int i = 0; i < 10; i++) {
                 executor.submit(() -> {
                     for(int j = 0; j < 3; j++) {
                        try {
                            Integer value = stack.pop();
                        } catch (Exception e) {
                        }
                     }
                 });
             }
         } catch (Exception e) {
            return;
         }finally {
             endTime = System.nanoTime();
             System.out.println("Elimination Backoff Stack Pop Time: " + (endTime-startTime));
             shutdownExecutor(executor);
         }
     }

     protected static void testSequential(SequentialStack<Integer> stack, int threadCount) {
         ExecutorService executor = Executors.newFixedThreadPool(threadCount);
         Random random = new Random();
         long startTime = System.nanoTime();

         try {
             for(int i = 0; i < threadCount; i++) {
                 executor.submit(() -> {
                     for(int j = 0; j < 3; j++) {
                        Integer value = random.nextInt();
                        stack.push(value);
                     }
                 });
             }

            for(int i = 0; i < 10; i++) {
                 executor.submit(() -> {
                     for(int j = 0; j < 3; j++) {
                        try {
                            Integer value = stack.pop();
                        } catch (Exception e) {
                        }
                     }
                 });
             }
         } catch (Exception e) {
            return;
         } 

         long endTime = System.nanoTime();
         System.out.println("Sequential Stack Push Time: " + (endTime-startTime));

         startTime = System.nanoTime();
         try {
             for(int i = 0; i < threadCount; i++) {
                 executor.submit(() -> {
                     for(int j = 0; j < 3; j++) {
                        Integer value = random.nextInt();
                        stack.push(value);
                     }
                 });
             }

            for(int i = 0; i < 10; i++) {
                 executor.submit(() -> {
                     for(int j = 0; j < 3; j++) {
                        try {
                            Integer value = stack.pop();
                        } catch (Exception e) {
                        }
                     }
                 });
             }
         } catch (Exception e) {
            return;
         }finally {
             endTime = System.nanoTime();
             System.out.println("Sequential Stack Pop Time: " + (endTime-startTime));
             shutdownExecutor(executor);
         }
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

 }
