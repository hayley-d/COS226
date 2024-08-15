import java.util.*;

public class Main {
    public static final String RED = "\u001B[31m";
    public static final String BLUE = "\u001B[32m";
    public static final String RESET = "\u001B[0m";

    public static void main(String[] args) {
        List<MethodCall> operations = Arrays.asList(
                new MethodCall("A", 1, "enq(x)"),
                new MethodCall("A", 2, "deq(z)"),
                new MethodCall("B", 2, "deq(x)"),
                new MethodCall("B", 1, "enq(z)"),
                new MethodCall("A", 3, "enq(v)"),
                new MethodCall("B", 3, "deq(v)")
        );

        List<List<MethodCall>> possibleOrders = ExecutionOrderChecker.findPossibleOrders(operations);

        
        for (List<MethodCall> order : possibleOrders) {
            System.out.println(order);
        }
        /*
         * Should print:
         * [(A,1,enq(x)), (B,1,deq(x)), (A,2,enq(y)), (B,2,deq(y))]
         * [(A,1,enq(x)), (A,2,enq(y)), (B,1,deq(x)), (B,2,deq(y))]
         */

        //Test independent items with 2 threads
        operations = Arrays.asList(
                new MethodCall("A", 1, "enq(x)"),
                new MethodCall("A", 2, "deq(x)"),
                new MethodCall("B", 2, "deq(z)"),
                new MethodCall("B", 1, "enq(z)"),
                new MethodCall("A", 3, "enq(v)"),
                new MethodCall("A", 5, "deq(v)"),
                new MethodCall("B", 3, "enq(l)"),
                new MethodCall("B", 4, "deq(l)")
        );

        possibleOrders = ExecutionOrderChecker.findPossibleOrders(operations);

        for(List<MethodCall> order : possibleOrders) {
            System.out.println(BLUE + order + RESET);
        }

        //Test interleaving 4 threads
        operations = Arrays.asList(
                new MethodCall("A", 1, "enq(x)"),
                new MethodCall("A", 2, "enq(y)"),
                new MethodCall("B", 1, "deq(x)"),
                new MethodCall("B", 2, "enq(z)"),
                new MethodCall("C", 1, "deq(y)"),
                new MethodCall("C", 2, "deq(z)"),
                new MethodCall("D", 1, "enq(a)"),
                new MethodCall("D", 2, "deq(a)")
        );

        possibleOrders = ExecutionOrderChecker.findPossibleOrders(operations);

        for(List<MethodCall> order : possibleOrders) {
            System.out.println(order);
        }
    }
}
