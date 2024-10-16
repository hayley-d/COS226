import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TestRunner {
    private final AtomicInteger totalTests = new AtomicInteger(0);
    private final AtomicInteger passedTests = new AtomicInteger(0);
    private final AtomicInteger failedTests = new AtomicInteger(0);
    private final AtomicInteger skippedTests = new AtomicInteger(0);
    private final long startTime;
    
    public TestRunner(Class<?> testClass,int numberOfThreads) {
        this.startTime = System.nanoTime();
        runTests(testClass,numberOfThreads);
    }

    public void runTests(Class<?> testClass, int numberOfThreads) {
        int count = 0;
        ThreadPool threadPool = new ThreadPool(numberOfThreads);
        List<Method> testMethods = new ArrayList<>();

        for (Method method : testClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Test.class)) {
                System.out.println("Adding test #" + count);
                count++;
                testMethods.add(method);
            }
        }

        testMethods.sort(Comparator.comparingInt(method -> {
            if (method.isAnnotationPresent(TestOrder.class)) {
                return method.getAnnotation(TestOrder.class).value();
            }
            return Integer.MAX_VALUE;
        }));

        for (Method testMethod : testMethods) {
            totalTests.incrementAndGet();
            threadPool.execute(() -> {
                try {
                    System.out.println(testMethod.getName() + " passed.");
                    passedTests.incrementAndGet();
                } catch (Exception e) {
                    System.out.println(testMethod.getName() + " failed: " + e.getCause());
                    failedTests.incrementAndGet();
                }
            });
        }

        System.out.println("Initiating shutdown...");
        threadPool.slowShutdown();
        try {
            threadPool.awaitTermination(); // Wait for all tasks to complete
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        long totalTime = System.nanoTime() - startTime;

        System.out.println("Test Summary:");
        System.out.println("Total Tests: " + totalTests.get());
        System.out.println("Passed: " + passedTests.get());
        System.out.println("Failed: " + failedTests.get());
        System.out.println("Skipped: " + skippedTests.get());
        System.out.println("Total Time: " + totalTime + " ns"); 
    }

}

