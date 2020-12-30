package com.example.android.arkanoid;

import android.content.Context;
import android.graphics.Point;

import java.util.ArrayList;

public class Level {

    ArrayList<Brick> brickList = new ArrayList<>();
    //livello giocatore
    public Level(Context context) {
        generateBricks(context);
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
