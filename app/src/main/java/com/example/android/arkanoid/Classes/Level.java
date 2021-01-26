package com.example.android.arkanoid.Classes;

import android.content.Context;

import java.util.ArrayList;
import java.util.Random;

public class Level{

    static public final int BRICK_HORIZONTAL_DISTANCE = 123;
    static public final int BRICK_VERTICAL_DISTANCE = 93;
    static public final int ROW_START = 3;
    static public final int COLUMN_START = 1;
    static public final int COLUMN_NUMBER = 7;

    private ArrayList<Brick> brickList = new ArrayList<>();
    private final int level;
    private int rowNumber;
    private int iNitro;
    private int jNitro;
    private int iSwitch;
    private int jSwitch;
    private Random random;
    private String creator;
    private String levelName;

    public Level(Context context, int level) {

        this.level = level;
        random = new Random();
        generateBricks(context);
    }

    public Level(ArrayList<Brick> brickList, String username, String levelName) {
        this.brickList = brickList;
        level = 10;
        this.creator = username;
        this.levelName = levelName;
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
                Brick b = new Brick(context, j * BRICK_HORIZONTAL_DISTANCE, i * BRICK_VERTICAL_DISTANCE, level, false);

                if (level >= 15) {
                    if (i == iNitro && j == jNitro) {
                        b.setNitroSkin();
                        b.createNitro();
                        b.setHard(false);
                    }
                    if (i == iSwitch && j == jSwitch) {
                        b.setSwitchSkin();
                        b.createSwitch();
                        b.setHard(false);
                    }
                }
                brickList.add(b);
            }
        }
    }

    protected ArrayList<Brick> getBrickList() {
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

    public String getLevelName() {
        return levelName;
    }

    public String getCreator() {
        return creator;
    }
}