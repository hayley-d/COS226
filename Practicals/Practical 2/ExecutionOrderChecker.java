import java.util.*;

public class ExecutionOrderChecker {

    public static final String RED = "\u001B[31m";
    public static final String BLUE = "\u001B[34m";
    public static final String RESET = "\u001B[0m";

    // Returns a list of all possible orders in which the operations can be executed to satisfy the sequential consistency constraints
    public static List<List<MethodCall>> findPossibleOrders(List<MethodCall> operations)
    {
        List<List<MethodCall>> possibleOrders = new ArrayList<>();
        if(operations== null || operations.isEmpty()) return possibleOrders;
        //check if valid

        //some code here
        Vector<Vector<MethodCall>> groupedThreadCalls = new Vector<>();

        for(MethodCall operation : operations){
            boolean inVector = false;
            for(Vector<MethodCall> group : groupedThreadCalls){
                if(group.elementAt(0).orderInThread == operation.orderInThread){
                    inVector = true;
                    group.add(operation);
                    break;
                }
            }

            if(!inVector){
                Vector<MethodCall> newGroup = new Vector<>();
                newGroup.add(operation);
                groupedThreadCalls.add(newGroup);
            }
        }

        boolean continueCalculation = true;
        for(Vector<MethodCall> group : groupedThreadCalls){
            if(!isCorrectOrder(group)) continueCalculation = false;
        }

        if(continueCalculation){
            Vector<Vector<Vector<MethodCall>>> allPermutations = new Vector<>();

            for(Vector<MethodCall> group : groupedThreadCalls){
                //generate permutation
                Vector<Vector<MethodCall>> groupPermutations = new Vector<>();
                generatePermutation(group,group.size(),groupPermutations);
                allPermutations.add(groupPermutations);
            }

            Vector<Vector<MethodCall>> merged = mergePermutations(allPermutations);

            for(Vector<MethodCall> permutation : merged){

                if(isValidCallOrder(permutation)){
                    possibleOrders.add(permutation);
                }
            }
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

    public static Vector<Vector<MethodCall>> mergePermutations(Vector<Vector<Vector<MethodCall>>> groupedPermutations) {
        Vector<Vector<MethodCall>> result = new Vector<>();
        Vector<MethodCall> current = new Vector<>();

        generateMergePermutations(groupedPermutations, 0, current, result);

        return result;
    }

    private static void generateMergePermutations(Vector<Vector<Vector<MethodCall>>> groupedPermutations, int level, Vector<MethodCall> current, Vector<Vector<MethodCall>> result) {
        if (level == groupedPermutations.size()) {
            result.add(new Vector<>(current));
            return;
        }

        for (Vector<MethodCall> permutation : groupedPermutations.get(level)) {
            current.addAll(permutation);
            generateMergePermutations(groupedPermutations, level + 1, current, result);
            current.subList(current.size() - permutation.size(), current.size()).clear();
        }
    }

    private static void generatePermutation(Vector<MethodCall> list, int size, Vector<Vector<MethodCall>> result){
        if(size == 1){
            result.addElement(new Vector<>(list));
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

    private static boolean isValidOperations(Vector<MethodCall> threadCalls){
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

    private static boolean isValidOrder(Vector<MethodCall> threadCalls){
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

    private static boolean isValidCallOrder(Vector<MethodCall> calls){
        if(!isValidOrder(calls)) return false;
        if(!isValidOperations(calls)) return false;

        /*Map<String,Integer> map = new HashMap<>();
        for(MethodCall call : calls){
            int order = map.getOrDefault(call.threadId,-1);

            if(order == -1){
                map.put(call.threadId, call.orderInThread);
            }else{
                if(call.orderInThread <= order){
                    return false;
                }else{
                    map.put(call.threadId, call.orderInThread);
                }
            }
        }*/
         
        return true;
    }

    private static boolean isCorrectOrder(Vector<MethodCall> calls){
        for(int i = 0; i < calls.size(); ++i){
            String name = calls.elementAt(i).threadId;
            for(int j = i+1; j < calls.size(); ++j){
                if(calls.elementAt(j).threadId.equals(name)){
                    System.out.println(calls.elementAt(j));
                    return false;
                }
            }
        }
        return true;
    }

}

