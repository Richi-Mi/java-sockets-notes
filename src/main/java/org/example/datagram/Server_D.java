package org.example.datagram;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class Server_D {
    public static void main(String[] args) {

        try {
            DatagramSocket ds = new DatagramSocket(8080);
            System.out.println("Servidor iniciado... escuchando datagramas");

            // InetSocketAddress l = new InetSocketAddress("200.1.1.1", 8080); Con ip personalizado
            // ds.bind(l);

            while (true) {
                DatagramPacket p = new DatagramPacket(new byte[1024], 1024);
                ds.receive(p);

                String nombre = new String(p.getData(), 0, p.getLength());
                String saludo = "hola " + nombre + " recibiendo un saludo por datagrama";
                byte[] tmp = saludo.getBytes();
                DatagramPacket p1 = new DatagramPacket(tmp, tmp.length, p.getAddress(), p.getPort());
                ds.send(p1);
            }
        } catch (SocketException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
