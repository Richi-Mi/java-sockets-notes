package org.example.hilos.socket;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

// Sincronización de hilos
// Cuando 2 o mas hilos buscan acceder a un recurso a la vez

// Sincronización de bloque
// Synchronized(Object o) {
// Modificar el objeto de forma sincronizada
// Secuencial sobre la variable d
// }

// Sincronización a nivel metodo
// MetodoSinc.java

// Sincronización a nivel variable
// MetodoSinc.java
// Volatible garantiza qué al leer la variable esta no sea modificada, pero si permite qué
// otros hilos acceden de manera concurrente a ella. 

public class Manejador extends Thread {

    // TODO: Investigar la cantidad de descriptores de entrada-salida qué soporta un proceso.

    private Socket socket;

    public Manejador(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            Scanner scan    = new Scanner(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
