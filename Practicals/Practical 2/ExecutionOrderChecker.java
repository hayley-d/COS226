import java.util.*;

public class ExecutionOrderChecker {

    public static final String RED = "\u001B[31m";
    public static final String BLUE = "\u001B[34m";
    public static final String RESET = "\u001B[0m";

    public static final List<List<MethodCall>> possibleOrders = new ArrayList<>();

    // Returns a list of all possible orders in which the operations can be executed to satisfy the sequential consistency constraints
    public static List<List<MethodCall>> findPossibleOrders(List<MethodCall> operations)
    {
        //clear the list - just in case
        possibleOrders.clear();
        
        //check if the input list is empty/null
        if(operations== null || operations.isEmpty()) return possibleOrders;
    
        //create list to hold permutations 
        ArrayList<ArrayList<MethodCall>> groupedThreadCalls = new ArrayList<>();
        generatePermutation(new ArrayList<>(operations), operations.size(), groupedThreadCalls);

        //for each permutation check if it is valid
        for(ArrayList<MethodCall> threadCalls : groupedThreadCalls){
            if(checkOrder(threadCalls) && isValidOperations(threadCalls)){
                possibleOrders.add(new ArrayList<MethodCall>(threadCalls));
            }
        }

        return possibleOrders;
    }

    private static void generateMergePermutations(ArrayList<ArrayList<ArrayList<MethodCall>>> groupedPermutations, int level, ArrayList<MethodCall> current) {
        if (level == groupedPermutations.size()) {
            process(current);
            return;
        }

        for (ArrayList<MethodCall> permutation : groupedPermutations.get(level)) {
            current.addAll(permutation);
            generateMergePermutations(groupedPermutations, level + 1, current);
            current.subList(current.size() - permutation.size(), current.size()).clear();
        }
    }

    private static void process(ArrayList<MethodCall> permutation){
        //System.out.println(permutation);
       if(/*isValidOrder(permutation) &&*/ isValidOperations(permutation)) {
           possibleOrders.add(new ArrayList<MethodCall>(permutation));
       }
    }

    private static void generatePermutation(ArrayList<MethodCall> list, int size, ArrayList<ArrayList<MethodCall>> result){
        if(size == 1){
            if(checkOrder(list) && isValidOperations(list)){
              result.add(new ArrayList<>(list));
            }
        } else{
           int index = 0;
           while(index < size){
               generatePermutation(list, size - 1, result);
               if(size % 2 != 0){
                   Collections.swap(list, 0, size-1);
               }else{
                   Collections.swap(list, index, size-1);
               }
               ++index;
           }
        }
    }

    private static boolean isValidOperations(ArrayList<MethodCall> threadCalls){
        Queue<String> queue = new LinkedList<>();

        for(MethodCall call : threadCalls){
            if(call.action.startsWith("enq")){
                if(queue.contains(extractValue(call))){
                    return false;
                }
                queue.add(extractValue(call));
            }else{
                //check if char is on the queue
                if(queue.peek()!=null && queue.peek().equals(extractValue(call))){
                    //valid
                    queue.remove(extractValue(call));
                }else{
                   //System.out.println(BLUE + threadCalls + RESET);
                    return false;
                }
            }
        }
        return true;
    }

    private static String extractValue(MethodCall call){
        String action = call.action;
        int start = action.indexOf('(');
        int end = action.indexOf(')');
        if(start != -1 && end != -1){
            if(start < end){
                return action.substring(start + 1, end);
            }
        }
        return "";
    }

    private static boolean isValidOrder(ArrayList<MethodCall> threadCalls){
        Stack<String> stack = new Stack<>();
        for(MethodCall call : threadCalls){
            if(stack.isEmpty() || !stack.contains(call.threadId)){
               stack.push(call.threadId);
            }
            else{
                if(stack.contains(call.threadId)) {
                    //try pop
                    if(stack.peek().equals(call.threadId)){
                        //at the top
                        stack.pop();
                    }else{
                        //System.out.println(RED + threadCalls +RESET);
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /*
    * Checks to see that no duplicate orders are made
    * */
    private static boolean isCorrectOrder(ArrayList<MethodCall> calls){
        for(int i = 0; i < calls.size(); ++i){
            for(int j = i+1; j < calls.size(); ++j){
                if(calls.get(j).threadId.equals(calls.get(i).threadId)){
                    //System.out.println(calls.get(j));
                    return false;
                }
            }
        }
        return true;
    }

    /*
    *checks that the order of calls for a given thread is correct
    *
    * */
    private static boolean checkOrder(ArrayList<MethodCall> calls){
        //first check ascending order
        for(int i = 0; i < calls.size(); ++i){
            int order = calls.get(i).orderInThread;
            for(int j = i+1; j < calls.size(); ++j){
                if(calls.get(j).threadId.equals(calls.get(i).threadId)){
                    ++order;
                   if(calls.get(j).orderInThread <= calls.get(i).orderInThread){
                       return false;
                   }
                   if(calls.get(j).orderInThread != order){
                       return false;
                   }
                }
            }
        }
        //check strict n+1 order

        return true;
    }
}

