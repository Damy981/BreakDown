package com.example.android.arkanoid.Classes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.android.arkanoid.R;

public class GameLayoutView extends RelativeLayout {

    private Game game;
    ImageView ivFreeze;
    ImageView ivExplosiveBall;
    RelativeLayout.LayoutParams params1;
    RelativeLayout.LayoutParams params2;

    public GameLayoutView(Context context, Game game) {
        super(context);
        this.game = game;
        this.addView(game);

        Bitmap freezeBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.freeze);
        Bitmap explosiveBallBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.explosion);

        ivFreeze = new ImageView(context);
        ivFreeze.setImageBitmap(freezeBitmap);

        ivExplosiveBall = new ImageView(context);
        ivExplosiveBall.setImageBitmap(explosiveBallBitmap);

        params1 = new LayoutParams(180,180);
        params2 = new LayoutParams(180,180);

        params1.leftMargin = 10;
        params1.topMargin = 1800;
        params2.leftMargin = 220;
        params2.topMargin = 1800;

        setExplosiveBallOnClick();
        setFreezeOnClick();

        this.addView(ivExplosiveBall, params1);
        this.addView(ivFreeze, params2);
    }

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

    private void setFreezeOnClick() {
        ivFreeze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = game.profile.getPowerUps().get(PowerUp.FREEZE).getQuantity();
                if (quantity > 0) {
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

    private void restoreSpeed(final float x, final float y) {
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                game.ball.setXSpeed(x);
                game.ball.setYSpeed(y);
            }
        }, 4000);
    }
}
