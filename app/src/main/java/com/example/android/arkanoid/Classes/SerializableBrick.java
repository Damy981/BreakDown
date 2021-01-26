package com.example.android.arkanoid.Classes;

import android.graphics.Bitmap;

import java.io.Serializable;

public class SerializableBrick implements Serializable {

    private final float x;
    private final float y;
    private final BitmapDataObject bitmap;

    public SerializableBrick (float x, float y, BitmapDataObject bitmap) {
        this.x = x;
        this.y = y;
        this.bitmap = bitmap;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public Bitmap getBitmap() {
        return bitmap.getCurrentImage();
    }
}
