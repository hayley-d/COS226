
import java.util.Queue;
import java.util.concurrent.locks.Lock;

public class Marker extends Thread {
    private final Lock lock;
    private final Output output;
    private final Queue<Paper> allPapers;

    public Marker(Threeterson lock, Output output, Queue<Paper> allPapers) {
        this.lock = lock;
        this.output = output;
        this.allPapers = allPapers;
    }

    @Override
    public void run() {
        //pop paper off the queue
        //mark paper paper.mark()
        //continue while there are papers on the queue
        boolean flag = true;
        while (flag) {
            lock.lock();
            try {
                if (!allPapers.isEmpty()) {
                    Paper paper = allPapers.poll();
                    if (paper != null) {
                        paper.mark();
                    }
                } else {
                    flag = false;
                }
            } finally {
                lock.unlock();
                try {
                    Thread.sleep(2);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }

    }

}
