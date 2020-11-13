package org.project.eye_game.Tetris.tetris;

public class Tetris implements ITetris, ITetrisGameState {
    public static final int EMPTY = 0;

    private Score score;
    private int removedLineCount;
    private boolean isEnableShadow = false;

    private TetrisIdleState idleState;
    private TetrisPlayState playState;
    private TetrisPauseState pauseState;
    private TetrisGameOverState gameOverState;

    private TetrisBoard board;
    private TetrisGameState gameState;

    private ITetrisObserver observer;

    public Tetris(int width, int height) {
        TetrisLog.d("Create new Tetris : " + width + " x " + height);

        board = new TetrisBoard(width, height);

        TetrisInitState initState = new TetrisInitState(this);

        idleState = new TetrisIdleState(this);
        pauseState = new TetrisPauseState(this);
        playState = new TetrisPlayState(this, this.board);
        gameOverState = new TetrisGameOverState(this);

        gameState = initState;
    }

    public void init() {
        TetrisLog.d("Tetris.Init()");

        removedLineCount = 0;
        gameState = idleState;
        board.init();
        if (score != null) {
            score.init();
        }
        gameState.update();
    }

    public void play() {
        TetrisLog.d("Tetris.play()");
        if (score != null) {
            score.init();
        }

        setState(playState);
        gameState.init();
        gameState.update();
    }

    public void pause() {
        TetrisLog.d("Tetris.pause()");
        setState(pauseState);
        gameState.update();
    }

    public void resume() {
        TetrisLog.d("Tetris.resume()");
        setState(playState);
        gameState.update();
    }

    public void moveLeft() {
        gameState.moveLeft();
        gameState.update();
    }

    public void moveRight() {
        gameState.moveRight();
        gameState.update();
    }

    public void moveDown() {
        if (gameState.gameOver()) {
            setState(gameOverState);
        } else {
            gameState.moveDown();
        }
        gameState.update();
    }

    public void rotate() {
        gameState.rotate();
        gameState.update();
    }

    public void moveBottom() {
        gameState.moveBottom();
        gameState.update();
    }

    private void setState(TetrisGameState state) {
        this.gameState = state;
    }

    public boolean setScore(Score score) {
        this.score = score;
        return true;
    }

    public int getWidth() {
        return board.getWidth();
    }
    public int getHeight() {
        return board.getHeight();
    }
    public int getScore() {
        if (score == null) {
            return 0;
        }
        return this.score.getScore();
    }

    public int getHighScore() {
        if (score == null) {
            return 0;
        }
        return this.score.getHighScore();
    }

    public void updateHighScore() {
        score.updateHighScore();
    }

    public int getRemovedLineCount() { return this.removedLineCount; }
    public void addRemoveLineCount(int line) {
        this.removedLineCount += line;
        this.score.removeLIne(line);
    }

    public void ClearBoard() {
        this.score.ClearBoard();
    }

    public int[][] getBoard() {
        return board.getBoard();
    }
    public Tetrominos getCurrentBlock() { return gameState.getCurrentTetrominos(); }
    public Tetrominos getNextBlock() { return gameState.getNextTetrominos(); }
    public Tetrominos getShadowBlock() { return gameState.getShodowTetrominos(); }

    public boolean isEnableShadow(){ return this.isEnableShadow; }

    public void enableShadow() {
        this.isEnableShadow = true;
        gameState.update();
    }

    public void disableShadow() {
        this.isEnableShadow = false;
        gameState.update();
    }

    public void register(ITetrisObserver observer) {
        TetrisLog.d("Registered view!");
        this.observer = observer;
    }

    public boolean isIdleState() { return gameState.isIdleState(); }
    public boolean isGameOverState() { return gameState.isGameOverState(); }
    public boolean isPlayState() { return gameState.isPlayState(); }
    public boolean isPauseState() { return gameState.isPauseState(); }

    public void update() {
        if (this.observer == null) {
            return;
        }
        this.observer.update();
    }
}
