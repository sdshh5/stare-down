package org.project.eye_game.Tetris.tetrisgame;

import android.util.Log;

import org.project.eye_game.Tetris.player.PlayerInput;

public class PlayerInputImplForN8 extends PlayerInput {
    private final String TAG = this.getClass().getName();

    public PlayerInputImplForN8() {
        startX = 80;
        startY = 80;
    }
    //가장 밑으로 내려가는 bottom을 호출하는 함수를 선언
    public void downCall(){
        Log.d(TAG, "bottom() down()");
        bottom();
    }
    public void rightCall(){
        Log.d(TAG, "right()");
        right();
    }

    public void leftCall(){
        Log.d(TAG, "left()");
        left();
    }

    public boolean touch(int touchX, int touchY) {
        if (player == null) {
            return true;
        }

        if ((touchX > 190) && (touchY > 400)
                && (touchX < 500) && (touchY < 500)) {
            Log.d(TAG, "touch: ");
            clickStartButton();
            return true;
        }


        if ((touchX > 700) && (touchY > 50)
                && (touchY < 250)) {
            Log.d(TAG, "Round button: play()");
            play();
            return true;
        }

        final int BOARD_HEIGHT = 20;

        if (touchX > startX + 750 &&
                touchY > startY + BLOCK_IMAGE_SIZE * BOARD_HEIGHT + 100 &&
                touchX < startX + 950 &&
                touchY < startY + BLOCK_IMAGE_SIZE * BOARD_HEIGHT + 100 + 200) {
            Log.d(TAG, "rotate()");
            rotate();
            return true;
        }

        if (touchX > startX &&
                touchY > startY + BLOCK_IMAGE_SIZE * BOARD_HEIGHT + 100 &&
                touchX < startX + 200 &&
                touchY < startY + BLOCK_IMAGE_SIZE * BOARD_HEIGHT + 100 + 200) {
            Log.d(TAG, "left()");
            left();
            return true;
        }

        if (touchX > startX + 250 &&
                touchY > startY + BLOCK_IMAGE_SIZE * BOARD_HEIGHT + 100 &&
                touchX < startX + 450 &&
                touchY < startY + BLOCK_IMAGE_SIZE * BOARD_HEIGHT + 100 + 200) {
            Log.d(TAG, "bottom() down()");
            bottom();
            down();
            return true;
        }

        if (touchX > startX + 500 &&
                touchY > startY + BLOCK_IMAGE_SIZE * BOARD_HEIGHT + 100 &&
                touchX < startX + 700 &&
                touchY < startY + BLOCK_IMAGE_SIZE * BOARD_HEIGHT + 100 + 200) {
            Log.d(TAG, "right()");
            right();
            return true;
        }

        if (touchX > startX + 750 &&
                touchY > startY + BLOCK_IMAGE_SIZE * BOARD_HEIGHT - 200 &&
                touchX < startX + 950 &&
                touchY < startY + BLOCK_IMAGE_SIZE * BOARD_HEIGHT) {
            Log.d(TAG, "down()");
            down();
            return true;
        }

        return false;
    }
}
