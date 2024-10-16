import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    public static AtomicInteger counter = new AtomicInteger(0);
    private static final List<Integer> sharedList = new ArrayList<>();
    private static final Random random = new Random();
    public static final ReentrantLock lock = new ReentrantLock();



    public static void main(String[] args) {
/*        System.out.println("Single Thread");
        sequentialRunner();
        System.out.println("\nMulti-threaded (5 threads)");
        testTestRunner();
        System.out.println("\nMulti-threaded (10 threads)");
       testTestRunner3();
        System.out.println("\nMulti-threaded (15 threads)");
        testTestRunner4();*/

        System.out.println("\nMulti-threaded (20 threads)");
        testTestRunner2();
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
        TestRunner runner = new TestRunner(Main.class, 5);
    }

    public static void testTestRunner2() {
        TestRunner runner = new TestRunner(Main.class, 20);
    }


    public static void testTestRunner3() {
        TestRunner runner = new TestRunner(Main.class, 10);
    }

    public static void testTestRunner4() {
        TestRunner runner = new TestRunner(Main.class, 15);
    }


    public static void sequentialRunner() {
        TestRunner runner = new TestRunner(Main.class, 1);
    }

    @TestOrder(2)
    @Test
    public static void testCounterIncrement() {
        for(int i = 0; i < 2000; i++) {
            counter.incrementAndGet();
        }
    }

    @TestOrder(1)
    @Test
    public static void addRandomToList() {
        for (int i = 0; i < 200; i++) {
            int randomNumber = random.nextInt(1000); 
            lock.lock();
            try {
                sharedList.add(randomNumber); 
            } finally {
                lock.unlock();
            }
        }
    }

    @TestOrder(8)
    @Test
    public static void sortList() {
        lock.lock();
        try {
            Sorting.bubbleSortList(sharedList); 
        } finally {
            lock.unlock();
        }
    }

    @TestOrder(7)
    @Test
    public static void decrementCounter() {
        for(int i = 0; i < 1000; i++) {
            counter.decrementAndGet();
        }
    }

    @TestOrder(11)
    @Test
    public static void mathOperationsList() {
        lock.lock();
        try {
            for(int i = 1; i < sharedList.size(); i++) {
                int newValue = sharedList.get(i - 1) * sharedList.get(i);
                sharedList.set(i, newValue);
            }
        } finally {
            lock.unlock();
        }
    }

    @TestOrder(10)
    @Test
    public static void testCounterIncrement2() {
        for(int i = 0; i < 2000; i++) {
            counter.incrementAndGet();
        }
    }


    @TestOrder(1)
    @Test
    public static void addRandomToList2() {
        for (int i = 0; i < 200; i++) {
            int randomNumber = random.nextInt(1000); 
            lock.lock();
            try {
                sharedList.add(randomNumber); 
            } finally {
                lock.unlock();
            }
        }
    }

    @TestOrder(8)
    @Test
    public static void sortList2() {
        lock.lock();
        try {
            Sorting.bubbleSortList(sharedList); 
        } finally {
            lock.unlock();
        }
    }

    @TestOrder(7)
    @Test
    public static void decrementCounter2() {
        for(int i = 0; i < 1000; i++) {
            counter.decrementAndGet();
        }
    }

    @TestOrder(11)
    @Test
    public static void mathOperationsList2() {
        lock.lock();
        try {
            for(int i = 1; i < sharedList.size(); i++) {
                int newValue = sharedList.get(i - 1) * sharedList.get(i);
                sharedList.set(i, newValue);
            }
        } finally {
            lock.unlock();
        }
    }

