public class BoundedTimestamp {
    public final int[] timestamp;
    private final int size;

    public BoundedTimestamp(int[] timestampArray) {
        //create a new timestamp with the same values as the input array
        this.timestamp = timestampArray;
        this.size = timestampArray.length;
    }

    public BoundedTimestamp(int n) {
        this.timestamp = new int[n];
        this.size = n;
        //explicitly set new timestamp to 0,0,0...
        for (int i = 0; i < n; i++) {
            timestamp[n] = 0;
        }
    }

    // Returns 1 if this comes before the other (i.e this is earlier), -1 if this comes after the other (i.e this is later), 0 if they are equal
    public int compare(BoundedTimestamp other) {

        boolean eql = true;
        //check if equal
        for(int i = 0; i < this.size;++i){
            if(this.timestamp[i] != other.timestamp[i]){
                eql = false;
            }
        }
        
        if(eql) return 0;
        
        return compareRec(0,other);
    }

    private int compareRec(int index, BoundedTimestamp other){
        if(index >= size) return 0; 
        
        if(this.timestamp[index] == other.timestamp[index]) return compareRec(++index,other);
        if(this.timestamp[index] == 1){
            if(other.timestamp[index] > 1) return 1;
            if(other.timestamp[index] < 1) return -1;
        } else if(this.timestamp[index] == 2){
            if(other.timestamp[index] == 0) return 1;
            if(other.timestamp[index] == 1) return -1;
        } else {
            if(other.timestamp[index] == 1) return 1;
            if(other.timestamp[index] == 2) return -1;
        }
        return compareRec(++index,other);
    }


    

    public static BoundedTimestamp getNext(BoundedTimestamp[] label,int indexAskingForLabel) {
        //find the largest timestamp in the array
        int[] largest = label[0].timestamp;
        int largestIndex = 0;
        for(int i = 1; i < label.length; ++i){
            if(i == indexAskingForLabel) continue;
            if(isLess(label[i].timestamp,largest)){
            } else {
                largestIndex = i;
                largest = label[i].timestamp;
            }
        }

        if(largest.length == 1){
            switch(largest[0]){
                case 0:
                    int[] arr = {1};
                    return new BoundedTimestamp(arr);
                case 1:
                    int [] arr2 = {2};
                    return new BoundedTimestamp(arr2);
                case 2:
                    int[] arr3 = {0};
                    return new BoundedTimestamp(arr3);
            }
        }
        
        //if all 2s roll over
        boolean all2s = false;
        for(int i = 0; i < largest.length;++i){
            if(largest[i] != 2){
                all2s = false;
                break;
            }
        }
        if(all2s) return new BoundedTimestamp(largest.length);
        
        boolean sameSubGraph = false;
        int len = largest.length;
        for(int i = 0; i < label.length; ++i){
            if(i == indexAskingForLabel || i == largestIndex) continue;
            if(label[i].timestamp[len-2] == largest[len-2]){
                sameSubGraph = true;
            }
        }
        
        if(sameSubGraph){
            //can return new label in "larger: sub graph
            int newSub = largest[len-2] == 2 ? 0 : largest[len-2]+1;
            int [] newLocation = new int[len];
            for(int i = 0; i < len; ++i){
                if(i < len-2){
                    newLocation[i] = largest[i];
                } else{
                    newLocation[len-2] = newSub;
                    newLocation[len-1] = 0;
                    break;
                }
            }
            return new BoundedTimestamp(newLocation);
        }
        else{
            //can return new label in the same subgraph
            int [] newLocation = new int[len];
            for(int i = 0; i < len; ++i){
                if(i < len-1){
                    newLocation[i] = largest[i];
                } else{
                    newLocation[len-1] = largest[len-1] == 2 ? 0 : largest[len-1]+1;
                    break;
                }
            }
            return new BoundedTimestamp(newLocation);
        }
    }

 
    private static boolean isLess(int[] a1, int[] a2){
        for(int i = 0; i < a1.length; ++i){
            if(a1[i] < a2[i]) return true;
            if(a2[i] < a1[i]) return false;
        }
        return false;
    }
    //you may add varibles and methods as needed

    @Override
    public String toString() {
        return java.util.Arrays.toString(timestamp);
    }

}
