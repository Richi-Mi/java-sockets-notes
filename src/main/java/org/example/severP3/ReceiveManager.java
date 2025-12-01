package org.example.severP3;

import org.example.severP3.data.AppDataSource;
import org.example.severP3.model.Chat;
import org.example.severP3.model.Message;
import org.example.severP3.model.User;
import org.json.simple.JSONArray;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.MulticastSocket;
import java.net.SocketAddress;
import java.util.Arrays;

public class ReceiveManager extends Thread {

    private static final int PACKET_LENGTH = 65535;

    private final MulticastSocket serverSocket;
    private SocketAddress address;

    private AppDataSource dataSource;

    public ReceiveManager(
            AppDataSource dataSource,
            MulticastSocket serverSocket
    ) {
        this.serverSocket = serverSocket;
        this.dataSource = dataSource;
    }

    @Override
    public void run() {
        do {
            try {
                // Receive data.
                byte[] buffer = new byte[PACKET_LENGTH];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                serverSocket.receive(packet);

                this.address = packet.getSocketAddress();

                // Parse the request.
                DatagramPacket eco_packet = new DatagramPacket(packet.getData(), packet.getLength(), address);
                String[] request = new String(packet.getData(), 0, packet.getLength()).split(",");

                // ENTER,NAME
                // CREATE,NAME,SENDERID
                // GET,SENDERID
                // GETCHAT,CHATID,SENDERID
                // SENDMESSAGE,SENDERID,CHATID,MESSAGECONTENT
                // LEAVECHAT,SENDERID,CHATID


                // Ingresar al sistema.
                if (request[0].equals("ENTER")) {
                    dataSource.addUser(request[1], address);
                    eco_packet.setData("success".getBytes());
                }
                // Crear un nuevo chat
                else if (request[0].equals("CREATE")) {
                    dataSource.addChat(request[1], dataSource.getUserByName(request[2]));
                    eco_packet.setData("success".getBytes());
                }
                // Obtener los chats disponibles.
                else if (request[0].equals("GET")) {
                    JSONArray jsonArray = dataSource.getJsonChats();
                    byte[] data = jsonArray.toJSONString().getBytes();
                    eco_packet.setData(data);
                }
                // TODO: PROVE THIS.
                else if (request[0].equals("SENDMESSAGE")) {

                    User user = dataSource.getUserByName(request[1]);
                    int chatId = Integer.parseInt(request[2]);
                    String message = request[3];

                    System.out.println("Mensaje recibido " + message + " de " + user.name());
                    dataSource.addMessageToChat(chatId, new Message(message, user.name()));

                    // Manda el mensaje a todos los usuarios activos en el chat.
                    Chat chat = dataSource.getChatByIndex(chatId);

                    for (User usr : chat.userList) {
                        try {
                            byte[] bufferJSON = chat.toJson().toString().getBytes();
                            DatagramPacket packetToSend = new DatagramPacket(bufferJSON, bufferJSON.length, usr.socketAddress());
                            serverSocket.send(packetToSend);
                        } catch (IOException e) {
                            System.out.println("Error sending chat");
                            throw new RuntimeException(e);
                        }
                    }
                    continue;
                } else if (request[0].equals("GETCHAT")) {
                    int chatId = Integer.parseInt(request[1]);
                    User user = dataSource.getUserByName(request[2]);
                    Chat chat = dataSource.getChatByIndex(chatId);
                    chat.addUserToChat(user);
                    eco_packet.setData(chat.toJson().toString().getBytes());
                } else if (request[0].equals("LEAVECHAT")) {
                    // LEAVECHAT,SENDERID
                    User user = dataSource.getUserByName(request[1]);

                    dataSource.removeUser(user);
                    eco_packet.setData("success".getBytes());

                    System.out.println("Usuario: " + user.name() + "sacado del chat");
                }

                // Mandamos eco de la petición.
                serverSocket.send(eco_packet);
                System.out.println(dataSource.getUsers());
                System.out.println(dataSource.getChats());
            } catch (IOException e) {
                System.out.println("Error al recibir/enviar el paquete");
                throw new RuntimeException(e);
            }
        } while (true);
    }
}
