package com.example.android.arkanoid.Classes;

import java.io.Serializable;

public class PowerUp implements Serializable {

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
}

