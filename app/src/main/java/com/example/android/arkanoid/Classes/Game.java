package com.example.android.arkanoid.Classes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.example.android.arkanoid.Activities.GameActivity;
import com.example.android.arkanoid.R;

import java.util.ArrayList;

//Class that contains methods to manage in-game events

public class Game extends View implements SensorEventListener, View.OnTouchListener {

    private Bitmap background;
    final  Bitmap ballBitmap;
    private Bitmap scaledBackground;
    private final Bitmap paddleBitmap;

    private Point size;
    final private Paint paint;

    public Ball ball;
    private ArrayList<Brick> brickList;
    private final Paddle paddle;
    private Level levelMap;
    public boolean explosiveBall;

    final private SensorManager sManager;
    final private Sensor accelerometer;

    private int lives;
    private int score;
    private int level;
    private boolean start;
    private boolean gameOver;
    final private Context context;
    public Profile profile;
    private final double dropRate;
    private final int paddleLength;

    private final MediaPlayer hitSound;
    private final MediaPlayer explosionSound;
    private final MediaPlayer hurtSound;
    private final Services services;
    private final ArrayList<Quest> quests;
    private final OnlineMatch match;
    private boolean matchCompleted = false; //this is used as a flag to check when user play all the 3 games in an online match
    private boolean isEditorLevel = false; //this is used as a flag to check if the user is playing a customized level
    private boolean editorLevelFinished = false; //this is used as a flag to make the game stop if the user win or loose a game in a customized level


