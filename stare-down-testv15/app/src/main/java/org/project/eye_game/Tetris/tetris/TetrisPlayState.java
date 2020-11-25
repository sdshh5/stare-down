package org.project.eye_game.Tetris.tetris;

public class TetrisPlayState extends TetrisGameState {
    private Tetrominos currentTetrominos;
    private Tetrominos nextTetrominos;
    private Tetrominos shadowTetrominos;
    private TetrisBoard tetrisBoard;

    public TetrisPlayState(Tetris tetris, TetrisBoard board) {
        this.tetris = tetris;
        this.tetrisBoard = board;
        initTetrominos();
    }

    public void init() {
        this.tetrisBoard.init();
        initTetrominos();
    }

    private void initTetrominos() {
        currentTetrominos = TetrominosFactory.create();
        nextTetrominos = TetrominosFactory.create();
        shadowTetrominos = TetrominosFactory.clone(currentTetrominos);
    }

    public void moveLeft() {
        TetrisLog.d("TetrisPlayState.moveLeft()");
        currentTetrominos.moveLeft();
        if (tetrisBoard.isAcceptable(currentTetrominos)) {
            TetrisLog.d("Accept");
        } else {
            currentTetrominos.moveRight();
            TetrisLog.d("Not Accept");
        }
    }

    public void moveRight() {
        TetrisLog.d("TetrisPlayState.moveRight()");
        currentTetrominos.moveRight();
        if (tetrisBoard.isAcceptable(currentTetrominos)) {
            TetrisLog.d("Accept");
        } else {
            currentTetrominos.moveLeft();
            TetrisLog.d("Not Accept");
        }
    }

    public void rotate() {
        TetrisLog.d("TetrisPlayState.rotate()");
        currentTetrominos.rotate();
        if (!tetrisBoard.isAcceptable(currentTetrominos)) {
            currentTetrominos.preRotate();
            TetrisLog.d("Not Accept");
        } else {
            TetrisLog.d("Accept");
        }
    }


    public void moveDown() {
        TetrisLog.d("TetrisPlayState.moveDown()");
        currentTetrominos.moveDown();
        if (tetrisBoard.isAcceptable(currentTetrominos)) {
            TetrisLog.d("Accept");
        } else {
            currentTetrominos.moveUp();
            TetrisLog.d("Can not move down");
            fixCurrentBlock();
            updateBoard();
            updateBlock() ;
        }
    }


    public void moveBottom() {
        TetrisLog.d("TetrisPlayState.moveBottom()");
        while(tetrisBoard.isAcceptable(currentTetrominos)) {
            currentTetrominos.moveDown();
        }
        if (tetrisBoard.isAcceptable(currentTetrominos)) {
            return;
        }
        currentTetrominos.moveUp();
    }


    private void fixCurrentBlock() {
        tetrisBoard.addTetrominos(currentTetrominos);
    }

    private void updateBlock() {
        currentTetrominos = nextTetrominos;
        shadowTetrominos = TetrominosFactory.clone(currentTetrominos);
        nextTetrominos = TetrominosFactory.create();
    }

    public boolean gameOver() {
        TetrisLog.d("Check Game over!");
        return !tetrisBoard.isAcceptable(currentTetrominos);
    }

    private void updateScore(int removedLines) {
        tetris.addRemoveLineCount(removedLines);
        if (tetrisBoard.isClear()) {
            tetris.ClearBoard();
        }
        tetris.updateHighScore();
    }

    private void updateBoard() {
        int removedLine = tetrisBoard.arrange();
        updateScore(removedLine);
    }

    public Tetrominos getCurrentTetrominos() {
        return currentTetrominos;
    }

    public Tetrominos getNextTetrominos() {
        return nextTetrominos;
    }

    public Tetrominos getShodowTetrominos() {
        moveShadowBottom();
        return shadowTetrominos;
    }

    private void moveShadowBottom() {
        TetrisLog.d("TetrisPlayState.moveShadowBottom()");

        shadowTetrominos.clone(currentTetrominos);

        while(tetrisBoard.isAcceptable(shadowTetrominos)) {
            shadowTetrominos.moveDown();
        }
        if (tetrisBoard.isAcceptable(shadowTetrominos)) {
            return;
        }
        shadowTetrominos.moveUp();
    }

    public boolean isPlayState() {
        return true;
    }
}
