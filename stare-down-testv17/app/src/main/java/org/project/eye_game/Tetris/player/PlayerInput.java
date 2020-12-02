package org.project.eye_game.Tetris.player;

public abstract class PlayerInput {
    protected Player player = null;

    protected int startX = 0;
    protected int startY = 0;
    protected final int BLOCK_IMAGE_SIZE = 60;

    public abstract boolean touch(int x, int y);

    public  void registerPlayer(Player player) {
        this.player = player;
    }

    protected void left() {
        player.MoveLeft();
    }

    protected void right() {
        player.MoveRight();
    }

    protected void down() {
        player.MoveDown();
    }

    protected void rotate() {
        player.rotate();
    }

    protected void bottom() { player.MoveBottom();}

    protected void play() { player.play(); }

    protected void clickStartButton() { player.clickStartButton(); }
}
