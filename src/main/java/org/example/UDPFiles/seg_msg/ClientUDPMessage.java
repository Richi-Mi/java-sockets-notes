package org.example.UDPFiles.seg_msg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Arrays;

public class ClientUDPMessage {

    private static final int PORT = 8080;
    private static final String HOST = "127.0.0.1";

    public static void main(String[] args) {
        try {
            InetAddress dst = InetAddress.getByName(HOST);
            // tam - Bytes a enviar por solicitud.
            int tam = 10, x;
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            DatagramSocket cl = new DatagramSocket();

            while (true) {
                x = 0;
                System.out.println("Escribe un mensaje, <Enter> para enviar, \"salir\" para terminar ");
                String message = br.readLine();

                // Cierra entradas / salidas de datos y termina el programa.
                if( message.compareToIgnoreCase("salir") == 0 ) {
                    System.out.println("Adios....");
                    br.close();
                    cl.close();
                    System.exit(0);
                }
                else {
                    byte[] buffer = message.getBytes();
                    if( buffer.length > tam ) {
                        byte[] b_eco = new byte[buffer.length];
                        // Numero de peticiones a enviar para terminar el mensaje completo.
                        int tp = (int) (buffer.length / tam);

                        for( int j = 0; j < tp; j++ ) {
                            x++;
                            // Mandamos una parte del mensaje dependiendo del tamaño a enviar.
                            byte[] tmp = Arrays.copyOfRange(buffer, j * tam, ((j * tam) + tam));
                            DatagramPacket p = new DatagramPacket(tmp, tmp.length, dst, PORT);
                            cl.send(p);

                            // Recibimos el mensaje de confirmación del servidor
                            System.out.println("Enviando fragmento " + x + "\ndesde:" + (j * tam) + " hasta " + ((j * tam) + (tam - 1)));
                            DatagramPacket pr = new DatagramPacket(new byte[tam], tam);
                            cl.receive(pr);

                            // Reconstruimos el ECO recibido.
                            byte[] b_pr = pr.getData();
                            for( int i = 0; i < tam; i++ ) {
                                b_eco[(j * tam) + i] = b_pr[i];
                            }
                        }
                        if(buffer.length % tam > 0) {
                            x++;
                            int sobrantes = buffer.length % tam;
                            System.out.println("Sobrantes: " + sobrantes);

                            // Mandamos el ultimo pedazo de bits restantes.
                            byte[] tmp = Arrays.copyOfRange(buffer, tp * tam, ((tp * tam) + sobrantes));
                            DatagramPacket p = new DatagramPacket(tmp, tmp.length, dst, PORT);
                            cl.send(p);

                            // Recibimos el ultimo ECO de bits.
                            DatagramPacket p1 = new DatagramPacket(new byte[tam], tam);
                            cl.receive(p1);

                            byte[] b_pr = p1.getData();
                            for( int i = 0; i < sobrantes; i++ ) {
                                b_eco[(tp * tam) + i] = b_pr[i];
                            }
                        }
                        String eco = new String(b_eco);
                        System.out.println("ECO Recibido: " + eco);
                    }
                    else {
                        // Manda mensaje al servidor con la entrada de buffer < 10 bytes.
                        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, dst, PORT);
                        cl.send(packet);

                        // Recibe mensaje de confirmación del servidor.
                        DatagramPacket packetReceive = new DatagramPacket(new byte[65535], 65535);
                        cl.receive(packetReceive);

                        // Mostrar mensaje en pantalla.
                        String eco = new String(packetReceive.getData(), 0, packetReceive.getLength());
                        System.out.println("ECO recibido: " + eco);
                    }
                }
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
