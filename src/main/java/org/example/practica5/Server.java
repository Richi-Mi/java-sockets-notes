package org.example.practica5;

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
        File folder = new File(DIR_SONGS);

        // 1. Verificamos que la carpeta exista y sea un directorio
        if (folder.exists() && folder.isDirectory()) {

            // 2. Obtenemos todos los archivos dentro de la carpeta
            File[] files = folder.listFiles();

            if (files != null) {
                for (File file : files) {
                    // 3. Filtramos para asegurarnos que sean archivos y (opcional) que sean de audio
                    // Puedes agregar más extensiones como .mp3 o .wav si lo necesitas
                    if (file.isFile() && (file.getName().endsWith(".mpeg") || file.getName().endsWith(".mp3"))) {

                        // Como estamos leyendo automáticamente, no tenemos el "Nombre Artista" ni el "Título"
                        // a mano. Usaremos el nombre del archivo como título temporal.
                        String title = file.getName();
                        String artist = "Desconocido"; // O podrías intentar extraerlo del nombre del archivo

                        // 4. Agregamos la canción a la lista
                        songs.add(new Song(title, artist, file));
                    }
                }
            }
        } else {
            System.out.println("La carpeta no existe o la ruta es incorrecta: " + DIR_SONGS);
        }

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