package org.example.severP3.model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Chat {

    private final String name;
    private final String owner;
    public List<User> userList;
    public List<Message> messageList;

    public Chat(String name, String owner) {
        this.name   = name;
        this.owner = owner;
        userList    = new ArrayList<>();
        messageList = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void addUserToChat(User user) {
        userList.add(user);
    }
    public void removeUserFromChat(User user) {
        userList.remove(user);
    }

    public JSONObject toJson() {
        System.out.println(this);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", name);

        JSONArray jsonArray = new JSONArray();

        for(User user : userList)
            jsonArray.add(user.toJSON());

        JSONArray messages = new JSONArray();
        for(Message msg : messageList)
            messages.add(msg.toJSON());

        jsonObject.put("users", jsonArray);
        jsonObject.put("messages", messages);

        return jsonObject;
    }
    @Override
    public String toString() {
        return "Chat(name='" + name + "')";
    }
}
