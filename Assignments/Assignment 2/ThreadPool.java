import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
private final ReentrantLock lock = new ReentrantLock();
private final Condition queueEmpty = lock.newCondition();
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Custom thread pool implementation that manages a fixed number of threads and a queue of tasks to be executed.
 * Tasks are executed by worker threads from the task queue.
 */
public class ThreadPool {
    /**
     * Queue for the tasks that need to be executed
     * A blocking queue is used to safely manage concurrent access by multiple threads.
     */
    private BlockingQueue<Runnable> taskQueue;
    /**
     * List of all the tasks submitted
     */
    private List<PoolThreadRunnable> tasks = new ArrayList<>(); 
    /**
     * Boolean value indicating if the thread pool has terminated
     */
    private boolean terminated = false;
    /**
     * ReentrantLock used to manage thread access to shared resources.
     */
    private final ReentrantLock lock = new ReentrantLock();
    /**
     * Latch for waiting on tasks to finish.
     */
    private CountDownLatch latch;
    /**
     * Counter for completed tasks.
     */
    private final AtomicInteger completedTaskCount = new AtomicInteger(0);

    /**
     * Constructor for the thread pool.
     * Creates a PoolThreadRunnable for each thread and adds it to the tasks list.
     * Iterates through the tasks list and creates a new thread for each task.
     * @param numThreads The number of threads to add to the thread pool.
     * @param maxNumTasks The maximum number of tasks submitted for the threads to execute.
     */
    public ThreadPool(int numThreads, int maxNumTasks) {
        taskQueue = new ArrayBlockingQueue(maxNumTasks);
        latch = new CountDownLatch(maxNumTasks);

        for(int i = 0; i < numThreads; i++) {
            PoolThreadRunnable pool_thread_runnable = new PoolThreadRunnable(taskQueue);
            tasks.add(pool_thread_runnable);
            new Thread(pool_thread_runnable).start();
        }
    }

    /**
     * Submits a new task to the threadPool for execution.
     * The task is added to the queue and the worker threads will execute it.
     *
     * @param task The Runnable task to be executed.
     * @throws IllegalStateException If the threadPool has been terminated.
     */
    public void execute(Runnable task) throws Exception{
        lock.lock();
        try {
            if(terminated) throw new IllegalStateException("ThreadPool has termintated.");
            taskQueue.offer(() -> {
                try {
                    task.run();
                } finally {
                    completedTaskCount.incrementAndGet();
                    latch.countDown(); 
                }
            });
        } finally {
            lock.unlock();
        }
        
    }
    
    /**
     * Waits for all tasks in the queue to finish execution.
     * This met
     */
    public void waitUntilFinished() {
        lock.lock();
        try {
              latch.await();  // Wait for all tasks to complete
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }

    public int getActiveThreads() {
        return (int) tasks.stream().filter(t -> !t.isStopped.get()).count();
    }

    public int getQueueSize() {
        return taskQueue.size();
    }

    public int getCompletedTasks() {
        return completedTaskCount.get();  
    }

    /**
     * Prevents tasks from taking too long to execute by wrapping the Runnable in a timed execution block.
     */
    public void executeWithTimeout(Runnable task, long timeout, TimeUnit unit) throws Exception {
        Future<?> future = submit(() -> {
            task.run();
            return null;
        });

        try {
            future.get(timeout, unit);
        } catch (TimeoutException e) {
            future.cancel(true);  
            throw new Exception("Task execution exceeded the timeout.");
        }
    }



    /**
     * Shuts down the thread pool.
     * This method sets the terminated flag and stops all worker threads from executing further tasks.
     */
    public void shutdown() {
        lock.lock();
        try {
            terminated = true; 
            for (PoolThreadRunnable task : tasks) {
                task.stop();  
            }
        } finally {
            lock.unlock();
        }
    }

    public void shutdownNow() {
        lock.lock();
        try {
            for (PoolThreadRunnable task : tasks) {
                task.stop();
                Thread.currentThread().interrupt();
            }
        } finally {
            lock.unlock();
        }
    }
}
