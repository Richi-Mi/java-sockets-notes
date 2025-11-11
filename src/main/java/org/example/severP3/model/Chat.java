package org.example.severP3.model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Chat {

    private final String name;
    public List<User> userList;
    public List<Message> messageList;

    public Chat(String name) {
        this.name   = name;
        userList    = new ArrayList<>();
        messageList = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void addUserToChat(User user) {
        userList.add(user);
    }

    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", name);

        JSONArray jsonArray = new JSONArray();

        for(User user : userList)
            jsonArray.add(user.toJSON());

        jsonObject.put("users", jsonArray);
        return jsonObject;
    }
    @Override
    public String toString() {
        return "Chat(name='" + name + "')";
    }
}
