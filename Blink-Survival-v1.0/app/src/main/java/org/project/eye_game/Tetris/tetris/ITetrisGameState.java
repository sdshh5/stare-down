package org.project.eye_game.Tetris.tetris;

public interface ITetrisGameState {

    void addRemoveLineCount(int line);
    void ClearBoard();
    void update();
    void updateHighScore();
}
