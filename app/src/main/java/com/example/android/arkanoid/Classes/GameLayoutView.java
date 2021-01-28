package com.example.android.arkanoid.Classes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.android.arkanoid.R;

//This class manage the power ups buttons in game

public class GameLayoutView extends RelativeLayout {

    public Game game;
    private final ImageView ivFreeze;
    private final ImageView ivExplosiveBall;
    private final MediaPlayer freezeSound;

    public GameLayoutView(Context context, Game game) {
        super(context);
        this.game = game;
        this.addView(game);

        Bitmap freezeBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.freeze);
        Bitmap explosiveBallBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.explosion);

        freezeSound = MediaPlayer.create(context, R.raw.freeze);

        ivFreeze = new ImageView(context);
        ivFreeze.setImageBitmap(freezeBitmap);

        ivExplosiveBall = new ImageView(context);
        ivExplosiveBall.setImageBitmap(explosiveBallBitmap);

        LayoutParams params1 = new LayoutParams(180, 180);
        LayoutParams params2 = new LayoutParams(180, 180);

        params1.leftMargin = 50;
        params1.topMargin = 1800;
        params2.leftMargin = 350;
        params2.topMargin = 1800;

        setExplosiveBallOnClick();
        setFreezeOnClick();

        this.addView(ivExplosiveBall, params1);
        this.addView(ivFreeze, params2);
    }

    //manage the use of the explosive ball power up in a game
    private void setExplosiveBallOnClick() {
        ivExplosiveBall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = game.profile.getPowerUps().get(PowerUp.EXPLOSIVE_BALL).getQuantity();
                if (quantity > 0) {
                    game.explosiveBall = PowerUp.explosiveBall();
                    game.profile.getPowerUps().get(PowerUp.EXPLOSIVE_BALL).setQuantity(quantity - 1);
                    game.profile.updateProfile();
                }
            }
        });
    }

    //manage the use of the freeze power up in a game
    private void setFreezeOnClick() {
        ivFreeze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = game.profile.getPowerUps().get(PowerUp.FREEZE).getQuantity();
                if (quantity > 0) {
                    freezeSound.start();
                    float[] ballSpeed;
                    float xSpeed = game.ball.getXSpeed();
                    float ySpeed = game.ball.getYSpeed();

                    ballSpeed = PowerUp.freeze(xSpeed, ySpeed);
                    game.ball.setXSpeed(ballSpeed[0]);
                    game.ball.setYSpeed(ballSpeed[1]);
                    game.profile.getPowerUps().get(PowerUp.FREEZE).setQuantity(quantity - 1);
                    game.profile.updateProfile();
                    restoreSpeed(xSpeed, ySpeed);
                }
            }
        });
    }

    //restore the speed of the ball after use freeze power up
    private void restoreSpeed(final float x, final float y) {
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                freezeSound.stop();
                game.ball.setXSpeed(x);
                game.ball.setYSpeed(y);
            }
        }, 4000);
    }
}
