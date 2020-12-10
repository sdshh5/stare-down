package org.project.eye_game.Tetris.tetris;

public class ZTetrominos extends Tetrominos {
    public ZTetrominos() {
        this.block = new int[][][]{
                {
                        {1, 1, 0},
                        {0, 1, 1},
                        {0, 0, 0},
                },
                {
                        {0, 1, 0},
                        {1, 1, 0},
                        {1, 0, 0},
                },
        };
        numOfBlockType = 2;
        x = 4;
        y = 0;
        r = 0;
        w = 3;
        h = 3;
        type = 7;
    }

    public int[][] getBlock() {
        return block[r];
    }
}
