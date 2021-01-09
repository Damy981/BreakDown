package com.example.android.arkanoid.Classes;

import android.content.Context;

import java.util.ArrayList;

public class Level {

    private final int BRICK_HORIZONTAL_DISTANCE = 123;
    private final int BRICK_VERTICAL_DISTANCE = 93;
    private final int ROW_START = 3;
    private final int COLUMN_START = 1;
    private final int COLUMN_NUMBER = 7;

    private ArrayList<Brick> brickList = new ArrayList<>();
    private int level;
    private int rowNumber;

    public Level(Context context, int level) {

        this.level = level;
        generateBricks(context);
    }

    private void generateBricks(Context context) {
        rowNumber = 4;
        if (level >= 5)
            rowNumber = 5;
        if (level >= 15)
            rowNumber = 6;
        for (int i = ROW_START; i < ROW_START + rowNumber; i++) {
            for (int j = COLUMN_START; j < COLUMN_START + COLUMN_NUMBER; j++) {
                brickList.add(new Brick(context, j * BRICK_HORIZONTAL_DISTANCE, i * BRICK_VERTICAL_DISTANCE));
            }
        }
    }

    protected ArrayList<Brick> getBrickList() {
        return brickList;
    }
}
