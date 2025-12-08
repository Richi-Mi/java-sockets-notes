package org.example.severP3;

import org.example.severP3.data.AppDataSource;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;
import java.util.Enumeration;

//git reset HEAD~1

public class ServerChat {

    private static final int PORT = 8085;
    private static final int PACKET_LENGTH = 65535;

    private static MulticastSocket serverSocket;

    public static NetworkInterface getHostInterface() {
        Enumeration<NetworkInterface> interfaces = null;
        try {
            interfaces = NetworkInterface.getNetworkInterfaces();

            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();

                // Check the multicast desponibility.
                System.out.printf("Nombre de despliegue: %s\n", iface.getDisplayName());
                System.out.printf("Nombre: %s\n", iface.getName());
                String multicast = (iface.supportsMulticast()) ? "Soporta multicast": "No soporta multicast";
                System.out.println("Multicast: " + multicast);

                if (iface.isLoopback() || !iface.isUp()) {
                    continue;
                }
                Enumeration<InetAddress> addresses = iface.getInetAddresses();
                while(addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    if (addr.getHostAddress().matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")) {
                        System.out.println("My IP direction: " + addr.getHostAddress());
                        return iface;
                    }
                }
            }
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static void main(String[] args) {
        AppDataSource appDataSource = new AppDataSource();

        try {
            serverSocket = new MulticastSocket(PORT);
            serverSocket.setReuseAddress(true);
            serverSocket.setTimeToLive(255);

            System.out.println("Multicast Server started on:" + serverSocket.getLocalPort());
            String multicastDirection = "231.1.1.1";
            InetAddress group = InetAddress.getByName(multicastDirection);
            SocketAddress multicastAddress = new InetSocketAddress(group, PORT);
            NetworkInterface networkInterface = getHostInterface();

            serverSocket.joinGroup(multicastAddress, networkInterface);
            System.out.println("Multicast socket joinend to group: " + group);

            ReceiveManager r = new ReceiveManager(appDataSource, serverSocket);

            r.start();
            r.join();

        } catch (SocketException e) {
            System.out.println("Error al intentar iniciar el servidor");
            throw new RuntimeException(e);
        } catch (IOException e) {
            System.out.println("Error con IO");
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            System.out.println("Hilo interrumpido");
            throw new RuntimeException(e);
        } finally {
            // Close the server if these are open.
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        }
    }
}
