package org.example.sendObjects;

import java.io.Serializable;

public class MyObject implements Serializable {

    //private transient int FU; // Evita que se mande una variable al serializar el objeto
    private transient int x;
    private float y;
    private String z;
    private int[][] m;

    public MyObject(
            int x,
            float y,
            String z,
            int[][] m
    ) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.m = m;
    }

    public int getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public String getZ() {
        return z;
    }

    public int[][] getM() {
        return m;
    }
}
