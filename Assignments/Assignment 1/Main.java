

public class Main {
    public static void main(String[] args) throws Exception {
        testCompare();                
        lessThanTest();
    }

    public static void testCompare(){
        int[] timestamp = {2,2,1};
        int [] timestamp2 = {2,0,2}; 
        
        BoundedTimestamp b1 = new BoundedTimestamp(timestamp);
        BoundedTimestamp b2 = new BoundedTimestamp(timestamp2);

        if(b1.compare(b2) == 1){
            System.out.println("Correct");
        } else{
            System.out.println("Failed test 1");
        }
        
        int [] t1 = {2,2,2,1};
        int [] t2 = {2,2,1,0};
        
        BoundedTimestamp b3 = new BoundedTimestamp(t1);
        BoundedTimestamp b4 = new BoundedTimestamp(t2);

        if(b3.compare(b4) == -1){
            System.out.println("Correct");
        } else{
            System.out.println("Failed test 2");
        }

        int [] t3 = {0,1,0};
        int [] t4 = {0,1,0};
        
        BoundedTimestamp b5 = new BoundedTimestamp(t3);
        BoundedTimestamp b6 = new BoundedTimestamp(t4);

        if(b5.compare(b6) == 0){
            System.out.println("Correct");
        } else{
            System.out.println("Failed test 2");
        }
        
        
    }

    public static void lessThanTest(){
        int [] t1t = {0,0,0};
        int [] t2t = {0,2,2};
        int [] t3t = {0,2,0};
        int [] t4t = {0,0,0};
        BoundedTimestamp b1 = new BoundedTimestamp(t1t); 
        BoundedTimestamp b3 = new BoundedTimestamp(t2t);
        BoundedTimestamp b4 = new BoundedTimestamp(t3t);
        //BoundedTimestamp b2 = new BoundedTimeStamp(t4t);
        BoundedTimestamp[] labels = {b1,b3,b4,b1};
        BoundedTimestamp timestamp = b1.getNext(labels,1);
        
//        if(timestamp.timestamp[0] == 2 && timestamp.timestamp[1] == 2 && timestamp.timestamp[2] == 1) System.out.println("Correct");
        System.out.println(timestamp);

        int [] t4 = {1};
        int [] t5 = {1};
        int [] t6 = {0};
        BoundedTimestamp b7 = new BoundedTimestamp(t4); 
        BoundedTimestamp b6 = new BoundedTimestamp(t5);
        BoundedTimestamp b5 = new BoundedTimestamp(t6);
        BoundedTimestamp[] labels2 = {b7,b6,b5};
        BoundedTimestamp timestamp2 = b7.getNext(labels2,0);

        System.out.println(timestamp2);


        int [] t8 = {2};
        int [] t9 = {1};
        BoundedTimestamp b8 = new BoundedTimestamp(t8); 
        BoundedTimestamp b9 = new BoundedTimestamp(t9);
        BoundedTimestamp[] labels3 = {b8,b9};
        BoundedTimestamp timestamp3 = b8.getNext(labels3,0);

        System.out.println(timestamp3);
 
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
