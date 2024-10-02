import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

  static Deque<Integer> queue = new LinkedList<>();

  public static void main(String[] args) {

    for (int i = 1; i <= 30; i++) {
      queue.addLast(i);
    }

    ExponentialBackoffTest();
  }

  public static void ExponentialBackoffTest() {
    EbLock lock = new EbLock();
    ExecutorService executor = Executors.newFixedThreadPool(3);
    Runnable task = () -> {
      String threadName = Thread.currentThread().getName();

      for (int j = 0; j < 3; j++) {
        lock.lock();
        try {
          Integer item = queue.pollFirst();
          if (item != null) {
            System.out.println(threadName + " dequeued: " + item);
            Integer lastItem = queue.peekLast() + 1;            
            queue.add(lastItem);
            System.out.println(Thread.currentThread().getName() + " enqueued: " + item);
          }
        } finally {
          lock.unlock();
          System.out.println(threadName + " has released the lock");
        }
      }
    };

    for (int i = 0; i < 3; i++) {
      executor.submit(task);
    }

    //executor.awaitTermination();
    executor.shutdown();
  }
}
