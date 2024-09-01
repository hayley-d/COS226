public class BoundedTimestamp {
    public final int[] timestamp;

    public BoundedTimestamp(int[] timestampArray) {
        //create a new timestamp with the same values as the input array
        this.timestamp = timestampArray;
    }

    public BoundedTimestamp(int n) {
        this.timestamp = new int[n];
        //explicitly set new timestamp to 0,0,0...
        for (int i = 0; i < n; i++) {
            timestamp[n] = 0;
        }
    }

    // Returns 1 if this comes before the other (i.e this is earlier), -1 if this comes after the other (i.e this is later), 0 if they are equal
    public int compare(BoundedTimestamp other) {

    }

    

    public static BoundedTimestamp getNext(BoundedTimestamp[] label,int indexAskingForLabel) {
        
    }

    //you may add varibles and methods as needed

    @Override
    public String toString() {
        return java.util.Arrays.toString(timestamp);
    }

}
