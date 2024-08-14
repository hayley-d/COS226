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

        Vector<Vector<MethodCall>> permutations = new Vector<>();

        if(!groupedThreadCalls.isEmpty()){
            generatePermutation(groupedThreadCalls.getFirst(),groupedThreadCalls.getFirst().size(),permutations);

            /*System.out.println(permutations);
            for(Vector<MethodCall> permutation : permutations){

                permutation.addAll(groupedThreadCalls.get(1));
                System.out.println(permutation);
                System.out.println(isValidOrder(permutation));
                for(MethodCall operation : permutation){
                    System.out.println(extractValue(operation));
                }
            }*/

            /*for(Vector<MethodCall> permutation : permutations){
                Map<String,Integer> my_map = new HashMap<>();
                int index = 0;
                for(MethodCall operation : permutation){
                    my_map.put(operation.threadId,index);
                    ++index;
                }
                //System.out.println(map);
                Vector<MethodCall> other = new Vector<>();
                int groupNumber = 1;
                for(int i = 1; i < groupedThreadCalls.size(); i++){
                    Vector<MethodCall> group = groupedThreadCalls.get(i);
                    if(group!= null){
                        Vector<MethodCall> sortedGroup = new Vector<>(group);
                        sortedGroup.sort(Comparator.comparingInt(p -> my_map.get(p.threadId)));

                        other.addAll(sortedGroup.reversed());
                    }
                }
                Vector<MethodCall> combined = new Vector<>(permutation);
                combined.addAll(other);

                System.out.println(combined);
            }*/
            /*for(MethodCall firstCall : groupedThreadCalls.getFirst()){
                int totalThreads = groupedThreadCalls.getFirst().size();
                int numberOfLists = factorial(totalThreads-1);

                Vector<Vector<MethodCall>> currentPermutation = new Vector<>();

            }*/
        }



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

}

