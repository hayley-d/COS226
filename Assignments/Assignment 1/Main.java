

public class Main {
    public static void main(String[] args) throws Exception {
        
    }

    // public static void testWithLock() throws InterruptedException {
    //     int threads = 4;
    //     Marker[] markers = new Marker[threads];
    //     Queue<Paper> allPapers = new LinkedList<>();
    //     System.out.println("###");
    //     for (int j = 0; j < 100; j++) {

    //         BackeryLock lock = new BackeryLock(threads);
    //         for (int i = 0; i < 50; i++) {
    //             allPapers.add(new Paper());
    //         }
    //         for (int i = 0; i < markers.length; i++) {
    //             markers[i] = new Marker(lock, allPapers);
    //         }
    //         for (int i = 0; i < markers.length; i++) {
    //             markers[i].start();
    //         }
    //         for (int i = 0; i < markers.length; i++) {
    //             markers[i].join();
    //         }
    //         System.out.println("Output " + j);
    //     }
    // }

}
