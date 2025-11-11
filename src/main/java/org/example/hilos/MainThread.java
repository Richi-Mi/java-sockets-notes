package org.example.hilos;

public class MainThread {
    public static void main(String[] args) throws InterruptedException {
        SimpleThread h1 = new SimpleThread(20, "20");
        Thread t1 = new Thread( new RunThread("1"));

        h1.start();
        t1.start();

        h1.join();
        t1.join(); // Espera a qué el hilo termine su ejecución
    }
}
