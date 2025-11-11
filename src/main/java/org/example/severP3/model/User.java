package org.example.severP3.model;

import org.json.simple.JSONObject;

import java.net.SocketAddress;

public record User(String name, SocketAddress socketAddress) {

    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", name);
        return jsonObject;
    }

    @Override
    public String toString() {
        return "U:(" + name + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof User user))
            return false;
        return user.name().equals(name);
    }
}
