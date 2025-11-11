package org.example.severP3;

import netscape.javascript.JSObject;
import org.example.severP3.data.AppDataSource;
import org.example.severP3.model.Chat;
import org.example.severP3.model.Message;
import org.example.severP3.model.User;
import org.json.simple.JSONArray;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;

public class Manager extends Thread {

    private static final int PACKET_LENGTH = 65535;

    private final DatagramSocket socket;
    private final SocketAddress address;
    private byte[] receiveBuffer;

    private AppDataSource dataSource;

    public Manager(
            AppDataSource dataSource,
            DatagramSocket socket,
            SocketAddress address,
            byte[] receiveBuffer
    ) {
        this.socket = socket;
        this.address = address;
        this.receiveBuffer = receiveBuffer;
        this.dataSource = dataSource;
    }

    @Override
    public void run() {

        // ENTER,NAME
        // CREATE,NAME,SENDERID
        // GET, SENDERID
        // GETCHAT,CHATID
        // SENDMESSAGE,SENDERID,CHATID,MESSAGECONTENT

        DatagramPacket eco_packet = new DatagramPacket(receiveBuffer, receiveBuffer.length, address);
        String[] request = new String(receiveBuffer).split(",");

        if(request[0].equals("ENTER")) {
            dataSource.addUser(request[1], address);
            eco_packet.setData("success".getBytes());
        }
        else if(request[0].equals("CREATE")) {
            dataSource.addChat(request[1], dataSource.getUserByName(request[2]));
            eco_packet.setData("success".getBytes());
        }
        else if(request[0].equals("GET")) {
            JSONArray jsonArray = dataSource.getJsonChats();
            byte[] data = jsonArray.toJSONString().getBytes();
            eco_packet.setData(data);
        }
        else if(request[0].equals("SENDMESSAGE")) {

            User user       = dataSource.getUserByName(request[1]);
            int chatId      = Integer.parseInt(request[2]);
            String message  = request[3];

            dataSource.addMessageToChat(chatId, new Message(message, user.name()));

            // Manda el mensaje a todos los usuarios activos en el chat.
            Chat chat = dataSource.getChatByIndex(chatId);

            for (User usr : chat.userList) {
                try {
                    byte[] buffer = chat.toJson().toString().getBytes();
                    DatagramPacket packetToSend = new DatagramPacket(buffer, buffer.length, usr.socketAddress());
                    socket.send(packetToSend);
                } catch (IOException e) {
                    System.out.println("Error sending chat");
                    throw new RuntimeException(e);
                }
            }
            eco_packet.setData("success".getBytes());
        }
        else if(request[0].equals("GETCHAT")) {
            int chatId      = Integer.parseInt(request[1]);

            Chat chat = dataSource.getChatByIndex(chatId);
            eco_packet.setData(chat.toJson().toString().getBytes());
        }

        try {
            // Mandamos eco de la petición.
            socket.send(eco_packet);
            System.out.println(dataSource.getUsers());
            System.out.println(dataSource.getChats());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
