package org.example.practica4.builder;

public enum ResponseTypes {
    TEXT("text/plain; charset=utf-8"),
    JSON("application/json; charset=utf-8"),
    IMAGE_PNG("image/png"),
    IMAGE_JPG("image/jpeg");

    private final String mimeType;

    ResponseTypes(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getMimeType() {
        return mimeType;
    }
}