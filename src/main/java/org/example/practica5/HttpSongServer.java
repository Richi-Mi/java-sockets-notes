package org.example.practica5;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class HttpSongServer {

    private static final String DIR_SONGS = "src/main/java/org/example/practica2/assets/";
    private static final int PORT = 8081;

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);

        server.createContext("/songs", new SongsHandler());

        server.setExecutor(null);
        server.start();

        System.out.println("Servidor HTTP de canciones corriendo en el puerto " + PORT);
        System.out.println("Carpeta: " + DIR_SONGS);
    }

    // Clase interna para manejar la petición
    static class SongsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // CORS: Permitir acceso desde cualquier origen (útil si pruebas en web, opcional en Android)
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

            if ("GET".equals(exchange.getRequestMethod())) {

                // 1. Obtener archivos
                List<File> songFiles = getSongFiles();

                // 2. Construir JSON manualmente
                // Formato: [{"title":"nombre.mp3", "artist":"Desconocido", "id":"nombre.mp3"}, ...]
                StringBuilder jsonBuilder = new StringBuilder();
                jsonBuilder.append("[");

                for (int i = 0; i < songFiles.size(); i++) {
                    File f = songFiles.get(i);
                    String name = f.getName();

                    // Escapamos comillas dobles por seguridad básica en JSON
                    String safeName = name.replace("\"", "\\\"");

                    jsonBuilder.append("{");
                    jsonBuilder.append("\"title\": \"").append(safeName).append("\",");
                    jsonBuilder.append("\"artist\": \"Canción\",");
                    // IMPORTANTE: El ID es el nombre del archivo para que el server UDP sepa cuál buscar
                    jsonBuilder.append("\"id\": \"").append(safeName).append("\"");
                    jsonBuilder.append("}");

                    if (i < songFiles.size() - 1) {
                        jsonBuilder.append(",");
                    }
                }
                jsonBuilder.append("]");

                String jsonResponse = jsonBuilder.toString();

                // 3. Enviar respuesta
                byte[] responseBytes = jsonResponse.getBytes(StandardCharsets.UTF_8);
                exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
                exchange.sendResponseHeaders(200, responseBytes.length);

                OutputStream os = exchange.getResponseBody();
                os.write(responseBytes);
                os.close();

                System.out.println("➡️ Enviada lista de " + songFiles.size() + " canciones.");

            } else {
                // Método no permitido (405)
                exchange.sendResponseHeaders(405, -1);
            }
        }
    }

    // Tu función de lectura de archivos adaptada
    private static List<File> getSongFiles() {
        List<File> songs = new ArrayList<>();
        File folder = new File(DIR_SONGS);

        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    // Filtramos extensiones de audio
                    if (file.isFile() && (file.getName().endsWith(".mp3") || file.getName().endsWith(".mpeg"))) {
                        songs.add(file);
                    }
                }
            }
        }
        return songs;
    }
}
