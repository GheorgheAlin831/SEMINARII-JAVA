gpackage org.example;

public class ThreadsDemo {

    public static void main(String[] args) {
        System.out.println("Main thread started: " + Thread.currentThread().getName());

        // Demo 1: Create a thread by extending Thread
        CountingThread threadA = new CountingThread("Thread-A");

        // Demo§ 2: Create a thread using Runnable
        Thread threadB = new Thread(new CountingRunnable("Runnable-B"));

//        threadA.start();
//        threadB.start();

        // Demo 3: Shared counter with synchronized method
        Counter sharedCounter = new Counter();

        Thread worker1 = new Thread(new CounterWorker(sharedCounter, "Worker-1"));
        Thread worker2 = new Thread(new CounterWorker(sharedCounter, "Worker-2"));
        Thread worker3 = new Thread(new CounterWorker(sharedCounter, "Worker-3"));

        worker1.start();
        worker2.start();
        worker3.start();

        try {
            // Demo 4: join() makes the main thread wait for other threads
            threadA.join();
            threadB.join();
            worker1.join();
            worker2.join();
            worker3.join();
        } catch (InterruptedException e) {
            System.out.println("Main thread was interrupted.");
            Thread.currentThread().interrupt();
        }

        System.out.println("Final counter value: " + sharedCounter.getValue());
        System.out.println("Main thread finished: " + Thread.currentThread().getName());
    }
}

// Demo 1: Creating a thread by extending Thread
class CountingThread extends Thread {

    public CountingThread(String name) {
        super(name);
    }

    @Override
    public void run() {
        for (int i = 1; i <= 5; i++) {
            System.out.println(getName() + " count: " + i);

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println(getName() + " was interrupted.");
                interrupt();
                return;
            }
        }
    }
}

// Demo 2: Creating a thread by implementing Runnable
class CountingRunnable implements Runnable {

    private final String name;

    public CountingRunnable(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        for (int i = 1; i <= 5; i++) {
            System.out.println(name + " count: " + i);

            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                System.out.println(name + " was interrupted.");
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}

// Demo 3: Shared data protected with synchronized
class Counter {

    private int value = 0;

    public synchronized void increment() {
        value++;
    }

    public synchronized int getValue() {
        return value;
    }
}

// Worker that increments the shared counter many times
class CounterWorker implements Runnable {

    private final Counter counter;
    private final String workerName;

    public CounterWorker(Counter counter, String workerName) {
        this.counter = counter;
        this.workerName = workerName;
    }

    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
            counter.increment();
        }

        System.out.println(workerName + " finished incrementing.");
    }
}

