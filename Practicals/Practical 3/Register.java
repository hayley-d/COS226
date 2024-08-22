import java.util.*;

public class Register {
    List<RegisterOperation> operations = new ArrayList<>();
    private List<RegisterOperation> reads = new ArrayList<>();
    private List<RegisterOperation> writes = new ArrayList<>();

    void write(int value, int startTime, int endTime) {
        operations.add(new RegisterOperation(RegisterOperation.Type.WRITE, value, startTime, endTime));
    }

    void read(int startTime, int endTime) {
        operations.add(new RegisterOperation(RegisterOperation.Type.READ, null, startTime, endTime));
    }

    List<List<Integer>> getValidReadValues() {
        reads.clear();
        writes.clear();
        List<List<Integer>> results = new ArrayList<>();
        List<List<Integer>> possibleValues = new ArrayList<>();

        if(!isOverlaps()){

            if(writes.size() != 0 && reads.size() != 0){
                int lastWriteEnd = writes.get(writes.size()-1).endTime;
                int firstWriteStart = writes.get(0).startTime;
                int firstWriteEnd = writes.get(0).endTime;

                for(int i = 0; i < reads.size();++i){
                    possibleValues.add(new ArrayList<Integer>());
                    int start = reads.get(i).startTime;
                    int end = reads.get(i).endTime;
                    if(start < firstWriteEnd){
                        possibleValues.get(i).add(null);
                    }
                    if(end < firstWriteStart){
                        //start before any writes
                        possibleValues.get(i).add(null);
                    }
                    else if(start < lastWriteEnd){

                        RegisterOperation lastWrite = null;
                        for(RegisterOperation write : writes){
                           if(write.endTime <= start){
                               lastWrite = write;
                           }
                        }
                        if(lastWrite != null)
                           possibleValues.get(i).add(lastWrite.value);
 
                        for(int j = 0; j < writes.size(); ++j){
                            int writeStart = writes.get(j).startTime;
                            int writeEnd = writes.get(j).endTime;
                            if((start <= writeStart && end <= writeEnd && end > writeStart) || (start <= writeStart && end > writeEnd) ||(start >= writeStart && end <= writeEnd) || (start >= writeStart && end > writeEnd && start < writeEnd)){
                                //create possible value
                                //if(writeStart > end) continue;
                                possibleValues.get(i).add(writes.get(j).value);
                            } 
                        }
                        
                        if(possibleValues.get(i).size() == 0){
                          //no overlap
                       }
                    } else{
                        //starts after all writes have finished
                        possibleValues.get(i).add(writes.get(writes.size()-1).value);
                    }
//                    System.out.println(possibleValues.get(i));
                }

                generatePermutations(possibleValues,results);
          } else if(writes.size() == 0 && reads.size() != 0){
                //no writes
                for(int i = 0; i < reads.size(); ++i){
                    possibleValues.add(new ArrayList<Integer>());
                    possibleValues.get(i).add(null);
                }
                generatePermutations(possibleValues,results);
          }
        }

        return results;
    }


    /*
     * The following should seporate the list into reads and writes and check for overlaps
     */
    private boolean isOverlaps(){
        
        for(RegisterOperation o : operations){
            if(o.type == RegisterOperation.Type.READ){
                reads.add(o);
            } else{
                writes.add(o);
            }
        }
        
        for(int i = 0; i < reads.size()-1; ++i){
            int start = reads.get(i).startTime;
            int end = reads.get(i).endTime;
            for(int j = i+1; j < reads.size(); ++j){
                if(reads.get(j).startTime == start || reads.get(j).startTime < end){
                    return true;
                }
            }
        } 

        for(int i = 0; i < writes.size()-1; ++i){
            int start = writes.get(i).startTime;
            int end = writes.get(i).endTime;
            for(int j = i+1; j < writes.size(); ++j){
                if(writes.get(j).startTime == start || writes.get(j).startTime < end){
                    return true;
                }
            }
        }
        return false;
    }


    private void generatePermutations(List<List<Integer>> possibleValues, List<List<Integer>> results){
        if(possibleValues.size() != 0 && possibleValues != null){
            generatePermutation(possibleValues,results,new ArrayList<>(),0);
        }
        return;
    }
    
    private void generatePermutation(List<List<Integer>> possibleValues, List<List<Integer>> results, List<Integer> currentList, int depth){
        if(depth == possibleValues.size()){
            results.add(new ArrayList<>(currentList));
            return;
        }
        
        for(Integer value : possibleValues.get(depth)){
            currentList.add(value);
            generatePermutation(possibleValues,results,currentList,depth+1);
            currentList.remove(currentList.size()-1);
        }
    }
}
