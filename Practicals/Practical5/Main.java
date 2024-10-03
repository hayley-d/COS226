import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

public class Main {

  static Deque<Integer> queue = new LinkedList<>();

  static int counter;

  public static void main(String[] args) {
    counter = 0;

    for (int i = 1; i <= 20; i++) {
      queue.addLast(i);
    }

    printQueue();

    System.out.println("Backoff Lock with low contention:");
    ExponentialBackoffTest(3);

    queue.clear();
    for (int i = 1; i <= 70; i++) {
      queue.addLast(i);
    }
    System.out.println("Backoff Lock with high contention:");
    ExponentialBackoffTest(20);

    queue.clear();
    for (int i = 1; i <= 10; i++) {
      queue.addLast(i);
    }
    System.out.println("Backoff Lock with no contention:");
    ExponentialBackoffTest(1);

    queue.clear();
    for (int i = 1; i <= 10; i++) {
      queue.addLast(i);
    }
    System.out.println("TAS Lock with no contention:");
    TASTest(1);

    queue.clear();
    for (int i = 1; i <= 10; i++) {
      queue.addLast(i);
    }
    System.out.println("TAS Lock with low contention:");
    TASTest(3);

    queue.clear();
    for (int i = 1; i <= 70; i++) {
      queue.addLast(i);
    }
    System.out.println("TAS Lock with high contention:");
    TASTest(20);

    queue.clear();
    for (int i = 1; i <= 10; i++) {
      queue.addLast(i);
    }
    System.out.println("TTAS Lock with no contention:");
    TTASTest(1);

    queue.clear();
    for (int i = 1; i <= 10; i++) {
      queue.addLast(i);
    }
    System.out.println("TTAS Lock with low contention:");
    TTASTest(3);

    queue.clear();
    for (int i = 1; i <= 70; i++) {
      queue.addLast(i);
    }
    System.out.println("TTAS Lock with high contention:");
    TTASTest(20);
  }

  public static void ExponentialBackoffTest(int numThreads) {
    long startTime = System.currentTimeMillis();
    test1(new EbLock(), numThreads);
    long endTime = System.currentTimeMillis();
    long duration = endTime - startTime;
    System.out.println("Backoff Test completed in " + duration + " milliseconds.");
    //printQueue();

    startTime = System.currentTimeMillis();

    test2(new EbLock(), numThreads);

    endTime = System.currentTimeMillis();
    duration = endTime - startTime;
    System.out.println("Backoff Test2 completed in " + duration + " milliseconds.");
    System.out.println("Counter: " + counter);
  }

  public static void printQueue() {
    if (queue.isEmpty()) {
      System.out.println("Queue is empty.");
    } else {
      System.out.println("Queue contents: " + queue);
    }
  }

  public static void TASTest(int numThreads) {
    long startTime = System.currentTimeMillis();
    test1(new TASLock(), numThreads);
    long endTime = System.currentTimeMillis();
    long duration = endTime - startTime;
    System.out.println("TAS Test completed in " + duration + " milliseconds.");
    //printQueue();

    startTime = System.currentTimeMillis();

    test2(new TASLock(), numThreads);

    endTime = System.currentTimeMillis();
    duration = endTime - startTime;
    System.out.println("TAS Test2 completed in " + duration + " milliseconds.");
    System.out.println("Counter: " + counter);
  }

  public static void TTASTest(int numThreads) {
    long startTime = System.currentTimeMillis();

    test1(new TTASLock(), numThreads);

    long endTime = System.currentTimeMillis();
    long duration = endTime - startTime;
    System.out.println("TTAS Test1 completed in " + duration + " milliseconds.");
    //printQueue();

    startTime = System.currentTimeMillis();

    test2(new TTASLock(), numThreads);

    endTime = System.currentTimeMillis();
    duration = endTime - startTime;
    System.out.println("TTAS Test2 completed in " + duration + " milliseconds.");
    System.out.println("Counter: " + counter);
  }

  public static void test1(Lock lock, int numThreads) {
    long startTime = System.currentTimeMillis();

    ExecutorService executor = Executors.newFixedThreadPool(numThreads);
    Runnable task = () -> {
      String threadName = Thread.currentThread().getName();

      for (int j = 0; j < 3; j++) {
        lock.lock();
        try {
          Integer item = queue.pollFirst();
          if (item != null) {
            //System.out.println(threadName + " dequeued: " + item);
          }
        } finally {
          lock.unlock();
        }
      }
    };

    for (int i = 0; i < numThreads; i++) {
      executor.submit(task);
    }

    try {
      executor.shutdown();
      if (!executor.awaitTermination(1, TimeUnit.MINUTES)) {
        System.out.println("Timeout reached");
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public static void test2(Lock lock, int numThreads) {
    long startTime = System.currentTimeMillis();

    ExecutorService executor = Executors.newFixedThreadPool(numThreads);
    Runnable task = () -> {
      String threadName = Thread.currentThread().getName();

      for (int j = 0; j < 3; j++) {
        lock.lock();
        try {
          counter++;
          //System.out.println(counter);
        } finally {
          lock.unlock();
        }
      }
    };

    for (int i = 0; i < numThreads; i++) {
      executor.submit(task);
    }

    try {
      executor.shutdown();
      if (!executor.awaitTermination(1, TimeUnit.MINUTES)) {
        System.out.println("Timeout reached");
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
