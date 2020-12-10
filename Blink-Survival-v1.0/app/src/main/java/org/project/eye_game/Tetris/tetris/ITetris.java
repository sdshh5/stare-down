package org.project.eye_game.Tetris.tetris;

public interface ITetris {
    void init();

    void moveLeft();
    void moveRight();
    void moveDown();
    void rotate();
    void moveBottom();

    void play();
    void pause();
    void resume();

    int getWidth();
    int getHeight();

    void register(ITetrisObserver observer);

    int[][] getBoard();

    int getScore();
    int getHighScore();

    boolean setScore(Score score);

    int getRemovedLineCount();

    Tetrominos getCurrentBlock();
    Tetrominos getNextBlock();
    Tetrominos getShadowBlock();

    boolean isIdleState();
    boolean isGameOverState();
    boolean isPlayState();
    boolean isPauseState();

    boolean isEnableShadow();
    void enableShadow();
    void disableShadow();
}