public static AtomicInteger counter2 = new AtomicInteger(0);

    @TestOrder(4)
    @Test
    public static void testCounterIncrement3() {
        for(int i = 0; i < 100; i++) {
            counter2.incrementAndGet();
        }
    }

    @TestOrder(1)
    @Test
    public static void addRandomToList3() {
        for (int i = 0; i < 20; i++) {
            int randomNumber = random.nextInt(1000); 
            lock.lock();
            try {
                sharedList.add(randomNumber); 
            } finally {
                lock.unlock();
            }
        }
    }

    @TestOrder(8)
    @Test
    public static void sortList3() {
        lock.lock();
        try {
            Sorting.bubbleSortList(sharedList); 
        } finally {
            lock.unlock();
        }
    }

    @TestOrder(7)
    @Test
    public static void decrementCounter3() {
        for(int i = 0; i < 100; i++) {
            counter.decrementAndGet();
        }
    }

    @TestOrder(11)
    @Test
    public static void mathOperationsList3() {
        lock.lock();
        try {
            for(int i = 1; i < sharedList.size(); i++) {
                int newValue = sharedList.get(i - 1) * sharedList.get(i);
                sharedList.set(i, newValue);
            }
        } finally {
            lock.unlock();
        }
    }

    @TestOrder(4)
    @Test
    public static void testCounterIncrement4() {
        for(int i = 0; i < 100; i++) {
            counter2.incrementAndGet();
        }
    }

    @TestOrder(1)
    @Test
    public static void addRandomToList4() {
        for (int i = 0; i < 20; i++) {
            int randomNumber = random.nextInt(1000); 
            lock.lock();
            try {
                sharedList.add(randomNumber); 
            } finally {
                lock.unlock();
            }
        }
    }

    @TestOrder(8)
    @Test
    public static void sortList4() {
        lock.lock();
        try {
            Sorting.bubbleSortList(sharedList); 
        } finally {
            lock.unlock();
        }
    }

    @TestOrder(7)
    @Test
    public static void decrementCounter4() {
        for(int i = 0; i < 100; i++) {
            counter.decrementAndGet();
        }
    }

    @TestOrder(11)
    @Test
    public static void mathOperationsList4() {
        lock.lock();
        try {
            for(int i = 1; i < sharedList.size(); i++) {
                int newValue = sharedList.get(i - 1) * sharedList.get(i);
                sharedList.set(i, newValue);
            }
        } finally {
            lock.unlock();
        }
    }

    @TestOrder(4)
    @Test
    public static void testCounterIncrement5() {
        for(int i = 0; i < 100; i++) {
            counter2.incrementAndGet();
        }
    }

    @TestOrder(1)
    @Test
    public static void addRandomToList5() {
        for (int i = 0; i < 20; i++) {
            int randomNumber = random.nextInt(1000); 
            lock.lock();
            try {
                sharedList.add(randomNumber); 
            } finally {
                lock.unlock();
            }
        }
    }

    @TestOrder(8)
    @Test
    public static void sortList5() {
        lock.lock();
        try {
            Sorting.bubbleSortList(sharedList); 
        } finally {
            lock.unlock();
        }
    }

    @TestOrder(7)
    @Test
    public static void decrementCounter5() {
        for(int i = 0; i < 100; i++) {
            counter.decrementAndGet();
        }
    }

    @TestOrder(11)
    @Test
    public static void mathOperationsList5() {
        lock.lock();
        try {
            for(int i = 1; i < sharedList.size(); i++) {
                int newValue = sharedList.get(i - 1) * sharedList.get(i);
                sharedList.set(i, newValue);
            }
        } finally {
            lock.unlock();
        }
    }

    @TestOrder(4)
    @Test
    public static void testCounterIncrement6() {
        for(int i = 0; i < 100; i++) {
            counter2.incrementAndGet();
        }
    }

    @TestOrder(1)
    @Test
    public static void addRandomToList6() {
        for (int i = 0; i < 20; i++) {
            int randomNumber = random.nextInt(1000); 
            lock.lock();
            try {
                sharedList.add(randomNumber); 
            } finally {
                lock.unlock();
            }
        }
    }

    @TestOrder(8)
    @Test
    public static void sortList6() {
        lock.lock();
        try {
            Sorting.bubbleSortList(sharedList); 
        } finally {
            lock.unlock();
        }
    }

    @TestOrder(7)
    @Test
    public static void decrementCounter6() {
        for(int i = 0; i < 100; i++) {
            counter.decrementAndGet();
        }
    }

    @TestOrder(11)
    @Test
    public static void mathOperationsList6() {
        lock.lock();
        try {
            for(int i = 1; i < sharedList.size(); i++) {
                int newValue = sharedList.get(i - 1) * sharedList.get(i);
                sharedList.set(i, newValue);
            }
        } finally {
            lock.unlock();
        }
    }



    @TestOrder(1)
    @Test
    public static void bubbleSortArray() {
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

        Sorting.bubblesort(array);
    }

    @Test
    public static void insertionSortArray() {
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

        Sorting.insertionSort(array);
    }


}

