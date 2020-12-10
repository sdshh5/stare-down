package org.project.eye_game.Tetris.tetris;

public class TetrisGameOverState extends TetrisGameState {
    public TetrisGameOverState(Tetris tetris) {
        this.tetris = tetris;
    }

    public boolean isGameOverState() {
        return true;
    }
}
