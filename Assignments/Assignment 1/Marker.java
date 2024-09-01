
import java.util.Queue;
import java.util.concurrent.locks.Lock;

public class Marker extends Thread {
    private final Lock lock;
    private final Queue<Paper> allPapers;

    public Marker(Lock lock, Queue<Paper> allPapers) {
        this.lock = lock;
        this.allPapers=allPapers;
    }

    @Override
    public void run() {
        Paper toMark=null;
        while(!allPapers.isEmpty()){
        lock.lock();
        try {
            toMark=allPapers.poll();
        }finally {
            lock.unlock();
        }
        if(toMark!=null){
            toMark.mark();
        }
        
    }

    }

}
