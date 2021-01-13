package com.example.android.arkanoid.Classes;

import android.content.Context;
import android.util.Log;

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
    private boolean nitroCreated;
    private boolean switchCreated;

    public Level(Context context, int level) {

        this.level = level;
        nitroCreated = false;
        switchCreated = false;
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
                Brick b = new Brick(context, j * BRICK_HORIZONTAL_DISTANCE, i * BRICK_VERTICAL_DISTANCE, level, nitroCreated, switchCreated);
                brickList.add(b);
                if (b.isNitro())
                    nitroCreated = true;
                if (b.isSwitch())
                    switchCreated = true;
            }
        }
    }

    protected ArrayList<Brick> getBrickList() {
        return brickList;
    }
}
