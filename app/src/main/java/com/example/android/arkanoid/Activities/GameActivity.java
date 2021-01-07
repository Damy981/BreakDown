package com.example.android.arkanoid.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.example.android.arkanoid.Classes.Game;
import com.example.android.arkanoid.Classes.Profile;
import com.example.android.arkanoid.Classes.UpdateThread;
import com.example.android.arkanoid.R;

public class GameActivity extends AppCompatActivity {

    private Game game;
    private UpdateThread myThread;
    private Handler updateHandler;
    private Profile profile;
    private float ballXSpeed;
    private float ballYSpeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profile = (Profile) getIntent().getSerializableExtra("profile");
        // create a new game
        game = new Game(this, 3, 0, profile);
        setContentView(game);
        createHandler();
        myThread = new UpdateThread(updateHandler);
        myThread.start();
    }
    // create a handler and thread
    @SuppressLint("HandlerLeak")
    private void createHandler() {
        updateHandler = new Handler() {
            public void handleMessage(Message msg) {
                game.invalidate();
                game.update();
                super.handleMessage(msg);
            }
        };
    }

    protected void onPause() {
        super.onPause();
        if (profile.isUsedAccelerometer())
            game.stopListener();
    }

    protected void onResume() {
        super.onResume();
        if (profile.isUsedAccelerometer())
            game.startListener();
    }

    @Override
    public void onBackPressed() {
        ballXSpeed = game.ball.getXSpeed();
        ballYSpeed = game.ball.getYSpeed();
        game.ball.setXSpeed(0);
        game.ball.setYSpeed(0);
        setContentView(R.layout.activity_game);
    }

    public void resumeGame(View view) {
        game.ball.setXSpeed(ballXSpeed);
        game.ball.setYSpeed(ballYSpeed);
        setContentView(game);
    }

    public void returnToMenu(View view) {
        game.stopGame();
        finish();
    }
}