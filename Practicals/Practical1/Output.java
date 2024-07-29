
import java.util.concurrent.ConcurrentLinkedQueue;

public class Output {
    private final ConcurrentLinkedQueue<String> lines;

    public Output(){
        this.lines=new ConcurrentLinkedQueue<>();
    }

    public ConcurrentLinkedQueue<String> getLines() {
        return lines;
    }

    public void println(String in){
        lines.add(in);
    }

    @Override
    public String toString(){
        String out="";
        for (String s : lines) {
            out+=s+"\n";
        }
        return out;
    }
}
