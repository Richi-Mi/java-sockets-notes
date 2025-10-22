package org.example.practica2;

import org.example.practica2.model.Song;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private static final int LENGTH = 65535;
    private static final int PORT = 8080;
    private static final String DIR_SONGS = "src/main/java/org/example/practica2/assets/";
    
    private static List<Song> getMySongs() {
        List<Song> songs = new ArrayList<>();
        File fs2 = new File(DIR_SONGS.concat("mono_no_aware.mpeg"));
        File fs3 = new File(DIR_SONGS.concat("no_capea.mpeg"));
        File fs1 = new File(DIR_SONGS.concat("coqueta_onichan.mpeg"));

        songs.add(new Song("COQUETA", "Grupo Frontera & Grupo Firme", fs1));
        songs.add(new Song("Mono no Aware", "Un Japones", fs2));
        songs.add(new Song("No Capea", "Xavi", fs3));

        return songs;
    }

    public static void main(String[] args) {
        List<Song> songs = getMySongs();
        System.out.println("Songs loaded: " + songs.size());

        try (DatagramSocket ds = new DatagramSocket(PORT)) { // Socket principal en 8080
            ds.setReuseAddress(true);
            System.out.println("Server started on port: " + PORT + " (Concurrente)");

            do {
                // 1. Servidor principal solo espera el *índice* de la canción
                byte[] buf = new byte[LENGTH];
                DatagramPacket packet = new DatagramPacket(buf, LENGTH);
                ds.receive(packet); // Espera petición en puerto 8080

                // 2. Extraemos el índice y la info del cliente
                int index;
                try {
                    
                    index = Integer.parseInt(new String(packet.getData(), 0, packet.getLength()));
                } catch (NumberFormatException e) {
                    System.err.println("Error: Paquete recibido no es un índice numérico. Ignorando.");
                    continue; // Vuelve al inicio del bucle a esperar otro cliente
                }
                
                if (index < 0 || index >= songs.size()) {
                    System.out.println("Índice inválido " + index + ". Ignorando.");
                    continue; // Vuelve al inicio del bucle
                }

                System.out.println("Petición recibida para: " + songs.get(index).getName());

                // 3. Extraemos los datos del cliente del paquete inicial
                InetAddress clientAddress = packet.getAddress();
                int clientPort = packet.getPort();
                Song songToPlay = songs.get(index);

                // 4. Enviamos el ECO de confirmación del índice
                
                ds.send(packet); 

                // 5. Creamos el Handler y lo iniciamos en un Hilo nuevo
                ClientHandler handler = new ClientHandler(songToPlay, clientAddress, clientPort);
                new Thread(handler).start(); // Iniciamos el hilo.

                // 6. El servidor NO espera. Vuelve al 'do-while' para recibir(packet)
                // de un *nuevo* cliente en el puerto 8080.
                
            } while (true);
        } catch (SocketException e) {
            System.out.println("Error al crear el Socket UDP");
            throw new RuntimeException(e);
        } catch (IOException e) {
            System.out.println("Error al leer el datagram packet");
            throw new RuntimeException(e);
        }
    }
}