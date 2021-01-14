package com.example.android.arkanoid.Classes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
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

import com.example.android.arkanoid.R;


import java.util.ArrayList;

public class Game extends View implements SensorEventListener, View.OnTouchListener {

    private final int BRICK_WIDTH = 123;
    private final int BRICK_HEIGHT = 66;
    private final int START_PADDLE_LENGTH = 200;

    private Bitmap background;
    final  Bitmap ballBitmap;
    private Bitmap scaledBackground;
    private final Bitmap paddleBitmap;

    private Display display;
    private Point size;
    final private Paint paint;

    public Ball ball;
    private ArrayList<Brick> brickList;
    private Paddle paddle;
    private Level levelMap;
    public boolean explosiveBall;

    private RectF r;

    final private SensorManager sManager;
    final private Sensor accelerometer;

    private int lives;
    private int score;
    private int level;
    private boolean start;
    private boolean gameOver;
    final private Context context;
    public Profile profile;
    private double dropRate;
    private int paddleLength;

    private MediaPlayer hitSound;
    private MediaPlayer explosionSound;
    private MediaPlayer hurtSound;

    public Game(Context context, int lives, int score, Profile profile) {
        super(context);
        paint = new Paint();

        // set context, lives, scores and levels
        this.context = context;
        this.lives = lives;
        this.score = score;
        this.profile = profile;
        level = this.profile.getLevelNumber();
        dropRate = profile.getPowerUps().get(PowerUp.COINS_DROP_RATE).getQuantity() / 100.0;
        paddleLength = profile.getPowerUps().get(PowerUp.PADDLE_LENGTH).getQuantity() + START_PADDLE_LENGTH;
        explosiveBall = false;

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
        ball = new Ball(size.x / 2, size.y - 460);
        paddle = new Paddle(size.x / 2, size.y - 400);
        levelMap = new Level(context, level);
        brickList = levelMap.getBrickList();
        this.setOnTouchListener(this);
        ball.increaseSpeed(level);
    }

    //set background
    private void setBackground(Context context) {
        background = Bitmap.createBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.background));
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        display = wm.getDefaultDisplay();
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
        r = new RectF(paddle.getX(), paddle.getY(), paddle.getX() + paddleLength, paddle.getY() + 65);
        canvas.drawBitmap(paddleBitmap, null, r, paint);

        // draw bricks
        for (int i = 0; i < brickList.size(); i++) {
            Brick b = brickList.get(i);
            r = new RectF(b.getX(), b.getY(), b.getX() + BRICK_WIDTH, b.getY() + BRICK_HEIGHT);
            canvas.drawBitmap(b.getBrick(), null, r, paint);
        }

        // draw text
        paint.setTextSize(50);
        canvas.drawText("Level: " + level, 100, 100, paint);
        canvas.drawText("Lives: " + lives, 400, 100, paint);
        canvas.drawText("Score: " + score, 700, 100, paint);

        //in case of lose draw "Game over!"
        if (gameOver) {
            paint.setTextSize(100);
            canvas.drawText("Game over!", size.x / 4, size.y / 2, paint);
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
            gameOver = true;
            start = false;
            invalidate();
        } else {
            hurtSound.start();
            lives--;
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
                        // explosiveBall = false;
                    }
                    else if (!b.isHardBrick() && !b.isSwitch()){
                        if (b.isNitro()) {
                            explosionSound.start();
                            lives --;
                            hurtSound.start();
                        }
                        if (!b.isNitro())
                            hitSound.start();
                        removeBrick(i);
                        dropCoin();
                    }
                    else if (b.isHardBrick()) {
                        hitSound.start();
                        b.setHardFalse();
                        b.changeHardBrickColor();
                    }
                    else if (b.isSwitch()) {
                        hitSound.start();
                        removeNitro();
                        b.setSwitchBrickOff();
                        removeBrick(i);
                    }
                }
            }
            ball.moveBall();
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
        levelMap = new Level(context, level);
        brickList = levelMap.getBrickList();
    }

    // find out if the player won or not
    private void win() {
        if (brickList.isEmpty()) {
            ++level;
            profile.increaseLevel();
            resetLevel();
            ball.increaseSpeed(level);
            start = false;
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

    private void removeBrick(int i) {
        brickList.remove(i);
        score = score + 80;
    }

    private void removeNitro() {
        for (int i = 0; i < brickList.size(); i++) {
            Brick b = brickList.get(i);
            if (b.isNitro()) {
                explosionSound.start();
                removeBrick(i);
            }
        }
    }

    private void dropCoin() {
        if (Math.random() < dropRate) {
            Log.i("cacca", "soldiiii");
            profile.setCoins(profile.getCoins() + 1);
        }
    }

    private void removeBricksExplosiveBall (Brick b) {
        float x = b.getX();
        float y = b.getY();

        for (int i = 0; i < brickList.size(); i++) {
            Brick b1 = brickList.get(i);
            if(b1.getX() == x + Level.BRICK_HORIZONTAL_DISTANCE)
                removeBrick(i);
        }
    }
}
