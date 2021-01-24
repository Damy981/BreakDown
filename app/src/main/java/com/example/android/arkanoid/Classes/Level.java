package com.example.android.arkanoid.Classes;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Level {

    static public final int BRICK_HORIZONTAL_DISTANCE = 123;
    static public final int BRICK_VERTICAL_DISTANCE = 93;
    private final int ROW_START = 3;
    private final int COLUMN_START = 1;
    private final int COLUMN_NUMBER = 7;

    private final ArrayList<Brick> brickList = new ArrayList<>();
    private final int level;
    private int rowNumber;
    private int iNitro;
    private int jNitro;
    private int iSwitch;
    private int jSwitch;
    private final Random random;

    public Level(Context context, int level) {

        this.level = level;
        random = new Random();
        generateBricks(context);
    }

    private void generateBricks(Context context) {
        rowNumber = 4;
        if (level >= 5)
            rowNumber = 5;
        if (level >= 15)
            rowNumber = 6;

        generateRandomIndex();

        for (int i = ROW_START; i < ROW_START + rowNumber; i++) {
            for (int j = COLUMN_START; j < COLUMN_START + COLUMN_NUMBER; j++) {
                Brick b = new Brick(context, j * BRICK_HORIZONTAL_DISTANCE, i * BRICK_VERTICAL_DISTANCE, level);

                if (level >= 15) {
                    if (i == iNitro && j == jNitro) {
                        b.createNitro();
                        b.setHardFalse();
                    }
                    if (i == iSwitch && j == jSwitch) {
                        b.createSwitch();
                        b.setHardFalse();
                    }
                }
                brickList.add(b);
            }
        }
    }

    protected ArrayList<Brick> getBrickList() {
        Collections.shuffle(brickList);
        return brickList;
    }

    private void generateRandomIndex() {
        iNitro =  random.nextInt((ROW_START + rowNumber) - ROW_START) + ROW_START;
        jNitro = random.nextInt((COLUMN_START + COLUMN_NUMBER) - COLUMN_START) + COLUMN_START;
        do {
            iSwitch =  random.nextInt((ROW_START + rowNumber) - ROW_START) + ROW_START;
            jSwitch = random.nextInt((COLUMN_START + COLUMN_NUMBER) - COLUMN_START) + COLUMN_START;
        } while(iNitro == iSwitch && jNitro == jSwitch);
    }
}