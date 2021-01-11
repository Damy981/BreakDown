package com.example.android.arkanoid.Classes;

import java.io.Serializable;

public class PowerUp implements Serializable {

    //int constant which symbolize the power-up position in the Profile.PowerUps arraylist
    public static final int COINS_DROP_RATE = 0;
    public static final int PADDLE_LENGTH = 1;
    public static final int FREEZE = 2;
    public static final int EXPLOSIVE_BALL = 3;
    public static final int ITEM5 = 4;
    //-----------------------------------------
    private String name;
    private int price;
    private int quantity;

    public PowerUp(String name, int price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }
    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    static public float[] freeze(float xSpeed, float ySpeed) {
        float[] ballSpeed = new float[2];
        ballSpeed[0] = xSpeed / 2;
        ballSpeed[1] = ySpeed / 2;
        return ballSpeed;
    }

    static public boolean explosiveBall() {
        return true;
    }
}

