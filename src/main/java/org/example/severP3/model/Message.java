package org.example.severP3.model;

import org.json.simple.JSONObject;

public class Message {
    private String content;
    private String owner;

    public Message(String content, String owner) {
        this.content = content;
        this.owner = owner;
    }

    public String getContent() {
        return content;
    }

    public String getOwner() {
        return owner;
    }

    public JSONObject toJSON() {
        JSONObject object = new JSONObject();

        object.put("content", this.content);
        object.put("owner", this.owner);

        return object;
    }
}
