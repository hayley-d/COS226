public class Main {
    public static void main(String[] args){

        //first way to create a thread -> make a subclass of the Thread class
        //Use this if you don't need to use inheritance since a class can only have one parent class
        MyThread thread1 = new MyThread();

        //other way to create a thread -> use a class implementing the Runnable interface and pass into the Thread class
        //Use this method if you want to be able to use inheritance in your class
        MyRunnable runnable1 = new MyRunnable();
        Thread thread2 = new Thread(runnable1);

        thread1.start();
        thread2.start();
    }
}
