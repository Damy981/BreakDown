package com.example.android.arkanoid.Classes;

public class Ball {

    protected float xSpeed;
    protected float ySpeed;
    private float x;
    private float y;

    public Ball(float x, float y) {
        this.x = x;
        this.y = y;
        generateSpeed();
    }

    // creates a random velocity ball
    protected void generateSpeed() {
        int maxX = 13;
        int minX = 7;
        int maxY = -17;
        int minY = -23;
        int rangeX = maxX - minX + 1;
        int rangeY = maxY - minY + 1;

        xSpeed = (int) (Math.random() * rangeX) + minX;
        ySpeed = (int) (Math.random() * rangeY) + minY;
    }

    // changes direction according to speed
    protected void changeDirection() {
        if (xSpeed > 0 && ySpeed < 0) {
            invertXSpeed();
        } else if (xSpeed < 0 && ySpeed < 0) {
            invertYSpeed();
        } else if (xSpeed < 0 && ySpeed > 0) {
            invertXSpeed();
        } else if (xSpeed > 0 && ySpeed > 0) {
            invertYSpeed();
        }
    }

    // increase speed based on level
    protected void increaseSpeed(int level) {
        xSpeed = xSpeed + (1 * level);
        ySpeed = ySpeed - (1 * level);
    }

    // changes direction and speed depending on which wall it touched
    protected void changeDirection(String wall) {
        if (xSpeed > 0 && ySpeed < 0 && wall.equals("right")) {
            invertXSpeed();
        } else if (xSpeed > 0 && ySpeed < 0 && wall.equals("up")) {
            invertYSpeed();
        } else if (xSpeed < 0 && ySpeed < 0 && wall.equals("up")) {
            invertYSpeed();
        } else if (xSpeed < 0 && ySpeed < 0 && wall.equals("left")) {
            invertXSpeed();
        } else if (xSpeed < 0 && ySpeed > 0 && wall.equals("left")) {
            invertXSpeed();
        } //else if (xSpeed > 0 && ySpeed > 0 && wall.equals("down")) {
       //     invertYSpeed(); }
         else if (xSpeed > 0 && ySpeed > 0 && wall.equals("right")) {
            invertXSpeed();
        }
    }

    // check if the ball is close to paddle
    private boolean isCloseToPaddle(float ax, float ay, float bx, float by) {
        bx += 12;
        by += 11;
        if ((Math.sqrt(Math.pow((ax + 50) - bx, 2) + Math.pow(ay - by, 2))) < 80) {
            return true;
        } else if ((Math.sqrt(Math.pow((ax + 100) - bx, 2) + Math.pow(ay - by, 2))) < 60) {
            return true;
        } else if ((Math.sqrt(Math.pow((ax + 150) - bx, 2) + Math.pow(ay - by, 2))) < 60) {
            return true;
        }
        return false;
    }

    // check if the ball is close to a brick
    private boolean isCloseToBrick(float ax, float ay, float bx, float by) {
        bx += 12;
        by += 11;
        double d = Math.sqrt(Math.pow((ax + 50) - bx, 2) + Math.pow((ay + 40) - by, 2));
        return d < 80;
    }

    // if the ball collides with the paddle, it changes direction
    protected void touchPaddle(float xPaddle, float yPaddle) {
        if (isCloseToPaddle(xPaddle, yPaddle, getX(), getY()))
            changeDirection();
    }

    // if the ball collides with a brick, it changes direction
    protected boolean touchBrick(float xBrick, float yBrick) {
        if (isCloseToBrick(xBrick, yBrick, getX(), getY())) {
            changeDirection();
            return true;
        } else return false;
    }

    //moves by the specified speed
    protected void moveBall() {
        x = x + xSpeed;
        y = y + ySpeed;
    }

    public void invertXSpeed() {
        xSpeed = -xSpeed;
    }

    public void invertYSpeed() {
        ySpeed = -ySpeed;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setXSpeed(float xSpeed) {
        this.xSpeed = xSpeed;
    }

    public void setYSpeed(float ySpeed) {
        this.ySpeed = ySpeed;
    }

    public float getXSpeed() {
        return xSpeed;
    }

    public float getYSpeed() {
        return ySpeed;
    }
}
