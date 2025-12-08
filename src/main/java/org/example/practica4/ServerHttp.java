package org.example.practica4;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerHttp {

    private static final int PORT = 4000;

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            serverSocket.setReuseAddress(true);

            System.out.println("Server listening on port " + PORT);

            do {
                Socket socket = serverSocket.accept();
                ServerManager sm = new ServerManager(socket);

                sm.start();
                sm.join();
            } while(true);


        } catch (IOException e) {
            System.out.println("Error opening server socket");
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            System.out.println("Interrupted");
            throw new RuntimeException(e);
        }
    }
}
