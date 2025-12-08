package org.example.practica4.builder;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Mendoza Castañeda José Ricardo
 * Clase usando el patrón de diseño Builder para construir la respuesta HTTP.
 * */
public class HttpResponseBuilder {

    // * Constant for line salt in http protocol.
    private static final String CRLF = "\r\n";

    private final Map<String, String> headers;
    private ResponseStatus status;
    private byte[] body;

    public HttpResponseBuilder() {
        this.status = ResponseStatus.OK;
        this.headers = new HashMap<>();
        this.body = new byte[0];
        this.headers.put("Connection", "close");
    }

    public HttpResponseBuilder setStatus(ResponseStatus status) {
        this.status = status;
        return this;
    }

    public HttpResponseBuilder setBody(String content, ResponseTypes type) {
        this.body = content.getBytes(StandardCharsets.UTF_8);
        this.headers.put("Content-Type", type.getMimeType());
        this.headers.put("Content-Length", String.valueOf(this.body.length));
        return this;
    }

    public HttpResponseBuilder setBody(byte[] content, ResponseTypes type) {
        this.body = content;
        this.headers.put("Content-Type", type.getMimeType());
        this.headers.put("Content-Length", String.valueOf(this.body.length));
        return this;
    }

    public HttpResponseBuilder addHeader(String key, String value) {
        this.headers.put(key, value);
        return this;
    }

    public byte[] build() {
        StringBuilder headerBuilder = new StringBuilder();

        headerBuilder.append("HTTP/1.1 ").append(this.status).append(CRLF);

        // Parse the headers in the response.
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            headerBuilder.append(entry.getKey())
                    .append(": ")
                    .append(entry.getValue())
                    .append(CRLF);
        }

        headerBuilder.append(CRLF);

        // Parse headers to bytes.
        byte[] headerBytes = headerBuilder.toString().getBytes(StandardCharsets.UTF_8);

        // Combine the response.
        byte[] response = new byte[headerBytes.length + body.length];
        System.arraycopy(headerBytes, 0, response, 0, headerBytes.length);
        System.arraycopy(body, 0, response, headerBytes.length, body.length);

        return response;
    }
}