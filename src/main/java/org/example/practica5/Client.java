package org.example.practica5;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Client {

    private static final int LENGTH         = 65535;
    private static final int PORT           = 8080;
    private static final String HOST        = "127.0.0.1";
    private static final int TAM            = 30000; // Bytes a enviar p/datagrama.

    public static void playMp3FromBytes(byte[] mp3ByteArray) {

        // Creamos un nuevo hilo donde reproducir la canción.
        new Thread(() -> {
            try {
                InputStream inputStream = new ByteArrayInputStream(mp3ByteArray);

                // Objeto de JLayer para reproducir audio.
                Player player = new Player(inputStream);

                // Inicia la reproducción de la canción (Si se decodifico bien).
                player.play();
                // Cuando se termina la canción se cierra el reproductor.
                player.close();

            } catch (JavaLayerException e) {
                System.err.println("Error al reproducir el MP3:");
                e.printStackTrace();
            }
        }).start();
    }

    public static void main(String[] args) {
        System.out.println(" * -------------------------------------- *");
        System.out.println(" |      Reproductor de Musica Pirata 🏴‍☠️   |");
        System.out.println(" * -------------------------------------- *");

        Scanner scan = new Scanner(System.in);

        try {
            InetAddress address = InetAddress.getByName(HOST);
            DatagramSocket socket = new DatagramSocket();

            System.out.println(" NUESTRO CATALOGO PIRATA: ");
            System.out.println(" 0. Coqueta ");
            System.out.println(" 1. Mono no Aware");
            System.out.println(" 2. No Capea ");


            int index = -1;

            try {
                System.out.println("Elige una canción (0, 1, o 2):");
                index = scan.nextInt();

                if (index < 0 || index >= 3) {
                    System.out.println("Índice no válido. Proceso cancelado.");
                    System.exit(0);
                }

            } catch (java.util.InputMismatchException e) {
                System.out.println("Error: Debes ingresar un número. Proceso cancelado.");
                System.exit(0); // Cierra el cliente si la entrada es inválida
            }

            byte[] data = Integer.toString(index).getBytes();
            DatagramPacket packet = new DatagramPacket(data, data.length, address, PORT);
            socket.send(packet);

            if( index < 0 || index >= 3) {
                System.out.println(" Proceso cancelado ");
                System.exit(0);
            }

            // Recibimos el ECO.
            socket.receive(packet);
            int eco = Integer.parseInt(new String(packet.getData()));
            System.out.println("Reproduciendo canción: " + eco);

            int x = 0;

            List<Byte> buffer_song = new ArrayList<>();

            do {
                byte[] buffer = new byte[TAM];
                packet.setData(buffer);
                socket.receive(packet);

                String s = new String(packet.getData(), 0, packet.getLength());
                // Verificamos qué no haya enviado un mensaje de salida.
                if( s.equals("salir") ) {
                    System.out.println("Canción recibida al 100%");
                    break;
                }

                x++;

                for( byte b : packet.getData() )
                    buffer_song.add( b );

                System.out.println("Paquete x=" + x + " recibido");
                byte[] eco_buffer = Integer.toString(x).getBytes();
                packet.setData(eco_buffer);
                socket.send(packet);
            } while (true);

            System.out.println("Reproduciendo canción...");
            byte[] song_data = new byte[buffer_song.size()];
            for( int i = 0; i < buffer_song.size(); i++ ) {
                song_data[i] = buffer_song.get(i);
            }

            playMp3FromBytes(song_data);

        } catch (UnknownHostException e) {
            System.out.println("Error: Host no encontrado");
            throw new RuntimeException(e);
        } catch (SocketException e) {
            System.out.println("Error: Socket no encontrado");
            throw new RuntimeException(e);
        } catch (IOException e) {
            System.out.println("Error: No se pudo mandar el socket");
            throw new RuntimeException(e);
        }
    }
}
