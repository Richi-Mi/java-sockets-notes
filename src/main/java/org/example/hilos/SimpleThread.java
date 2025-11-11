package org.example.hilos;

public class SimpleThread extends Thread {
    int v1;
    String message;

    public SimpleThread(int v1, String message) {
        this.v1 = v1;
        this.message = message;
    }

    @Override
    public void run() {
        System.out.println("Hilo " + message + " iniciado");
        System.out.println("Nombre: " + getName() + " ID: " + getId() + " Prioridad: " + getPriority());

        System.out.println(this);
    }
}
