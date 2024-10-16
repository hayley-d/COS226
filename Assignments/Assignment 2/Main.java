import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    
    public static Queue<Integer> queue = new Queue<Integer>();

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
    public static void testCounterIncrement() {
        AtomicInteger counter = new AtomicInteger(0);
        System.out.println("Incrementing counter");
        for(int i = 0; i < 2000; i++) {
            counter.incrementAndGet();
        }
    }

    @TestOrder(1)
    @Test
    public static void arraySort() {
        int[] array = {
            120, 450, 932, 763, 812, 251, 612, 93, 440, 198, 571, 730, 674, 552, 83, 122, 
            995, 202, 653, 823, 201, 349, 902, 67, 741, 438, 811, 56, 672, 180, 145, 688, 
            314, 457, 932, 245, 766, 333, 81, 672, 917, 54, 639, 792, 364, 285, 114, 632, 
            47, 908, 347, 123, 563, 983, 346, 288, 149, 485, 729, 315, 879, 432, 158, 912, 
            75, 432, 524, 837, 612, 981, 193, 719, 254, 894, 135, 762, 413, 682, 975, 211, 
            398, 541, 786, 245, 623, 512, 120, 878, 169, 290, 632, 785, 494, 235, 401, 600, 
            789, 180, 274, 653, 794, 335, 499, 254, 920, 511, 807, 123, 490, 349, 901, 370, 
            76, 512, 438, 601, 322, 457, 688, 937, 114, 690, 238, 178, 504, 81, 930, 423, 
            512, 173, 882, 325, 789, 473, 160, 297, 843, 699, 598, 412, 180, 372, 719, 450, 
            911, 642, 983, 481, 723, 320, 173, 585, 423, 801, 591, 763, 281, 647, 836, 191, 
            576, 829, 298, 174, 392, 659, 305, 860, 674, 719, 903, 136, 781, 290, 541, 843, 
            927, 512, 174, 698, 125, 432, 583, 754, 671, 271, 948, 312, 821, 472, 509, 104 
        };
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

