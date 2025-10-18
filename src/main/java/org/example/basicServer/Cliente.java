package org.example.basicServer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class Cliente {
    public static void main(String[] args) {
        try {
            Socket cl = new Socket("127.0.0.1", 1234);
            System.out.println("Conexión establecida... Escribe tu nombre: ");
            BufferedReader brIn = new BufferedReader( new InputStreamReader(System.in));

            String nombre = brIn.readLine();
            PrintWriter pw      = new PrintWriter(new OutputStreamWriter(cl.getOutputStream()));
            BufferedReader br   = new BufferedReader(new InputStreamReader(cl.getInputStream()));

            pw.println(nombre);
            pw.flush(); // Vacia el buffer y manda el mensaje
            System.out.println("Mensaje enviado...\n Recibiendo saludo.");
            String saludo = br.readLine();
            System.out.println(saludo);

            pw.close();
            br.close();
            brIn.close();
            cl.close();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
