public class Main {
    public static void main(String[] args) {
        poolTest();
        slowShutdownTest();
    }

    public static void poolTest() {
        ThreadPool threadPool = new ThreadPool(5); 

        for (int i = 0; i < 10; i++) {
            final int taskId = i;
            threadPool.execute(() -> {
                System.out.println("Executing task " + taskId + " on thread " + Thread.currentThread().getName());
            });
        }

        threadPool.shutdown();
    }
    public static void slowShutdownTest() {
        ThreadPool threadPool = new ThreadPool(5); 

        for (int i = 0; i < 10; i++) {
            final int taskId = i;
            threadPool.execute(() -> {
                System.out.println("Executing task " + taskId + " on thread " + Thread.currentThread().getName());
                try {
                    Thread.sleep(1000); // Simulating work
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        try {
            Thread.sleep(2000); // Wait for 2 seconds before shutdown
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Initiating slow shutdown...");
        threadPool.slowShutdown();

        try {
            threadPool.awaitTermination(); // Wait for all tasks to complete
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("All tasks have completed. The pool has shut down officially.");
    }
}

