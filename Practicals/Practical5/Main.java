import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

  static ConcurrentLinkedQueue<Integer> queue = new ConcurrentLinkedQueue<>();

  public static void main(String[] args) {

    for (int i = 1; i <= 30; i++) {
      queue.add(i);
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
          Integer item = queue.poll();
          if (item != null) {
            System.out.println(threadName + " dequeued: " + item);
            Integer lastItem = getLastItem(queue) + 1;            
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
