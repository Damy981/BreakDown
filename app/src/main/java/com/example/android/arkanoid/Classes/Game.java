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
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.example.android.arkanoid.R;


import java.util.ArrayList;

public class Game extends View implements SensorEventListener, View.OnTouchListener {

    private Bitmap background;
    final  Bitmap redBall;
    private Bitmap scaledBackground;
    final private Bitmap paddle_bitmap;

    private Display display;
    private Point size;
    final private Paint paint;

    public Ball ball;
    private ArrayList<Brick> brickList;
    private Paddle paddle;
    private Level levelMap;

    private RectF r;

    final private SensorManager sManager;
    final private Sensor accelerometer;

    private int lives;
    private int score;
    private int level;
    private boolean start;
    private boolean gameOver;
    final private Context context;
    private Profile profile;

    public Game(Context context, int lives, int score, Profile profile) {
        super(context);
        paint = new Paint();

        // set context, lives, scores and levels
        this.context = context;
        this.lives = lives;
        this.score = score;
        this.profile = profile;
        level = this.profile.getLevelNumber();
        //start a gameOver to find out if the game is standing and if the player has lost
        start = false;
        gameOver = false;

        // creates an accelerometer and a SensorManager
        sManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        setBackground(context);

        // creates a bitmap for the ball and paddle
        redBall = BitmapFactory.decodeResource(getResources(), R.drawable.redball);
        paddle_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.paddle);

        //creates a new ball, paddle, and list of bricks
        ball = new Ball(size.x / 2, size.y - 480);
        paddle = new Paddle(size.x / 2, size.y - 400);
        levelMap = new Level(context, level);
        brickList = levelMap.getBrickList();
        this.setOnTouchListener(this);
        ball.increaseSpeed(level);

    }

    //set background
    private void setBackground(Context context) {
        background = Bitmap.createBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.pozadie_score));
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
        paint.setColor(Color.RED);
        canvas.drawBitmap(redBall, ball.getX(), ball.getY(), paint);

        // draw paddle
        paint.setColor(Color.WHITE);
        r = new RectF(paddle.getX(), paddle.getY(), paddle.getX() + 200, paddle.getY() + 40);
        canvas.drawBitmap(paddle_bitmap, null, r, paint);

        // draw bricks
        paint.setColor(Color.GREEN);
        for (int i = 0; i < brickList.size(); i++) {
            Brick b = brickList.get(i);
            r = new RectF(b.getX(), b.getY(), b.getX() + 100, b.getY() + 80);
            canvas.drawBitmap(b.getBrick(), null, r, paint);
        }

        // draw text
        paint.setColor(Color.WHITE);
        paint.setTextSize(50);
        canvas.drawText("" + lives, 400, 100, paint);
        canvas.drawText("" + score, 700, 100, paint);

        //in case of lose draw "Game over!"
        if (gameOver) {
            paint.setColor(Color.RED);
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
                    brickList.remove(i);
                    score = score + 80;
                    profile.setCoins(profile.getCoins() + 1);
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
        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN: {
            }
            break;

            case MotionEvent.ACTION_MOVE:
            {
                paddle.setX((float)event.getX());

                invalidate();
            }

            break;
            case MotionEvent.ACTION_UP:

                paddle.setX((float)event.getX());
                invalidate();
                break;
        }
        return true;
    }

}
