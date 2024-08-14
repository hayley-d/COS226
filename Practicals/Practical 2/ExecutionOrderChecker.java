import java.util.*;

public class ExecutionOrderChecker {

    // Returns a list of all possible orders in which the operations can be executed to satisfy the sequential consistency constraints
    public static List<List<MethodCall>> findPossibleOrders(List<MethodCall> operations)
    {

        //check if valid

        //some code here
        Vector<Vector<MethodCall>> groupedThreadCalls = new Vector<>();

        for(MethodCall operation : operations){
            boolean inVector = false;
            for(Vector<MethodCall> group : groupedThreadCalls){
                if(group.getFirst().orderInThread == operation.orderInThread){
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
        Vector<MethodCall> ops = new Vector<>(operations);
        Vector<Vector<MethodCall>> groupPermutations = new Vector<>();
        generatePermutation(ops,ops.size(),groupPermutations);
        //List<List<MethodCall>> groupPermutationsList = new ArrayList<>();

        for(Vector<MethodCall> permutation : groupPermutations){
            if(isValidCallOrder(permutation) && isValidOrder(permutation) && isValidOperations(permutation)){
                System.out.println(permutation);
            }
        }


        /*if(!groupedThreadCalls.isEmpty()){
            for(Vector<MethodCall> group : groupedThreadCalls){
                Vector<Vector<MethodCall>> permutations = new Vector<>();
                generatePermutation(group,group.size(),permutations);
                groupPermutations.add(permutations);
            }

            for(Vector<MethodCall> permutation : groupPermutations.get(1)){
                for(int i = 1; i < groupPermutations.size();++i){
                    for(int j = 0; j < groupPermutations.get(i).size();++j){

                    }
                }
            }
            for(int i = 1; i < groupPermutations.size(); ++i){

                for()
            }


        }*/



        //System.out.println(groupedThreadCalls.toString());
        return null;
    }

    private static int factorial(int n){
        int result = 1;
        for(int i = 2; i <= n; i++){
            result *= i;
        }
        return result;
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
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private static boolean isValidCallOrder(Vector<MethodCall> calls){

         
        return true;
    }

}

