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
    private final int level;
    private boolean hardBrick;
    private boolean nitroBrick;
    private boolean switchBrick;

    public Brick(Context context, float x, float y, int level) {
        super(context);
        this.x = x;
        this.y = y;
        this.level = level;
        hardBrick = false;
        nitroBrick = false;
        switchBrick = false;
        createSkins();
    }

    //assigns a random image to the brick
    private void createSkins() {
        int SKIN_NUMBER = 10;
        int a = (int) (Math.random() * SKIN_NUMBER);
        switch (a) {
            case 0:
                brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_blue);
                break;
            case 1:
                brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_grey);
                if (level >= 5) {
                    brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_black);
                    hardBrick = true;
                }
                break;
            case 2:
                brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_green);
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
                brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_orange);
                break;
            case 9:
                brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_violet);
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

    public boolean isHardBrick() {
        return hardBrick;
    }

    public void setHardFalse() {
        hardBrick = false;
    }

    public void changeHardBrickColor() {
        if (!isHardBrick())
            brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_grey);
    }

    public boolean isNitro() {
        return nitroBrick;
    }

    public boolean isSwitch() {
        return switchBrick;
    }

    public void setSwitchBrickOff() {
        brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_switch_off);
    }

    public void createNitro() {
        brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_nitro);
        nitroBrick = true;
    }

    public void createSwitch() {
        brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_switch_on);
        switchBrick = true;
    }


}
