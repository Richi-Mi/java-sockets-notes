package org.example.severP3.data;

import org.example.severP3.model.Chat;
import org.example.severP3.model.Message;
import org.example.severP3.model.User;
import org.json.simple.JSONArray;

import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

public class AppDataSource {
    private final ArrayList<User> usuarios;
    public final ArrayList<Chat> chatList;

    public AppDataSource() {
        this.usuarios = new ArrayList<>();
        this.chatList = new ArrayList<>();
    }
    public void addUser(String name, SocketAddress address) {
        User newUser = null;

        for(User user : this.usuarios)
            if(user.name().equals(name))
                newUser = user;

        if( newUser == null) {
            newUser = new User(name, address);
            this.usuarios.add(newUser);
        }
    }
    public List<User> getUsers() {
        return this.usuarios;
    }
    public User getUserByName(String name) {
        for(User user : this.usuarios) {
            if(user.name().equals(name))
                return user;
        }
        return null;
    }

    public void addChat(String name, User from) {
        Chat chat = new Chat(name, from.name());
        // chat.addUserToChat(from);
        chatList.add(chat);
    }

    public List<Chat> getChats() {
        return this.chatList;
    }
    public Chat getChatByIndex(int index) {
        Chat chat = chatList.get(index);

        return chat;
    }
    public JSONArray getJsonChats() {
        JSONArray jsonArray = new JSONArray();
        for(Chat chat : this.chatList) {
            jsonArray.add(chat.toJson());
        }

        return jsonArray;
    }

    public void addMessageToChat(int index, Message message) {
        chatList.get(index).messageList.add(message);
    }
}
