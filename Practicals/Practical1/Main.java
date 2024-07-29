import java.util.LinkedList;
import java.util.Queue;

public class Main {
    public static void main(String[] args) throws Exception {
        Paper paper1 = new Paper();
        Paper paper2 = new Paper();
        Paper paper3 = new Paper();
        Paper paper4 = new Paper();
        Paper paper5 = new Paper();

        Queue<Paper> papers = new LinkedList<Paper>();
        papers.add(paper1);
        papers.add(paper2);
        papers.add(paper3);
        papers.add(paper4);
        papers.add(paper5);

        Output output = new Output();
        Threeterson lock = new Threeterson(output);

        Marker m1 = new Marker(lock,output,papers);
        m1.setName("Marker 1");
        Marker m2 = new Marker(lock,output,papers);
        m2.setName("Marker 2");
        Marker m3 = new Marker(lock,output,papers);
        m3.setName("Marker 3");

        m1.start();
        m2.start();
        m3.start();

       m1.join();
       m2.join();
       m3.join();



    }
}
