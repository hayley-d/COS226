import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        poolTest();
        slowShutdownTest();
        testTestRunner();
        System.out.println("Test Runner finished.");
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

    public static void testTestRunner() {
        TestRunner runner = new TestRunner(Main.class);
        System.out.println("Done");
    }

    @TestOrder(2)
    @Test
    public static void testMethodA() {
        System.out.println("Test Method A is running.");
    }

    @TestOrder(1)
    @Test
    public static void testMethodB() {
        System.out.println("Test Method B is running.");
    }

    @Test
    public static void testMethodC() {
        System.out.println("Test Method C is running.");
    }

    @TestOrder(3)
    @Test
    public void testMethodD() {
        System.out.println("Test Method D is running.");
    }

}

