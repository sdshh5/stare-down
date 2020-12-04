package org.project.eye_game.Tetris.tetris;

public class TetrisPauseState extends TetrisGameState {
    public TetrisPauseState(Tetris tetris) {
        this.tetris = tetris;
    }

    public boolean isPauseState() { return true; }
}
