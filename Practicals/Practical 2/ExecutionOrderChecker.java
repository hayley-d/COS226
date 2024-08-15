import java.util.*;

public class ExecutionOrderChecker {

    public static final String RED = "\u001B[31m";
    public static final String BLUE = "\u001B[34m";
    public static final String RESET = "\u001B[0m";

    public static final List<List<MethodCall>> possibleOrders = new ArrayList<>();

    // Returns a list of all possible orders in which the operations can be executed to satisfy the sequential consistency constraints
    public static List<List<MethodCall>> findPossibleOrders(List<MethodCall> operations)
    {
        possibleOrders.clear();
        if(operations== null || operations.isEmpty()) return possibleOrders;
        //check if valid

        //some code here
        ArrayList<ArrayList<MethodCall>> groupedThreadCalls = new ArrayList<>();

        for(MethodCall operation : operations){
            boolean inVector = false;
            for(ArrayList<MethodCall> group : groupedThreadCalls){
                if(group.get(0).orderInThread == operation.orderInThread){
                    inVector = true;
                    group.add(operation);
                    break;
                }
            }

            if(!inVector){
                /*ArrayList<MethodCall> newGroup = new Vector<>();
                newGroup.add(operation);
                groupedThreadCalls.add(newGroup);*/
                groupedThreadCalls.add(new ArrayList<>(Collections.singletonList(operation)));
            }
        }

        boolean continueCalculation = true;
        for(ArrayList<MethodCall> group : groupedThreadCalls){
            if(!isCorrectOrder(group)) continueCalculation = false;
        }

        if(continueCalculation){
            ArrayList<ArrayList<ArrayList<MethodCall>>> allPermutations = new ArrayList<>();

            for(ArrayList<MethodCall> group : groupedThreadCalls){
                //generate permutation
                ArrayList<ArrayList<MethodCall>> groupPermutations = new ArrayList<>();
                generatePermutation(group,group.size(),groupPermutations);
                allPermutations.add(groupPermutations);
            }

            //mergePermutations(allPermutations);
            generateMergePermutations(allPermutations, 0, new ArrayList<MethodCall>());

            /*for(Vector<MethodCall> permutation : merged){

                if(isValidCallOrder(permutation)){
                    possibleOrders.add(permutation);
                }
            }*/
        }



        return possibleOrders;
    }

    /*private static int factorial(int n){
        int result = 1;
        for(int i = 2; i <= n; i++){
            result *= i;
        }
        return result;
    }*/

    /*public static void mergePermutations(Vector<Vector<Vector<MethodCall>>> groupedPermutations) {
        Vector<MethodCall> current = new Vector<>();
        generateMergePermutations(groupedPermutations, 0, current);
        //System.out.println(RED+ possibleOrders+ RESET);
    }*/

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
       if(isValidOrder(permutation) && isValidOperations(permutation)) {
           possibleOrders.add(new ArrayList<MethodCall>(permutation));
       }
    }

    private static void generatePermutation(ArrayList<MethodCall> list, int size, ArrayList<ArrayList<MethodCall>> result){
        if(size == 1){
            result.add(new ArrayList<>(list));
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
            if(isEnqueue(call)){
                queue.add(extractValue(call));
            }else{
                //check if char is on the queue
                if(queue.peek()!=null && queue.peek().equals(extractValue(call))){
                    //valid
                    queue.poll();
                }else{
                   //System.out.println(BLUE + threadCalls + RESET);
                    return false;
                }
            }
        }
        return true;
    }



    private static boolean isEnqueue(MethodCall call){
        return call.action.startsWith("enq");
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

}

