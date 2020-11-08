package org.project.eye_game.Tetris.tetrisgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import com.chobocho.player.PlayerUI;
import com.chobocho.tetris.R;
import com.chobocho.tetris.Tetris;
import com.chobocho.tetris.Tetrominos;

public class PlayerUIForN8 extends PlayerUI {
    private Context mConext;
    private final int BLOCK_IMAGE_SIZE = 60;
    private final int N8_width = 1080;
    private final int N8_height = 1920;
    public static final int BOARD_WIDTH = 10;
    public static final int BOARD_HEIGHT = 20;

    Paint mPaint;

    Bitmap mGameBack;
    Bitmap mGameStart;
    Bitmap mGameOver;

    Bitmap leftArrow;
    Bitmap rightArrow;
    Bitmap downArrow;
    Bitmap bottomArrow;
    Bitmap rotateArrow;
    Bitmap playBtn;
    Bitmap pauseBtn;

    Bitmap[] mTile   = new Bitmap[10];

    boolean isLoadedImage = false;

    public PlayerUIForN8(Context context) {
        mConext = context;
        init();
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (!isLoadedImage) {
            return;
        }

        int i = 0;
        int j = 0;
        int startX = 80;
        int startY = 80;

        int width = player.getWidth();
        int height = player.getHeight();

        canvas.drawBitmap(mGameBack, null, new Rect(0, 0, N8_width, N8_height), null);

        Paint paint = new Paint();
        paint.setAlpha(128);

        int[][] m_Board = player.getBoard();

        // Draw board
        for (i = 0; i < width; i++) {
            for (j = 0; j < height; j++) {

                if (Tetris.EMPTY == m_Board[j][i]) {
                    canvas.drawBitmap(mTile[m_Board[j][i]], null,
                            new Rect(i * BLOCK_IMAGE_SIZE + startX,
                                    j * BLOCK_IMAGE_SIZE + startY,
                                    i * BLOCK_IMAGE_SIZE + startX + BLOCK_IMAGE_SIZE,
                                    j * BLOCK_IMAGE_SIZE + startY + BLOCK_IMAGE_SIZE), paint);
                } else {
                    canvas.drawBitmap(mTile[m_Board[j][i]], null,
                            new Rect(i * BLOCK_IMAGE_SIZE + startX,
                                    j * BLOCK_IMAGE_SIZE + startY,
                                    i * BLOCK_IMAGE_SIZE + startX + BLOCK_IMAGE_SIZE,
                                    j * BLOCK_IMAGE_SIZE + startY + BLOCK_IMAGE_SIZE), null);
                }
            }
        }
        mPaint.setTextSize(BLOCK_IMAGE_SIZE);

        canvas.drawText("Score", 760, 700, mPaint);
        canvas.drawText(Integer.toString(player.getScore()), 760, 760, mPaint);
        canvas.drawText("Line", 760, 820, mPaint);
        canvas.drawText(Integer.toString(player.getRemovedLineCount()), 760, 880, mPaint);
        canvas.drawText("High Score : " + Integer.toString(player.getHighScore()), startX, startY + 1620, mPaint);


        canvas.drawBitmap(leftArrow, null,
                new Rect(startX,
                        startY + BLOCK_IMAGE_SIZE * BOARD_HEIGHT + 100,
                        startX+200,
                        startY + BLOCK_IMAGE_SIZE * BOARD_HEIGHT + 100 + 200), null);
        canvas.drawBitmap(bottomArrow, null,
                new Rect(startX+250,
                        startY + BLOCK_IMAGE_SIZE * BOARD_HEIGHT + 100,
                        startX+450,
                        startY + BLOCK_IMAGE_SIZE * BOARD_HEIGHT + 100 + 200), null);
        canvas.drawBitmap(rightArrow, null,
                new Rect(startX+500,
                        startY + BLOCK_IMAGE_SIZE * BOARD_HEIGHT + 100,
                        startX+700,
                        startY + BLOCK_IMAGE_SIZE * BOARD_HEIGHT + 100 + 200), null);
        canvas.drawBitmap(rotateArrow, null,
                new Rect(startX+750,
                        startY + BLOCK_IMAGE_SIZE * BOARD_HEIGHT + 100,
                        startX+950,
                        startY + BLOCK_IMAGE_SIZE * BOARD_HEIGHT + 100 + 200), null);

        canvas.drawBitmap(downArrow, null,
                new Rect(startX+750,
                        startY + BLOCK_IMAGE_SIZE * BOARD_HEIGHT - 200,
                        startX+950,
                        startY + BLOCK_IMAGE_SIZE * BOARD_HEIGHT), null);

        if (player == null) {
            Log.d("Tetris", "Tetris is null");
            return;
        }

        if (player.isIdleState()) {
            canvas.drawBitmap(mGameStart, 190, 400, null);

            canvas.drawBitmap(playBtn, null,
                    new Rect(startX + BLOCK_IMAGE_SIZE * BOARD_WIDTH + 100,
                            startY,
                            startX + BLOCK_IMAGE_SIZE * BOARD_WIDTH + 100 + 200,
                            startY + 200), null);
        } else if (player.isGameOverState()) {
            canvas.drawBitmap(mGameOver, 190, 400, null);

            canvas.drawBitmap(playBtn, null,
                    new Rect(startX + BLOCK_IMAGE_SIZE * BOARD_WIDTH + 100,
                            startY,
                            startX + BLOCK_IMAGE_SIZE * BOARD_WIDTH + 100 + 200,
                            startY + 200), null);
        } else if (player.isPlayState()) {

            if (player.isEnableShadow()) {
                Tetrominos sblock = player.getShadowBlock();
                onDrawBlock(canvas, sblock, startX, startY, paint);
            }

            Tetrominos block = player.getCurrentBlock();
            onDrawBlock(canvas, block, startX, startY);

            Tetrominos nextTetrominos = player.getNextBlock();
            int nStartX = 600;
            int nStartY = startY + 5 * BLOCK_IMAGE_SIZE;
            onDrawBlock(canvas, nextTetrominos, nStartX, nStartY);

            canvas.drawBitmap(pauseBtn, null,
                    new Rect(startX + BLOCK_IMAGE_SIZE * BOARD_WIDTH + 100,
                            startY,
                            startX + BLOCK_IMAGE_SIZE * BOARD_WIDTH + 100 + 200,
                            startY + 200), null);
        } else if (player.isPauseState()) {
            canvas.drawBitmap(mGameStart, 190, 400, null);
            canvas.drawBitmap(playBtn, null,
                    new Rect(startX + BLOCK_IMAGE_SIZE * BOARD_WIDTH + 100,
                            startY,
                            startX + BLOCK_IMAGE_SIZE * BOARD_WIDTH + 100 + 200,
                            startY + 200), null);

            mPaint.setTextSize(30);
            canvas.drawText("[" + screenWidth + "x" + screenHeight +"]", 800, 1700, mPaint);
        }
    }

