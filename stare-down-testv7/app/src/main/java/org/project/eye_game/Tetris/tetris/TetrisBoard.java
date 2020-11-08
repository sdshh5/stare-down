package org.project.eye_game.Tetris.tetris;

public class TetrisBoard {
    private int width;
    private int height;
    private int[][] board;

    /**
     * Default constructor
     */
    TetrisBoard(int width, int height) {
        this.width = width;
        this.height = height;

        board = new int[this.height][this.width];
        init();
    }

    public void init() {
        for (int i = 0; i < this.getHeight(); i++) {
            for (int j = 0; j < this.getWidth(); j++) {
                board[i][j] = 0;
            }
        }
    }

    public int[][] getBoard() {
        return this.board;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public boolean isAcceptable(Tetrominos tetrominos) {
        int[][] block = tetrominos.getBlock();
        int w = tetrominos.getWidth();
        int h = tetrominos.getHeight();
        int x = tetrominos.getX();
        int y = tetrominos.getY();

        TetrisLog.d("W : " + w + " H : " + h);
        TetrisLog.d("X : " + x + " Y : " + y);

        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                if (block[i][j] != Tetris.EMPTY) {
                    if (x < 0 || (x+j) > (width-1) || (y+i) > (height-1) || y < 0) {
                        return false;
                    }
                    if (board[y+i][x+j] != Tetris.EMPTY) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public void addTetrominos(Tetrominos tetrominos) {
        int[][] block = tetrominos.getBlock();
        int w = tetrominos.getWidth();
        int h = tetrominos.getHeight();
        int x = tetrominos.getX();
        int y = tetrominos.getY();
        int type = tetrominos.getType();

        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                if (block[i][j] != Tetris.EMPTY) {
                    board[i + y][j + x] = type;
                }
            }
        }
    }

    public int arrange() {
        int x = 0, y = 0, m = 0;
        int count = 0;
        int removedLIne = 0;

        for (y = height-1; y >= 0; y--) {
            count = 0;
            for (x = 0; x < 10; x++) {
                if (board[y][x] != Tetris.EMPTY) {
                    count++;
                }
            }

            if (count == width) {
                removedLIne++;
                for (x = 0; x < width; x++) {
                    for (m = y; m > 0; m--) {
                        board[m][x] = board[m - 1][x];
                    }
                    board[m][0] = Tetris.EMPTY;
                }
                y++;
            }
        }
        return removedLIne;
    }

    public boolean isClear() {
        int i = 0, j = 0;

        for (i = height-1; i >= 0; i--) {
            for (j = 0; j < width; j++) {
                if (board[i][j] != Tetris.EMPTY) {
                    return false;
                }
            }
        }

        return true;
    }
}
