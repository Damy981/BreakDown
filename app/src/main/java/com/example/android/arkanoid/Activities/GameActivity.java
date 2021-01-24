package com.example.android.arkanoid.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.arkanoid.Classes.Game;
import com.example.android.arkanoid.Classes.GameLayoutView;
import com.example.android.arkanoid.Classes.OnlineMatch;
import com.example.android.arkanoid.Classes.Profile;
import com.example.android.arkanoid.Classes.Services;
import com.example.android.arkanoid.Classes.UpdateThread;
import com.example.android.arkanoid.R;


public class GameActivity extends AppCompatActivity {

    private GameLayoutView gameLayout;
    private Game game;
    private Handler updateHandler;
    private float ballXSpeed;
    private float ballYSpeed;
    private MediaPlayer music;
    private boolean musicOn;
    private boolean accelerometerOn;
    public static final int LIVES = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Profile profile = (Profile) getIntent().getSerializableExtra("profile");
        OnlineMatch match;
        match = (OnlineMatch) getIntent().getSerializableExtra("match");
        Services services = new Services(getSharedPreferences(Services.SHARED_PREF_DIR, MODE_PRIVATE), profile.getUserId());
        musicOn = services.getMusicSetting();
        accelerometerOn = services.getAccelerometerSetting();
        // create a new game
        game = new Game(this, LIVES, 0, profile, services, match);
        gameLayout = new GameLayoutView(this, game);
        setContentView(gameLayout);

        music = MediaPlayer.create(getApplicationContext(), R.raw.game_background_theme);
        music.setLooping(true);
        if (musicOn)
            music.start();

        createHandler();
        UpdateThread myThread = new UpdateThread(updateHandler);
        myThread.start();
    }

    // create a handler and thread
    @SuppressLint("HandlerLeak")
    private void createHandler() {
        updateHandler = new Handler() {
            public void handleMessage(Message msg) {
                gameLayout.game.invalidate();
                if (!game.isMatchCompleted()){
                    gameLayout.game.update();
                }
                else {
                    View view = GameActivity.this.getLayoutInflater().inflate(R.layout.activity_game, null);
                    addContentView(view, new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    Button btnResume = view.findViewById(R.id.btnResume);
                    TextView tvEndMatchMessage = view.findViewById(R.id.tvEndMatchMessage);
                    tvEndMatchMessage.setVisibility(View.VISIBLE);
                    btnResume.setEnabled(false);
                }
                super.handleMessage(msg);
            }
        };
    }

    protected void onPause() {
        super.onPause();
        if (accelerometerOn)
            gameLayout.game.stopListener();
    }

    protected void onResume() {
        super.onResume();
        if (accelerometerOn)
            gameLayout.game.startListener();
    }

    @Override
    public void onBackPressed() {
        music.pause();
        ballXSpeed = gameLayout.game.ball.getXSpeed();
        ballYSpeed = gameLayout.game.ball.getYSpeed();
        gameLayout.game.ball.setXSpeed(0);
        gameLayout.game.ball.setYSpeed(0);
        setContentView(R.layout.activity_game);
    }

    public void resumeGame(View view) {
        if (musicOn)
            music.start();
        gameLayout.game.ball.setXSpeed(ballXSpeed);
        gameLayout.game.ball.setYSpeed(ballYSpeed);
        setContentView(gameLayout);
    }

    public void returnToMenu(View view) {
        music.stop();
        gameLayout.game.stopGame();
        finish();
    }
}