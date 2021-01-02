package com.example.android.arkanoid.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.android.arkanoid.Classes.Game;
import com.example.android.arkanoid.Classes.UpdateThread;
import com.example.android.arkanoid.R;

public class GameActivity extends AppCompatActivity {

    private Game game;
    private UpdateThread myThread;
    private Handler updateHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // create a new game
        game = new Game(this, 3, 0);
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
        game.stopListener();
    }

    protected void onResume() {
        super.onResume();
        game.startListener();
    }
}