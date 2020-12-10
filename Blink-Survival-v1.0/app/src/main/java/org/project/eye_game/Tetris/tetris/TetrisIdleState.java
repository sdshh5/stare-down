package org.project.eye_game.Tetris.tetris;

public class TetrisIdleState extends TetrisGameState {

    public TetrisIdleState(Tetris tetris) {
        TetrisLog.d("TetrisIdleState()");
        this.tetris = tetris;
    }
    public boolean isIdleState() {
        return true;
    }
}
