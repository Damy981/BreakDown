package com.example.android.arkanoid.Classes;

import android.graphics.Bitmap;

import java.io.Serializable;

/*This class is used to serialize the level created from LevelEditor.
* This provides a simplified representation for Brick class(which extends View, that is an non serializable class)*/

public class SerializableBrick implements Serializable {

    private final float x;
    private final float y;
    private final BitmapDataObject bitmap;
    private boolean hardBrick;
    private boolean nitroBrick;
    private boolean switchBrick;

    public SerializableBrick (float x, float y, BitmapDataObject bitmap, boolean hardBrick, boolean nitroBrick, boolean switchBrick) {
        this.x = x;
        this.y = y;
        this.bitmap = bitmap;
        this.hardBrick = hardBrick;
        this.nitroBrick = nitroBrick;
        this.switchBrick = switchBrick;
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

    public boolean isHardBrick() {
        return hardBrick;
    }

    public boolean isNitroBrick() {
        return nitroBrick;
    }

    public boolean isSwitchBrick() {
        return switchBrick;
    }
}
