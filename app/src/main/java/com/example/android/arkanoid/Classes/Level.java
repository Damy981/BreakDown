package com.example.android.arkanoid.Classes;

import android.content.Context;

import java.util.ArrayList;

public class Level {

    ArrayList<Brick> brickList = new ArrayList<>();
    int level;

    public Level(Context context, int level) {
        generateBricks(context);
        this.level = level;
    }

    private void generateBricks(Context context) {
        for (int i = 3; i < 7; i++) {
            for (int j = 1; j < 6; j++) {
                brickList.add(new Brick(context, j * 150, i * 100));
            }
        }
    }

    protected ArrayList<Brick> getBrickList() {
        return brickList;
    }
}