    private void init() {
        loadImage();

        mPaint = new Paint();
        mPaint.setColor(Color.BLUE);
        mPaint.setStrokeWidth(3);
        mPaint.setAntiAlias(true);

        isLoadedImage = true;
    }

    private void loadImage() {
        mGameBack = BitmapFactory.decodeResource(mConext.getResources(),
                R.drawable.backimage);
        mGameStart = BitmapFactory.decodeResource(mConext.getResources(),
                R.drawable.start);
        mGameOver = BitmapFactory.decodeResource(mConext.getResources(),
                R.drawable.gameover);

        mTile[0] =  BitmapFactory.decodeResource(mConext.getResources(),
                R.drawable.black);
        mTile[1] = BitmapFactory.decodeResource(mConext.getResources(),
                R.drawable.yellow);
        mTile[2] = BitmapFactory.decodeResource(mConext.getResources(),
                R.drawable.blue);
        mTile[3] = BitmapFactory.decodeResource(mConext.getResources(),
                R.drawable.red);
        mTile[4] = BitmapFactory.decodeResource(mConext.getResources(),
                R.drawable.gray);
        mTile[5] = BitmapFactory.decodeResource(mConext.getResources(),
                R.drawable.green);
        mTile[6] = BitmapFactory.decodeResource(mConext.getResources(),
                R.drawable.magenta);
        mTile[7] = BitmapFactory.decodeResource(mConext.getResources(),
                R.drawable.orange);
        mTile[8] = BitmapFactory.decodeResource(mConext.getResources(),
                R.drawable.cyan);

        leftArrow = BitmapFactory.decodeResource(mConext.getResources(),
                R.drawable.left);
        rightArrow = BitmapFactory.decodeResource(mConext.getResources(),
                R.drawable.right);
        downArrow  = BitmapFactory.decodeResource(mConext.getResources(),
                R.drawable.down);
        rotateArrow = BitmapFactory.decodeResource(mConext.getResources(),
                R.drawable.rotate);
        playBtn = BitmapFactory.decodeResource(mConext.getResources(),
                R.drawable.play);
        pauseBtn = BitmapFactory.decodeResource(mConext.getResources(),
                R.drawable.pause);
        bottomArrow =  BitmapFactory.decodeResource(mConext.getResources(),
                R.drawable.bottom);
    }

    public void onDrawBlock(Canvas canvas, Tetrominos block, int startX, int startY) {
        onDrawBlock(canvas, block, startX, startY, null);
    }

    public void onDrawBlock(Canvas canvas, Tetrominos block, int startX, int startY, Paint paint) {
        int [][]m_block = block.getBlock();

        int i = 0, j = 0;
        int w = block.getWidth();
        int h = block.getHeight();
        int x = block.getX();
        int y = block.getY();
        int type = block.getType();

        for (i = 0; i < w; i++) {
            for (j = 0; j < h; j++) {
                if (m_block[j][i] != Tetris.EMPTY) {
                    canvas.drawBitmap(mTile[type], null,
                            new Rect((x + i) * BLOCK_IMAGE_SIZE + startX,
                                    (y + j) * BLOCK_IMAGE_SIZE + startY,
                                    (x + i + 1) * BLOCK_IMAGE_SIZE + startX,
                                    (y + j+ 1) * BLOCK_IMAGE_SIZE + startY),
                            paint);
                }
            }
        }
    }

}
