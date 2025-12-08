package org.example.practica4;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSONDataSource {

    private static JSONDataSource instance;
    private JSONObject jsonObject;
    private JSONParser parser;

    private JSONDataSource() {
        this.jsonObject = new JSONObject();
        this.jsonObject.put("serverStatus", "running");
        this.parser = new JSONParser();
    }

    public static synchronized JSONDataSource getInstance() {
        if (instance == null) {
            instance = new JSONDataSource();
        }
        return instance;
    }

    public synchronized String getData() {
        return jsonObject.toJSONString();
    }

    public synchronized void saveData(String jsonBody) throws ParseException {
        Object obj = parser.parse(jsonBody);
        if (obj instanceof JSONObject) {
            this.jsonObject = (JSONObject) obj;
        }
    }

    public synchronized void appendData(String jsonBody) throws ParseException {
        Object obj = parser.parse(jsonBody);
        if (obj instanceof JSONObject) {
            JSONObject newJson = (JSONObject) obj;
            this.jsonObject.putAll(newJson);
        }
    }

    public synchronized void deleteData() {
        this.jsonObject.clear();
    }
}