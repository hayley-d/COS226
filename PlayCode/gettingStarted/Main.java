public class Main {
    public static void main(String[] args) throws InterruptedException {

        //first way to create a thread -> make a subclass of the Thread class
        //Use this if you don't need to use inheritance since a class can only have one parent class
        MyThread thread1 = new MyThread();

        //other way to create a thread -> use a class implementing the Runnable interface and pass into the Thread class
        //Use this method if you want to be able to use inheritance in your class
        MyRunnable runnable1 = new MyRunnable();
        Thread thread2 = new Thread(runnable1);

        thread1.setDaemon(true); // this sets it to a daemon thread so it is no longer a user thread
        //JVM does not wait for daemon threads to complete before exiting the program.
        //as long as there is a user thread JVM will wait until last user thread dies.

        thread1.start();
        //thread1.join(); // calling thread (main) will wait until the given thread dies or until specified miliseconds is done
        thread2.start();
    }
}
