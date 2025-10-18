package org.example.practica2;

import org.example.practica2.model.Song;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Server {

    private static final int LENGTH         = 65535;
    private static final int PORT           = 8080;
    private static final String DIR_SONGS   = "/Users/josericardomendoza/IdeaProjects/Sockets/src/main/java/org/example/practica2/assets/";
    private static final String HOST        = "127.0.0.1";
    private static final int TAM            = 60000; // Bytes a enviar p/datagrama.



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

        try {
            InetAddress address = InetAddress.getByName(HOST);
            DatagramSocket ds = new DatagramSocket(PORT);
            ds.setReuseAddress(true);

            System.out.println("Server started on port: " + PORT);

            do {
                // Primero el cliente nos manda un numero para saber el numero de la canción a reproducir.
                byte[] buf = new byte[LENGTH];
                DatagramPacket packet = new DatagramPacket(buf, LENGTH);
                ds.receive(packet);

                int index = Integer.parseInt(new String(packet.getData(), 0, packet.getLength()));
                // Si se recibe 1 indice invalido, entonces rompe el bucle.
                if(index < 0 || index >= songs.size()) {
                    System.out.println("Goodbye");
                    break;
                }

                // Mandar ECO del paquete.
                System.out.println("Se ha recibido el indice " + index + " reproduciendo: " + songs.get(index).getName());
                ds.send(packet);

                // Mandar bytes de la canción.
                FileInputStream fis     = new FileInputStream(songs.get(index).getFile());
                BufferedInputStream bis = new BufferedInputStream(fis);

                // Leemos todos los bytes
                byte[] data = bis.readAllBytes();
                System.out.println("Longitud de la canción: " + data.length);

                // Mandamos la canción por partes.
                if( data.length > TAM ) {
                    byte[] buffer_eco = new byte[data.length];
                    // Número de peticiones a enviar para terminar el archivo.
                    int tp = (data.length / TAM);


                    int init_index = 0;
                    int end_index  = 0;

                    for( int j = 0; j < tp; j++ ) {
                        // Inidices de inicio a fin.
                        init_index = j * TAM;
                        end_index  = (j * TAM) + TAM;

                        // Mandamos un paquete de datos de 60 KB
                        byte[] tmp = Arrays.copyOfRange(data, init_index, end_index);
                        packet.setData(tmp);
                        ds.send(packet);

                        // Recibimos el eco desde el cliente.
                        ds.receive(packet);

                        int eco_index = Integer.parseInt(new String(packet.getData(), 0, packet.getLength()));
                        // TODO: Check this validation (The three validations) ....
                        if( j != eco_index ) {
                            // Retroceder_n
                            j = eco_index;
                        }
                        // ....
                        System.out.println("Paquete x = " + eco_index + " recibido");

                    }
                    if(buffer_eco.length % TAM > 0) {
                        int sobrantes = data.length % TAM;
                        // Mandamos el ultimo pedazo de bits restantes.
                        byte[] tmp = Arrays.copyOfRange(buffer_eco, tp * TAM, ((tp * TAM) + sobrantes));
                        packet.setData(tmp);
                        ds.send(packet);

                        // Recibimos el ultimo ECO de bits.
                        ds.receive(packet);

                        int eco_index = Integer.parseInt(new String(packet.getData(), 0, packet.getLength()));
                        System.out.println("Paquete x = " + eco_index + " recibido");
                    }
                    System.out.println("ECO de los paquetes recibido. ");
                    byte[] exit = "salir".getBytes();
                    packet.setData(exit);
                    ds.send(packet);
                }
                fis.close();
                bis.close();
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
