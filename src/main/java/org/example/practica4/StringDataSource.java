package org.example.practica4;

public class StringDataSource {

    private static StringDataSource instance;
    private String data;

    private StringDataSource() {
        this.data = "Init";
    }

    public static synchronized StringDataSource getInstance() {
        if (instance == null) {
            instance = new StringDataSource();
        }
        return instance;
    }

    public synchronized String getData() {
        return data;
    }

    public synchronized void saveData(String newData) {
        this.data = newData;
    }

    public synchronized void appendData(String newData) {
        this.data += newData;
    }

    public synchronized void deleteData() {
        this.data = "";
    }
}