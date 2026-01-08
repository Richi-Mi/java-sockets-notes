package org.example.practica4;

import org.example.practica4.builder.HttpResponseBuilder;
import org.example.practica4.builder.ResponseStatus;
import org.example.practica4.builder.ResponseTypes;
import org.json.simple.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ServerManager extends Thread {

    private static final String DIR_FILES = "src/main/java/org/example/practica4/assets/";

    private final boolean isSecondary;

    private final Socket client;
    private final Scanner in;

    public ServerManager(Socket client) {
        this.client = client;
        this.isSecondary = false;
        try {
            this.in  = new Scanner(client.getInputStream());
        } catch (IOException e) {
            System.out.println("Error creating output/input stream");
            throw new RuntimeException(e);
        }
    }
    public ServerManager(Socket client, boolean isSecondary) {
        this.client = client;
        this.isSecondary = isSecondary;
        try {
            this.in  = new Scanner(client.getInputStream());
        } catch (IOException e) {
            System.out.println("Error creating output/input stream");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        byte[] responseBytes = new byte[0];

        try {
            // TEST FOR 500 error:
            // int a = 9 / 0;

            if (in.hasNextLine()) {
                String requestLine = in.nextLine();
                if(!isSecondary) {
                    // TEST FOR two servers
                    // Thread.sleep(10000);
                    System.out.println("Request Header: " + requestLine);
                }
                else {
                    System.out.println("Request Header on second server: " + requestLine);
                }



                // get the content-length (For POST and DELETE)
                int contentLength = 0;
                while (in.hasNextLine()) {
                    String line = in.nextLine();
                    if (line.isEmpty()) break; // Fin de cabeceras

                    if (line.startsWith("Content-Length:")) {
                        contentLength = Integer.parseInt(line.split(":")[1].trim());
                    }
                }
                // Read the body if exists
                StringBuilder bodyBuilder = new StringBuilder();
                if (contentLength > 0) {
                    in.useDelimiter("");
                    for (int i = 0; i < contentLength; i++) {
                        if (in.hasNext()) {
                            bodyBuilder.append(in.next());
                        }
                    }
                }
                String body = bodyBuilder.toString();

                StringDataSource stringDataSource = StringDataSource.getInstance();
                JSONDataSource jsonDataSource = JSONDataSource.getInstance();
                // 1. Resource: JSON.
                if (requestLine.startsWith("GET /api/json ")) {
                    responseBytes = new HttpResponseBuilder()
                            .setStatus(ResponseStatus.OK)
                            .setBody(jsonDataSource.getData(), ResponseTypes.JSON)
                            .build();
                }
                else if (requestLine.startsWith("POST /api/json ")) {
                    jsonDataSource.saveData(body);

                    responseBytes = new HttpResponseBuilder()
                            .setStatus(ResponseStatus.OK)
                            .setBody(jsonDataSource.getData(), ResponseTypes.JSON)
                            .build();
                }
                else if (requestLine.startsWith("PUT /api/json ")) {
                    jsonDataSource.appendData(body);

                    responseBytes = new HttpResponseBuilder()
                            .setStatus(ResponseStatus.OK)
                            .setBody(jsonDataSource.getData(), ResponseTypes.JSON)
                            .build();
                }
                else if (requestLine.startsWith("DELETE /api/json ")) {
                    jsonDataSource.deleteData();

                    responseBytes = new HttpResponseBuilder()
                            .setStatus(ResponseStatus.OK)
                            .setBody(jsonDataSource.getData(), ResponseTypes.JSON)
                            .build();
                }
                // 2. Resource: Plain Text.
                else if (requestLine.startsWith("GET /api/text ")) {
                    responseBytes = new HttpResponseBuilder()
                            .setStatus(ResponseStatus.OK)
                            .setBody(stringDataSource.getData(), ResponseTypes.TEXT)
                            .build();
                }
                else if (requestLine.startsWith("POST /api/text ")) {
                    stringDataSource.saveData(body);

                    responseBytes = new HttpResponseBuilder()
                            .setStatus(ResponseStatus.OK)
                            .setBody(stringDataSource.getData(), ResponseTypes.TEXT)
                            .build();
                }
                else if (requestLine.startsWith("PUT /api/text")) {
                    stringDataSource.appendData(body);

                    responseBytes = new HttpResponseBuilder()
                            .setStatus(ResponseStatus.OK)
                            .setBody(stringDataSource.getData(), ResponseTypes.TEXT)
                            .build();
                }
                else if (requestLine.startsWith("DELETE /api/text")) {
                    stringDataSource.deleteData();

                    responseBytes = new HttpResponseBuilder()
                            .setStatus(ResponseStatus.OK)
                            .setBody(stringDataSource.getData(), ResponseTypes.TEXT)
                            .build();
                }
                // 3. Resource: png
                else if (requestLine.startsWith("GET /api/img/png")) {
                    File image = new File(DIR_FILES.concat("github_symbol.png"));
                    BufferedInputStream bis = new BufferedInputStream(new FileInputStream(image));
                    responseBytes = new HttpResponseBuilder()
                            .setStatus(ResponseStatus.OK)
                            .setBody(bis.readAllBytes(), ResponseTypes.IMAGE_PNG)
                            .build();
                }
                // 4. Resource: jpg
                else if (requestLine.startsWith("GET /api/img/jpg")) {
                    File image = new File(DIR_FILES.concat("miku.jpg"));
                    BufferedInputStream bis = new BufferedInputStream(new FileInputStream(image));
                    responseBytes = new HttpResponseBuilder()
                            .setStatus(ResponseStatus.OK)
                            .setBody(bis.readAllBytes(), ResponseTypes.IMAGE_JPG)
                            .build();
                }
                // Bad request if there are no request with the route
                else {
                    responseBytes = new HttpResponseBuilder()
                            .setStatus(ResponseStatus.BAD_REQUEST)
                            .setBody("Solicitud incorrecta", ResponseTypes.TEXT)
                            .build();
                }
            }
        } catch (Exception e) {
            responseBytes = new HttpResponseBuilder()
                    .setStatus(ResponseStatus.INTERNAL_SERVER_ERROR)
                    .setBody("Internal Server Error", ResponseTypes.TEXT)
                    .build();
            System.err.println("Error manejando el cliente: " + e.getMessage());
        } finally {
            try {
                if (!isSecondary) {
                    // Decrement counter
                    ServerHttp.decrementConnections();
                }
                if (client != null) {
                    client.getOutputStream().write(responseBytes);
                    client.getOutputStream().flush();
                    client.close();
                }
            } catch (IOException e) {
                System.err.println("Error cerrando el socket del cliente: " + e.getMessage());
            }
        }
    }
}