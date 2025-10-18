package org.example.basicServer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.StandardSocketOptions;

/**
 * <h1> Flujos (Stream) </h1>
 * Es una manera de transmitir información de manera sincrona.
 * <ul>
 *     <li> <b>Flujos orientados a byte:</b> Transporta bytes </li>
 *     <ol>
 *         <li> Input Stream </li>
 *         <li> Output Stream </li>
 *     </ol>
 *     <li> <b>Flujos orientados a carácter</b> Transporta caracteres </li>
 *     <ol>
 *         <li> Reader: Buffered Reader </li>
 *         <li> Writter: PrintWritter </li>
 *     </ol>
 * </ul>
 * Los sockets devuelven instancias de InputStream y OutputStream <br>
 * Si se le agrega Buffered o PrintWriter, ya no es carácter por carácter es un arreglo de bits generalmente
 * <h3> StandardSocketOptions.SO_REUSEADDR </h3>
 * <p> Se utiliza para permitir que un servidor se enlace a una dirección IP y puerto que ya está siendo utilizado por otro socket, especialmente cuando el otro socket está en el estado de espera de cierre </p>
 *
 * */

public class Server {

    public static void main(String[] args) {
        try {
            // Server creation.
            ServerSocket ss = new ServerSocket(1234);
            System.out.println(" S: Server started on port: " + ss.getLocalPort());
            // Adding option SO_REUSEADDR.
            ss.setOption(StandardSocketOptions.SO_REUSEADDR, true);
            System.out.println(" S: Valor por defecto de la opción SO_RCVBUF: " + ss.getOption(StandardSocketOptions.SO_RCVBUF));
            // Listening connections.
            do {
                // Accept connection
                Socket client = ss.accept();
                System.out.println(" * Cliente conectado desde: " + client.getInetAddress() + ':' + client.getPort());

                // getInputStream()  Flujo de bajo nivel orientado a byte qué permite leer 1 byte a la vez.
                // getOutputStream() Flujo de bajo nivel orientado a byte qué permite escribir 1 byte a la vez.

                // OutputStreamWriter Convierto byte's a caracteres de salida.
                // InputStreamReader Convierto byte's a caracteres de entrada

                // PrintWritter Flujo orientado a caracter que permite escribir un arreglo de caracteres.
                // BufferedReader Flujo orientado a caracter qué permite leer un arreglo de caracteres.
                PrintWriter pw = new PrintWriter(new OutputStreamWriter((client.getOutputStream())));
                BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));

                String nombre = br.readLine();
                System.out.println("Nombre recibido: " + nombre);
                String saludo = "Hola " + nombre + " recibe un saludo desde el servidor";
                pw.println(saludo);
                pw.flush(); // Vacia el buffer y manda el mensaje

                br.close();
                pw.close();
                client.close();
            } while (true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

}
