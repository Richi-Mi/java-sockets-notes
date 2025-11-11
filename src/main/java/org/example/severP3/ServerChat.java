package org.example.severP3;

import org.example.severP3.data.AppDataSource;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;
import java.util.Enumeration;

public class ServerChat {

    private static final int PORT = 8080;
    private static final int PACKET_LENGTH = 65535;

    private static DatagramSocket serverSocket;

    public static String getHostIp() {
        Enumeration<NetworkInterface> interfaces = null;
        try {
            interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();
                if (iface.isLoopback() || !iface.isUp()) {
                    continue;
                }
                Enumeration<InetAddress> addresses = iface.getInetAddresses();
                while(addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    if (addr.getHostAddress().matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}"))
                        return addr.getHostAddress();
                }
            }
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        return "127.0.0.1";
    }

    public static void main(String[] args) {
        AppDataSource appDataSource = new AppDataSource();
        try {
            serverSocket = new DatagramSocket(PORT);
            serverSocket.setReuseAddress(true);

            System.out.println("Server started on: " + getHostIp() + ":" + serverSocket.getLocalPort());

            do {
                byte[] buffer = new byte[PACKET_LENGTH];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                serverSocket.receive(packet);

                byte[] received = Arrays.copyOfRange(packet.getData(), 0, packet.getLength());
                SocketAddress senderAddress = packet.getSocketAddress();

                new Manager(appDataSource, serverSocket, senderAddress, received).start();
            } while (true);
        } catch (SocketException e) {
            System.out.println("Error al intentar iniciar el servidor");
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            // Close the server if these are open.
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        }
    }
}
