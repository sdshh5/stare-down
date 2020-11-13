package org.project.eye_game.Tetris.player;

import android.graphics.Canvas;

public abstract class PlayerUI {
    protected int screenWidth;
    protected int screenHeight;
    protected Player player;

    public  void registerPlayer(Player player) {
        this.player = player;
    }
    public abstract void onDraw(Canvas g);

    public void setScreenSize(int w, int h) {
        this.screenWidth = w;
        this.screenHeight = h;
    }
}
