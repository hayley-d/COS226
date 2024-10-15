import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
/**
 * Worker thread class that executes tasks from the task queue.
 */
public class PoolThreadRunnable implements Runnable {

    private Thread thread = null;
    private BlockingQueue<Runnable> taskQueue = null;
    public final AtomicBoolean isStopped = new AtomicBoolean(false);


    public PoolThreadRunnable(BlockingQueue<Runnable> queue){
        this.taskQueue = queue;
    }

    @Override
    public void run(){
        this.thread = Thread.currentThread();
        while(!isStopped.get()){
            try{
                Runnable task = taskQueue.take();
                task.run();
            } catch(InterruptedException e){
                Thread.currentThread().interrupt();
            }
        }
    }

    public void stop(){
        isStopped.set(true); 
        this.thread.interrupt();
    }
}
