package com.example.android.arkanoid.Classes;

import android.content.Context;

import java.util.ArrayList;

public class Level {

    private final int BRICK_HORIZONTAL_DISTANCE = 123;
    private final int BRICK_VERTICAL_DISTANCE = 93;

    private ArrayList<Brick> brickList = new ArrayList<>();
    private int level;
    private int rowNumber; //primo for: 7-3 = 4 righe - non si può mettere 0 invece di 3 altrimenti si sposta tutto sopra
    private int columnNumber; //stessa cosa delle righe ma nel secondo for

    public Level(Context context, int level) {

        this.level = level;
        generateBricks(context);

    }

    private void generateBricks(Context context) {
        for (int i = 3; i < 7; i++) {
            for (int j = 1; j < 8; j++) {
                brickList.add(new Brick(context, j * BRICK_HORIZONTAL_DISTANCE, i * BRICK_VERTICAL_DISTANCE));
            }
        }
    }

    protected ArrayList<Brick> getBrickList() {
        return brickList;
    }
}