    public Game(Context context, int lives, int score, Profile profile, Services services, OnlineMatch match, Level levelMap) {
        super(context);
        paint = new Paint();

        // set context, lives, scores and levels
        this.context = context;
        this.lives = lives;
        this.score = score;
        this.profile = profile;
        this.services = services;
        this.match = match;
        if(match != null) {
            level = 15;
        }
        else {
            level = this.profile.getLevelNumber();
        }
        dropRate = profile.getPowerUps().get(PowerUp.COINS_DROP_RATE).getQuantity() / 100.0;
        int START_PADDLE_LENGTH = 200;
        paddleLength = profile.getPowerUps().get(PowerUp.PADDLE_LENGTH).getQuantity() * 2 + START_PADDLE_LENGTH;
        explosiveBall = false;
        quests = profile.getQuestsList();

        hitSound = MediaPlayer.create(context, R.raw.hit);
        explosionSound = MediaPlayer.create(context, R.raw.explosion);
        hurtSound = MediaPlayer.create(context, R.raw.hurt);

        //start a gameOver to find out if the game is standing and if the player has lost
        start = false;
        gameOver = false;

        // creates an accelerometer and a SensorManager
        sManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        setBackground(context);

        // creates bitmaps
        ballBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ball);
        paddleBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.paddle);

        //creates a new ball, paddle, and list of bricks
        ball = new Ball(size.x / 2, size.y - 510, paddleLength);
        paddle = new Paddle(size.x / 2, size.y - 450);

        if (levelMap == null) {
            this.levelMap = new Level(context, level);
        }
        else {
            this.levelMap = levelMap;
            isEditorLevel = true;
        }
        brickList = this.levelMap.getBrickList();
        this.setOnTouchListener(this);
        ball.increaseSpeed(level);
    }

    //set background
    private void setBackground(Context context) {
        background = Bitmap.createBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.background_game));
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        size = new Point();
        display.getSize(size);
    }

    protected void onDraw(Canvas canvas) {
        // creates a background only once
        if (scaledBackground == null) {
            scaledBackground = Bitmap.createScaledBitmap(background, size.x, size.y, false);
        }
        canvas.drawBitmap(scaledBackground, 0, 0, paint);

        // draw the ball
        canvas.drawBitmap(ballBitmap, ball.getX(), ball.getY(), paint);

        // draw paddle
        RectF r = new RectF(paddle.getX(), paddle.getY(), paddle.getX() + paddleLength, paddle.getY() + 65);
        canvas.drawBitmap(paddleBitmap, null, r, paint);

        // draw bricks
        for (int i = 0; i < brickList.size(); i++) {
            Brick b = brickList.get(i);
            int BRICK_WIDTH = 123;
            int BRICK_HEIGHT = 66;
            r = new RectF(b.getX(), b.getY(), b.getX() + BRICK_WIDTH, b.getY() + BRICK_HEIGHT);
            canvas.drawBitmap(b.getBrick(), null, r, paint);
        }

        // draw text
        paint.setColor(Color.WHITE);
        paint.setTextSize(50);
        canvas.drawText(getContext().getString(R.string.level) + level, 100, 100, paint);
        canvas.drawText(getContext().getString(R.string.lives) + lives, 400, 100, paint);
        canvas.drawText(getContext().getString(R.string.scoreGame) + score, 700, 100, paint);

        //in case of lose draw "Game over!"
        if (gameOver) {
            profile.updateProfile();
            paint.setTextSize(100);
            canvas.drawText(getContext().getString(R.string.gameOver), size.x / 4, size.y / 2, paint);
        }
    }

    //check that the ball has not touched the edge
    private void checkEdges() {
        if (ball.getX() + ball.getXSpeed() >= size.x - 60) {
            ball.changeDirection("right");
        } else if (ball.getX() + ball.getXSpeed() <= 0) {
            ball.changeDirection("left");
        } else if (ball.getY() + ball.getYSpeed() <= 150) {
            ball.changeDirection("up");
        } else if (ball.getY() + ball.getYSpeed() >= size.y - 200) {
            checkLives();
        }
    }

    // checks the status of the game. whether my lives or whether the game is over
    private void checkLives() {
        if (lives == 1) {
            score -= 350;
            gameOver = true;
            start = false;
            if (match != null) {   //if user is playing an online match
                int i = match.getCounter();
                match.setPlayer1Score(score, i);
                match.increaseCounter();
                match.updateMatch();
                if(match.getCounter() == 3){
                    matchCompleted = true;
                }
            }
            if (isEditorLevel)
                editorLevelFinished = true;
            invalidate();
        }
        else {
            hurtSound.start();
            lives--;
            score -= 350;
            ball.setX(size.x / 2);
            ball.setY(size.y - 480);
            ball.generateSpeed();
            ball.increaseSpeed(level);
            start = false;
        }
    }

    // each step checks whether there is a collision, a lose or a win, etc.
    public void update() {
        if (start) {
            win();
            checkEdges();
            ball.touchPaddle(paddle.getX(), paddle.getY());
            for (int i = 0; i < brickList.size(); i++) {
                Brick b = brickList.get(i);
                if (ball.touchBrick(b.getX(), b.getY())) {
                    if (explosiveBall) {
                        explosionSound.start();
                        removeBricksExplosiveBall(b);
                        removeBrick(i);
                        explosiveBall = false;
                    }
                    else if (!b.isHardBrick() && !b.isSwitch()){
                        if (b.isNitro()) {
                            explosionSound.start();
                            if (lives == 1)
                                checkLives();
                            else
                                --lives;
                            hurtSound.start();
                        }
                        if (!b.isNitro())
                            hitSound.start();
                        removeBrick(i);
                    }
                    else if (b.isHardBrick()) {
                        hitSound.start();
                        b.setHard(false);
                        b.changeHardBrickColor();
                    }
                    else if (b.isSwitch()) {
                        hitSound.start();
                        b.setSwitchBrickOff();
                        removeBrick(i);
                        removeNitro();
                    }
                }
            }
            ball.moveBall();
            if (score > profile.getBestScore())
                profile.setBestScore(score);
        }
    }

    public void stopListener() {
        sManager.unregisterListener(this);
    }

    public void startListener() {
        sManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
    }

    //change accelerometer
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            paddle.setX(paddle.getX() - event.values[0] - event.values[0]);

            if (paddle.getX() + event.values[0] > size.x - 240) {
                paddle.setX(size.x - 240);
            } else if (paddle.getX() - event.values[0] <= 20) {
                paddle.setX(20);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    // serves to suspend the game in case of a new game
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (gameOver && !start) {
            score = 0;
            lives = 3;
            resetLevel();
            gameOver = false;

        } else {
            start = true;
        }
        return false;
    }

    // sets the game to start
    private void resetLevel() {
        profile.updateProfile();
        ball.setX(size.x / 2);
        ball.setY(size.y - 480);
        ball.generateSpeed();
        ball.increaseSpeed(level);
        levelMap = new Level(context, level);
        brickList = levelMap.getBrickList();
    }

    // find out if the player won or not
    private void win() {
        if (brickList.isEmpty()) {
            if (isEditorLevel)
                editorLevelFinished = true;
            if (match != null) {
                int i = match.getCounter();
                match.setPlayer1Score(score, i);
                match.increaseCounter();
                match.updateMatch();
                if(match.getCounter() == 3){
                    matchCompleted = true;
                }else {
                    resetLevel();
                }
            }
            else if (!isEditorLevel) {
                //set and save quests progress
                quests.get(Quest.QUEST_WIN_5).setProgress(quests.get(Quest.QUEST_WIN_5).getProgress() + 1);
                if(lives == GameActivity.LIVES){
                    quests.get(Quest.QUEST_WIN_3_WITH_ALL_LIVES).setProgress(quests.get(Quest.QUEST_WIN_3_WITH_ALL_LIVES).getProgress() + 1);
                }
                services.updateQuestsFile(context, quests);

                ++level;
                profile.increaseLevel();
                if (level <= 5)
                    profile.setCoins(profile.getCoins() + 50);
                if (level > 5 && level <= 15)
                    profile.setCoins(profile.getCoins() + 100);
                if (level > 15)
                    profile.setCoins(profile.getCoins() + 150);
                profile.updateProfile();
                resetLevel();
                ball.increaseSpeed(level);
                start = false;
            }
        }
    }

    public boolean onTouchEvent(MotionEvent event)
    {
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (event.getX() < 885)
                paddle.setX((float) event.getX());
        }
        return true;
    }

    public void stopGame() {
        start = false;
    }

    //remove brick from the bricklist (and from game)
    private void removeBrick(int i) {
        brickList.remove(i);
        score += 80;
        dropCoin();
        //set and save quests progress
        quests.get(Quest.QUEST_DESTROY_BRICKS_100).setProgress(quests.get(Quest.QUEST_DESTROY_BRICKS_100).getProgress() + 1);
        quests.get(Quest.QUEST_DESTROY_BRICKS_10000).setProgress(quests.get(Quest.QUEST_DESTROY_BRICKS_10000).getProgress() + 1);
        services.updateQuestsFile(context, quests);
    }

    //remove nitro bricks from the bricklist (and from game)
    private void removeNitro() {
        for (int i = 0; i < brickList.size(); i++) {
            Brick b = brickList.get(i);
            if (b.isNitro()) {
                explosionSound.start();
                score += 300;
                removeBrick(i);
                //set and save quests progress
                quests.get(Quest.QUEST_DEFUSE_NITROS).setProgress(quests.get(Quest.QUEST_DEFUSE_NITROS).getProgress() + 1);
                services.updateQuestsFile(context, quests);
            }
        }
    }

    //randomly increase the user's coins, according to the value of the stat "drop rate"
    private void dropCoin() {
        if (Math.random() < dropRate) {
            profile.setCoins(profile.getCoins() + 5);
        }
    }

    //remove the bricks around the one which is hit by an explosive ball
    private void removeBricksExplosiveBall (Brick b) {
        int i;
        float x = b.getX();
        float y = b.getY();
        float xBrickLeft = x - Level.BRICK_HORIZONTAL_DISTANCE;
        float xBrickRight = x + Level.BRICK_HORIZONTAL_DISTANCE;
        float yBrickUp = y - Level.BRICK_VERTICAL_DISTANCE;
        float yBrickDown = y + Level.BRICK_VERTICAL_DISTANCE;

        if((i = checkIfBrickExist(xBrickLeft, y)) != -1)
            removeBrick(i);
        if((i = checkIfBrickExist(xBrickRight, y)) != -1)
            removeBrick(i);
        if((i = checkIfBrickExist(x, yBrickUp)) != -1)
            removeBrick(i);
        if((i = checkIfBrickExist(x, yBrickDown)) != -1)
            removeBrick(i);
    }

    //if a brick exist, return its position in the bricklist, else return -1
    private int checkIfBrickExist(float x, float y) {
        for (int i = 0; i < brickList.size(); i++) {
            Brick b = brickList.get(i);
            if (b.getX() == x && b.getY() == y)
                return i;
        }
        return -1;
    }

    public boolean isMatchCompleted() {
        return matchCompleted;
    }

    public boolean isEditorLevelFinished() {
        return editorLevelFinished;
    }
}
