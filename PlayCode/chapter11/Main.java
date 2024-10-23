import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.Random;

 class Main {
     public static void main(String[] args) {
         LockFreeStack<Integer> lock_free = new LockFreeStack();

         try{
             testLockFree(lock_free);
         } catch (Exception e) {
             System.out.println("Empty stack!");
         }
     }

     protected static void testLockFree(LockFreeStack<Integer> lock_free) {
         ExecutorService executor = Executors.newFixedThreadPool(10);
         Random random = new Random();
         try {
             for(int i = 0; i < 10; i++) {
                 executor.submit(() -> {
                     for(int j = 0; j < 3; j++) {
                        Integer value = random.nextInt();
                        lock_free.push(value);
                        System.out.println("Pushed "+ value);
                     }
                 });
             }

            for(int i = 0; i < 10; i++) {
                 executor.submit(() -> {
                     for(int j = 0; j < 3; j++) {
                        try {
                            Integer value = lock_free.pop();
                            System.out.println("Pushed "+ value);
                        } catch (Exception e) {
                            System.out.println("Empty stack");
                        }
                     }
                 });
             }
         } catch (Exception e) {
            System.out.println("Empty stack");
            return;
         } finally {
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
