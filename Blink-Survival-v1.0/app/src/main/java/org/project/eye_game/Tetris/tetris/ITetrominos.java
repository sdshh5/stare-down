package org.project.eye_game.Tetris.tetris;

public class ITetrominos extends Tetrominos {
    public ITetrominos() {
        this.block = new int[][][]{
                {
                        {1, 0, 0, 0},
                        {1, 0, 0, 0},
                        {1, 0, 0, 0},
                        {1, 0, 0, 0},
                },
                {
                        {1, 1, 1, 1},
                        {0, 0, 0, 0},
                        {0, 0, 0, 0},
                        {0, 0, 0, 0},
                }
        };
        numOfBlockType = 2;
        x = 3;
        y = 0;
        r = 0;
        w = 4;
        h = 4;
        type = 2;
    }

    public int[][] getBlock() {
        return block[r];
    }
}
