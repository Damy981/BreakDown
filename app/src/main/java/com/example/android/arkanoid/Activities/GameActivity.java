package com.example.android.arkanoid.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.example.android.arkanoid.Classes.Game;
import com.example.android.arkanoid.Classes.GameLayoutView;
import com.example.android.arkanoid.Classes.Profile;
import com.example.android.arkanoid.Classes.UpdateThread;
import com.example.android.arkanoid.R;

public class GameActivity extends AppCompatActivity {

    private GameLayoutView gameLayout;
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
        gameLayout = new GameLayoutView(this, game);
        setContentView(gameLayout);

        createHandler();
        myThread = new UpdateThread(updateHandler);
        myThread.start();
    }

    // create a handler and thread
    @SuppressLint("HandlerLeak")
    private void createHandler() {
        updateHandler = new Handler() {
            public void handleMessage(Message msg) {
                gameLayout.game.invalidate();
                gameLayout.game.update();
                super.handleMessage(msg);
            }
        };
    }

    protected void onPause() {
        super.onPause();
        if (profile.isUsedAccelerometer())
            gameLayout.game.stopListener();
    }

    protected void onResume() {
        super.onResume();
        if (profile.isUsedAccelerometer())
            gameLayout.game.startListener();
    }

    @Override
    public void onBackPressed() {
        ballXSpeed = gameLayout.game.ball.getXSpeed();
        ballYSpeed = gameLayout.game.ball.getYSpeed();
        gameLayout.game.ball.setXSpeed(0);
        gameLayout.game.ball.setYSpeed(0);
        setContentView(R.layout.activity_game);
    }

    public void resumeGame(View view) {
        gameLayout.game.ball.setXSpeed(ballXSpeed);
        gameLayout.game.ball.setYSpeed(ballYSpeed);
        setContentView(gameLayout);
    }

    public void returnToMenu(View view) {
        gameLayout.game.stopGame();
        finish();
    }
}