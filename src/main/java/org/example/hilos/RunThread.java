package org.example.hilos;

public class RunThread implements Runnable {
    public String message;

    public RunThread(String message) {
        this.message = message;
    }

    @Override
    public void run() {
        System.out.println("Thread " + message + " iniciado");
        System.out.println("Nombre: " + Thread.currentThread().getName() + " ID: " + Thread.currentThread().getId() + " Prioridad: " + Thread.currentThread().getPriority() + " group" + Thread.currentThread().getThreadGroup().getName());
    }
}
