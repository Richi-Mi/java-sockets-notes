package org.example.hilos.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static final int PORT = 8000;

    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(PORT);
            System.out.println("Servidor Iniciado en el puerto: " + PORT);

            while (true) {
                Socket client = server.accept();
                System.out.println("Cliente conectado desde " + client.getInetAddress());

                new Manejador(client).start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
