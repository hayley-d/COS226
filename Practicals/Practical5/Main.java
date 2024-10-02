import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

  static Deque<Integer> queue = new LinkedList<>();

  public static void main(String[] args) {

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
  }

  public static void ExponentialBackoffTest(int numThreads) {
    long startTime = System.currentTimeMillis();

    EbLock lock = new EbLock();
    ExecutorService executor = Executors.newFixedThreadPool(numThreads);
    Runnable task = () -> {
      String threadName = Thread.currentThread().getName();

      for (int j = 0; j < 3; j++) {
        lock.lock();
        try {
          //System.out.println(Thread.currentThread().getName() + " aquired the lock");
          Integer item = queue.pollFirst();
          if (item != null) {
            System.out.println(threadName + " dequeued: " + item);
          }
        } finally {
          lock.unlock();
          //System.out.println(Thread.currentThread().getName() + " released the lock");
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

    long endTime = System.currentTimeMillis();
    long duration = endTime - startTime;
    System.out.println("Backoff Test completed in " + duration + " milliseconds.");
    printQueue();
  }

  public static void printQueue() {
    if (queue.isEmpty()) {
      System.out.println("Queue is empty.");
    } else {
      System.out.println("Queue contents: " + queue);
    }
  }
}
