package com.example.android.arkanoid.Classes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;

import com.example.android.arkanoid.R;

public class Brick extends View {

    private Bitmap brick;
    private float x;
    private float y;
    private final int COLORS_NUMBER = 10;


    public Brick(Context context, float x, float y) {
        super(context);
        this.x = x;
        this.y = y;
        skin();
    }

    //assigns a random image to the brick
    private void skin() {
        int a = (int) (Math.random() * COLORS_NUMBER);
        switch (a) {
            case 0:
                brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_beige);
                break;
            case 1:
                brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_grey);
                break;
            case 2:
                brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_light_green);
                break;
            case 3:
                brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_light_orange);
                break;
            case 4:
                brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_pink);
                break;
            case 5:
                brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_red);
                break;
            case 6:
                brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_sky_blue);
                break;
            case 7:
                brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_yellow);
                break;
            case 8:
                brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_lilac);
                break;
            case 9:
                brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_light_violet);
                break;
        }
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public void setX(float x) {
        this.x = x;
    }

    @Override
    public float getY() {
        return y;
    }

    @Override
    public void setY(float y) {
        this.y = y;
    }

    public Bitmap getBrick() {
        return brick;
    }
}
