package org.example.practica5;

import org.example.practica2.model.Song;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

// 1. Implementamos 'Runnable' para que pueda ejecutarse en un hilo separado
public class ClientHandler implements Runnable {

    // Constantes de la práctica
    private static final int TAM = 30000; // Bytes a enviar p/datagrama

    // Información del cliente y la canción
    private Song song;
    private InetAddress clientAddress;
    private int clientPort;
    private DatagramSocket socket; // El socket *propio* de este hilo

    public ClientHandler(Song song, InetAddress clientAddress, int clientPort) {
        this.song = song;
        this.clientAddress = clientAddress;
        this.clientPort = clientPort;
    }

    @Override
    public void run() {
        // 2. Este hilo crea su PROPIO socket en un puerto aleatorio
        // El cliente responderá automáticamente a este puerto.
        try (
                DatagramSocket threadSocket = new DatagramSocket();
                FileInputStream fis = new FileInputStream(song.getFile());
                BufferedInputStream bis = new BufferedInputStream(fis)
        ) {
            this.socket = threadSocket;
            System.out.println("[Handler " + socket.getLocalPort() + "] Hilo iniciado para: " + song.getName() + " -> " + clientAddress.getHostAddress() + ":" + clientPort);

            // 3. --- INICIA LÓGICA QUE TENIAMOS DE SERVER.JAVA ---


            byte[] data = bis.readAllBytes();
            System.out.println("[Handler " + socket.getLocalPort() + "] Longitud de la canción: " + data.length);

            // Mandamos la canción por partes.
            if (data.length > TAM) {
                // Número de peticiones a enviar para terminar el archivo.
                int tp = (data.length / TAM);
                int init_index = 0;
                int end_index = 0;
                int x = 0;

                for (int j = 0; j < tp; j++) {
                    init_index = j * TAM;
                    end_index = (j * TAM) + TAM;
                    x++;

                    byte[] tmp = Arrays.copyOfRange(data, init_index, end_index);
                    // 4. Creamos el paquete dirigido al cliente
                    DatagramPacket packet = new DatagramPacket(tmp, tmp.length, clientAddress, clientPort);
                    socket.send(packet);

                    // Recibimos el eco desde el cliente (en nuestro socket de hilo)
                    byte[] ackBuf = new byte[1024];
                    DatagramPacket ackPacket = new DatagramPacket(ackBuf, ackBuf.length);
                    socket.receive(ackPacket); // Espera el ECO

                    int eco_index = Integer.parseInt(new String(ackPacket.getData(), 0, ackPacket.getLength()));
                    if (x != eco_index) {
                        System.out.println("[Handler " + socket.getLocalPort() + "] Error de paquete, retrocediendo...");
                        j = eco_index; // (Esta lógica de retroceso sigue aquí)
                    }
                    // System.out.println("[Handler " + socket.getLocalPort() + "] Paquete x = " + eco_index + " recibido");
                }

                // 5. --- INICIA LÓGICA DE SOBRANTES ---
                if (data.length % TAM > 0) {
                    int sobrantes = data.length % TAM;
                    int inicio_sobrantes = tp * TAM;
                    int fin_sobrantes = inicio_sobrantes + sobrantes;

                    byte[] tmp = Arrays.copyOfRange(data, inicio_sobrantes, fin_sobrantes);
                    DatagramPacket packet = new DatagramPacket(tmp, tmp.length, clientAddress, clientPort);
                    socket.send(packet);

                    // Recibimos el ultimo ECO de bits.
                    byte[] ackBuf = new byte[1024];
                    DatagramPacket ackPacket = new DatagramPacket(ackBuf, ackBuf.length);
                    socket.receive(ackPacket);
                    int eco_index = Integer.parseInt(new String(ackPacket.getData(), 0, ackPacket.getLength()));
                    // System.out.println("[Handler " + socket.getLocalPort() + "] Paquete final x = " + eco_index + " (sobrantes) recibido");
                }

                System.out.println("[Handler " + socket.getLocalPort() + "] Transferencia de '"+ song.getName() +"' completada.");
                byte[] exit = "salir".getBytes();
                DatagramPacket packet = new DatagramPacket(exit, exit.length, clientAddress, clientPort);
                socket.send(packet);
            }
            // 6. --- FIN LÓGICA DE SERVER.JAVA ---

        } catch (IOException e) {
            System.err.println("[Handler] Error en el hilo: " + e.getMessage());
            e.printStackTrace();
        }
        // El socket y los streams se cierran automáticamente gracias al 'try-with-resources'
    }
}