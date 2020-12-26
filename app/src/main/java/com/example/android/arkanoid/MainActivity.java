package com.example.android.arkanoid;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private Game game;
    private UpdateThread myThread;
    private Handler updateHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // sets the screen orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //show splashScreen
        Intent intent = new Intent(this, SplashScreenActivity.class);
        startActivity(intent);

        // create a new game
     /*   game = new Game(this, 3, 0);
        setContentView(game);

        // create a handler and thread
        createHandler();
        myThread = new UpdateThread(updateHandler);
        myThread.start();
    }

    @SuppressLint("HandlerLeak")
    private void createHandler() {
        updateHandler = new Handler() {
            public void handleMessage(Message msg) {
                game.invalidate();
                game.update();
                super.handleMessage(msg);
            }
        }; */
    }
/*
    protected void onPause() {
        super.onPause();
        game.stopListener();
    }

    protected void onResume() {
        super.onResume();
        game.startListener();
    }
*/
}
