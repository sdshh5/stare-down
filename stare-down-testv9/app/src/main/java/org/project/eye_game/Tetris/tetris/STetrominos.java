package org.project.eye_game.Tetris.tetris;

public class STetrominos extends ITetrominos {
    public STetrominos() {
        this.block = new int[][][]{
                {
                        {0, 1, 1},
                        {1, 1, 0},
                        {0, 0, 0},
                },
                {
                        {1, 0, 0},
                        {1, 1, 0},
                        {0, 1, 0},
                },
        };
        numOfBlockType = 2;
        x = 4;
        y = 0;
        r = 0;
        w = 3;
        h = 3;
        type = 6;
    }

    public int[][] getBlock() {
        return block[r];
    }
}
