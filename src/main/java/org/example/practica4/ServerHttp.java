package org.example.practica4;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ServerHttp {

    private static final int PORT = 4000;

    // Pool size
    private static final int MAX_POOL_SIZE = 6;
    private static final int PORT_SECONDARY = 8081;

    private static int activeConnections = 0;

    private static boolean secondaryServerRunning = false;

    public static synchronized void incrementConnections() {
        activeConnections++;
        System.out.println(">>> Conexión entrante. Total activas: " + activeConnections);
    }

    public static synchronized void decrementConnections() {
        activeConnections--;
        System.out.println("<<< Conexión finalizada. Total activas: " + activeConnections);
    }

    public static synchronized int getConnections() {
        return activeConnections;
    }

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            serverSocket.setReuseAddress(true);

            System.out.println("Server listening on port " + PORT);

            do {
                Socket socket = serverSocket.accept();

                int currentLoad = getConnections();

                if (currentLoad >= (MAX_POOL_SIZE / 2)) {
                    System.out.println("Servidor sobrecargado (" + currentLoad + "). Redirigiendo...");

                    startSecondaryServerIfNotRunning();

                    // Redirect Client.
                    handleRedirection(socket, PORT_SECONDARY);

                } else {
                    incrementConnections();

                    ServerManager sm = new ServerManager(socket);
                    sm.start();
                }
            } while(true);


        } catch (IOException e) {
            System.out.println("Error opening server socket");
            throw new RuntimeException(e);
        }
    }
    private static void handleRedirection(Socket client, int targetPort) {
        try {
            Scanner tempScanner = new Scanner(client.getInputStream());

            String path = "/";

            if (tempScanner.hasNextLine()) {
                String requestLine = tempScanner.nextLine();
                String[] parts = requestLine.split(" ");
                if (parts.length > 1) {
                    path = parts[1];
                }
                System.out.println("-> Redirigiendo ruta: " + path);
            }

            PrintWriter out = new PrintWriter(client.getOutputStream(), false);

            out.print("HTTP/1.1 307 Temporary Redirect\r\n");
            out.print("Location: http://localhost:" + targetPort + path + "\r\n");
            out.print("Content-Length: 0\r\n");
            out.print("Connection: close\r\n");
            out.print("\r\n");
            out.flush();

            client.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void startSecondaryServerIfNotRunning() {
        synchronized (ServerHttp.class) {
            if (!secondaryServerRunning) {
                secondaryServerRunning = true;
                new Thread(() -> {
                    try (ServerSocket secSocket = new ServerSocket(PORT_SECONDARY)) {
                        System.out.println("\n SERVIDOR SECUNDARIO INICIADO EN PUERTO " + PORT_SECONDARY + " \n");
                        while (true) {
                            Socket secClient = secSocket.accept();
                            // The secondary server attends without limit
                            new ServerManager(secClient, true).start();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        }
    }
}
