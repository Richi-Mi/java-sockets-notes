package org.example.severP3.model;

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
}
