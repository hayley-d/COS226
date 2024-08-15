import java.util.*;

public class Main {

    public static void main(String[] args) {
        List<MethodCall> operations = Arrays.asList(
                new MethodCall("A", 1, "enq(x)"),
                new MethodCall("A", 2, "deq(x)")
                /*new MethodCall("B", 2, "deq(y)"),
                new MethodCall("B", 1, "enq(y)"),
                new MethodCall("A", 3, "deq(v)"),
                new MethodCall("A", 4, "enq(v)")*/
                /*new MethodCall("B", 3, "enq(v)"),
                new MethodCall("B", 4, "deq(p)"),
                new MethodCall("C", 1, "enq(g)"),
                new MethodCall("C", 2, "deq(l)"),
                new MethodCall("D", 1, "enq(l)"),
                new MethodCall("D", 2, "deq(g)")*/
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
    }
}
