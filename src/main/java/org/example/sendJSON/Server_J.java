package org.example.sendJSON;

import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server_J {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(8080);

            System.out.println("Iniciando servidor...");
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Cliente conectado: ..." + socket.getInetAddress() + " enviando datos...");
                JSONObject jsonObject = new JSONObject();

                jsonObject.put("nombre", "adrian");
                jsonObject.put("apPat", "adrian");
                jsonObject.put("apMat", "adrian");

                PrintWriter wr = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                wr.write(jsonObject.toString() + "\n");
                wr.flush();

                System.out.println("JSON enviado....");


            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
