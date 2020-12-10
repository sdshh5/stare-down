package org.project.eye_game.Tetris.tetris;

public class LTetrominos extends Tetrominos {
    public LTetrominos() {
        this.block = new int[][][]{
                {
                        {1, 0, 0},
                        {1, 0, 0},
                        {1, 1, 0},
                },
                {
                        {1, 1, 1},
                        {1, 0, 0},
                        {0, 0, 0},
                },
                {
                        {1, 1, 0},
                        {0, 1, 0},
                        {0, 1, 0},
                },
                {
                        {0, 0, 0},
                        {0, 0, 1},
                        {1, 1, 1},
                }
        };
        numOfBlockType = 4;
        x = 4;
        y = 0;
        r = 0;
        w = 3;
        h = 3;
        type = 3;
    }

    public int[][] getBlock() {
        return block[r];
    }
}
