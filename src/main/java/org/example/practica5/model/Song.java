package org.example.practica5.model;

import java.io.File;

public class Song {
    private final File file;
    private final String name;
    private final String artist;

    public Song(String name, String artist, File file) {
        this.file = file;
        this.name = name;
        this.artist = artist;
    }

    public String getName() {
        return name;
    }

    public String getArtist() {
        return artist;
    }
    public File getFile() {
        return file;
    }
}
