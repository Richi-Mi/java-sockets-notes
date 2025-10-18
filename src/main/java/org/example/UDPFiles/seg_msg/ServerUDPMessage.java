package org.example.UDPFiles.seg_msg;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ServerUDPMessage {

    private final static int PORT = 8080;

    public static void main(String[] args) {
        try {
            String message = "";
            DatagramSocket ds = new DatagramSocket(PORT);
            // Usado para reutilizar la misma conexión para cuando se desconecta un cliente.
            ds.setReuseAddress(true);

            System.out.println("Servidor iniciado... esperando datagramas");
            while (true) {
                byte[] buf = new byte[65535];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                ds.receive(packet);

                message = new String(packet.getData(), 0, packet.getLength());
                System.out.println("Se ha recibido datagrama desde "+packet.getAddress()+":"+packet.getPort()+" con el mensaje:"+message);
                ds.send(packet);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
