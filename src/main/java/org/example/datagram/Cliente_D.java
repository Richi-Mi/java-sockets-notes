package org.example.datagram;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class Cliente_D {
    public static void main(String[] args) {
        try {
            DatagramSocket c = new DatagramSocket();
            System.out.println("Cliente iniciado... \nEscribe tu nombre: ");

            BufferedReader br   = new BufferedReader(new InputStreamReader(System.in));
            String nombre       = br.readLine();

            byte[] buffer = nombre.getBytes();

            String dir = "127.0.0.1";
            int porta = 8080;

            DatagramPacket p = new DatagramPacket(buffer, buffer.length, new InetSocketAddress(dir, porta));
            c.send(p);

            System.out.println("Recibiendo mensaje con el contenido: ");
            DatagramPacket p1 = new DatagramPacket(new byte[1024], 1024); // PEOR CASO: 65535
            c.receive(p1);

            String saludo = new String(p1.getData(), 0, p1.getLength());
            System.out.println(saludo);

            br.close();
            c.close();

        } catch (SocketException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
