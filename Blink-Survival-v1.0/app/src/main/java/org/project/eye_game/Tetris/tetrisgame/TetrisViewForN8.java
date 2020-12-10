package org.project.eye_game.Tetris.tetrisgame;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import org.project.eye_game.Tetris.player.Player;
import org.project.eye_game.Tetris.player.PlayerInput;
import org.project.eye_game.Tetris.player.PlayerObserver;
import org.project.eye_game.Tetris.player.PlayerUI;
import org.project.eye_game.Tetris.tetris.Score;

import static android.content.Context.MODE_PRIVATE;

public class TetrisViewForN8 extends View implements PlayerObserver {
    private String LOG_TAG = this.getClass().getName();
    private Context mContext;
    private Player player;
    private PlayerInput playerInput;
    private PlayerUI playerUI;
    private Score playerScore;

    private int highScore = 0;
    private float scaleX = 1.0f;
    private float scaleY = 1.0f;
    private boolean isSetScale = false;
    private boolean needScaleCanvas = false;

    private static final int EMPTY_MESSAGE = 0;
    private HandlerThread playerHandlerThread;
    private Handler playerHandler;
    private BoardProfile profile;

    public TetrisViewForN8(Context context, Player player, BoardProfile profile) {
        super(context);
        this.mContext = context;
        this.profile=profile;
        isSetScale = false;
        needScaleCanvas = false;

        createPlayerThread();

        //loadHIghScore();

        this.player = player;
        playerInput = new PlayerInputImplForN8(profile);
        playerUI = new PlayerUIForN8(mContext, profile);
        playerScore = new PlayerScoreImpl();

        playerScore.setHighScore(this.highScore);
        player.setInputDevice(playerInput);
        player.setView(playerUI);
        player.setSCore(playerScore);
        player.register(this);
        player.init();
    }

    private void createPlayerThread() {
        Log.d(LOG_TAG,"createPlayerThread");
        playerHandlerThread = new HandlerThread("Player Processing Thread");
        playerHandlerThread.start();
        playerHandler = new Handler(playerHandlerThread.getLooper()){
            @Override
            public void handleMessage(Message msg){
                if (player != null && player.isPlayState()) {
                    player.MoveDown();
                    int gameSpeed = 700 - (player.getScore() / 10000);
                    if (playerHandler.hasMessages(EMPTY_MESSAGE)) {
                        playerHandler.removeMessages(EMPTY_MESSAGE);
                    }
                    playerHandler.sendEmptyMessageDelayed(EMPTY_MESSAGE, gameSpeed);
                }
            }
        };
    }


    public void setScreenSize(int w, int h) {
        playerUI.setScreenSize(w, h);
    }

    public void startGame() {
        playerHandler.sendEmptyMessage(EMPTY_MESSAGE);
    }

    public void pauseGame() {
        Log.d(LOG_TAG, "pauseGame");
        if (playerHandler.hasMessages(EMPTY_MESSAGE)) {
            playerHandler.removeMessages(EMPTY_MESSAGE);
            Log.d(LOG_TAG, "Removed event");
        }
        if (player != null) {
            player.pause();
        }

        if (playerHandlerThread != null) {
            playerHandlerThread.quit();
        }
        //saveScore();
    }

    public void resumeGame() {
        Log.d(LOG_TAG, "resumeGame");
        createPlayerThread();
    }

    public void update() {
        Log.d(LOG_TAG, "View.update()");
        invalidate();
    }

    public void onDraw(Canvas canvas) {
        if (playerUI == null) {
            return;
        }
/*
        if (!isSetScale) {
            scaleX = canvas.getWidth() / 1080f;
            scaleY = canvas.getHeight() / 1920f;
            isSetScale = true;

            if (scaleX <= 0.999f) {
                needScaleCanvas = true;
                Log.d(LOG_TAG, "Resolution of device is smaller than 1080");
            }
        }

        if (needScaleCanvas) {
            canvas.scale(scaleX, scaleY);
        }
*/
        playerUI.onDraw(canvas);
    }


    public boolean onTouchEvent(MotionEvent event) {
        Log.d(LOG_TAG, ">> X: " + event.getX() + " Y: " + event.getY());

        if (playerInput == null) {
            return false;
        }
        if (MotionEvent.ACTION_DOWN != event.getAction()) {
            return false;
        }

        Log.d(LOG_TAG, ">> scaleX: " + scaleX + " scaleY: " + scaleY);

        int x = (int) (event.getX());
        int y = (int) (event.getY());
/*
        if (needScaleCanvas) {
            x = (int) (x / scaleX);
            y = (int) (y / scaleY);
        }
*/
        Log.d(LOG_TAG, ">> X: " + x + " Y: " + y);
        return playerInput.touch(x, y);
    }

    public void loadHIghScore(int sc) {
        this.highScore = sc;
        playerScore.setHighScore(this.highScore);

//        Log.d(LOG_TAG, "load()");
//        SharedPreferences pref = mContext.getSharedPreferences("choboTetris", MODE_PRIVATE);
//        this.highScore = pref.getInt("highscore", 0);
    }

    public int saveScore() {
        if(this.highScore > player.getHighScore()){
            return this.highScore;
        }
        else{
            return player.getHighScore();
        }
//        Log.d(LOG_TAG, "saveScore()");
//        if (this.highScore > player.getHighScore()) {
//            return;
//        }
//        SharedPreferences pref = mContext.getSharedPreferences("choboTetris", MODE_PRIVATE);
//        SharedPreferences.Editor edit = pref.edit();
//
//        edit.putInt("highscore", player.getHighScore());
//        edit.commit();
    }

}
