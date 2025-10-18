package org.example.UDPFiles.metadata;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ServerMetadata {

    private static final int PORT = 5555;
    private static final int LENGTH = 65535;

    public static void main(String[] args){
        try{
            DatagramSocket s = new DatagramSocket(PORT);
            System.out.println("Servidor esperando datagrama..");

            while ( true ){
                // Recibimos paquete de datagrama.
                DatagramPacket p = new DatagramPacket(new byte[LENGTH],LENGTH);
                s.receive(p);

                // Leemos información recibida
                DataInputStream dis = new DataInputStream(new ByteArrayInputStream(p.getData()));
                int n = dis.readInt();
                int tam = dis.readInt();
                byte[] b = new byte[tam];
                int x = dis.read(b);


                String cadena = new String(b);
                System.out.println("Paquete recibido con los datos: #paquete->"+ n+ " con "+tam+" bytes y el mensaje:"+cadena);
                dis.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
