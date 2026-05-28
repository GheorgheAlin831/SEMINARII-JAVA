package org.example;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        CountingThread ct = new CountingThread("Thread A");

        Thread cr = new Thread(new CountingRunnable("Runnable-B"));
        ct.start();
        cr.start();

    }
}