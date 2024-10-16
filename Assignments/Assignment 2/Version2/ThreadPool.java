import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
/**
 * @class ThreadPool Contains a list of worker threads to execute Runnable tasks from a task queue.
 */
class ThreadPool {
    /**
     * List of worker threads.
     */
    private final List<Worker> workers;
    /**
     * List of Runnable tasks for the workers to execute.
     */
    private final BlockingQueue<Runnable> taskQueue;
    /**
     * Boolean value indicating if the pool has terminated execution.
    */
    private AtomicBoolean isShutdown = new AtomicBoolean(false);
    private final ReentrantLock lock = new ReentrantLock();
    /**
     * Condition to signal all threads that execution has terminated.
     */
    private final Condition condition = lock.newCondition();
    private AtomicInteger activeTasks = new AtomicInteger(0);
    private AtomicInteger completedTasks = new AtomicInteger(0);

    /**
     * Public constructor for the thread pool.
     * @param numberOfThreads The number of threads the pool should manage.
     */
    public ThreadPool(int numberOfThreads) {
        workers = new LinkedList<>();
        taskQueue = new LinkedBlockingQueue<>();

        for (int i = 0; i < numberOfThreads; i++) {
            Worker worker = new Worker();
            workers.add(worker);
            worker.start();
        }
    }

    public void execute(Runnable task) {
        if (isShutdown.get()) {
            throw new IllegalStateException("ThreadPool is shut down");
        }
        activeTasks.getAndIncrement();
        
        taskQueue.offer(() -> {
            try {
                task.run();
            } finally {
                completeTask();
            }
        });

    }

    private void completeTask() {
        lock.lock();
        try {
            activeTasks.getAndDecrement();
            completedTasks.getAndIncrement();
            if (activeTasks.compareAndSet(0,0) && isShutdown.get()) {
                condition.signalAll(); 
            }
        } finally {
            lock.unlock();
        }
    }

    public void shutdown() {
        isShutdown.set(true); 
        for (Worker worker : workers) {
            worker.interrupt(); 
        }
    }

    public void slowShutdown() {
        isShutdown.set(true);
    }

    public void awaitTermination() throws InterruptedException {
        lock.lock();
        try {
            while (activeTasks.get() > 0) {
                condition.await(); 
            }
        } finally {
            lock.unlock();
        }
    }

    private class Worker extends Thread {
        @Override
        public void run() {
            while (!isShutdown.get()) {
                try {
                    Runnable task = taskQueue.take(); 
                    task.run();
                } catch (InterruptedException e) {
                    if (isShutdown.get()) {
                        break; 
                    }
                }
            }
        }
    }
}

