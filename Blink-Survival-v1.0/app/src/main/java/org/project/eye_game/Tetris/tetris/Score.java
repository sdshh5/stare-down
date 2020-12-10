package org.project.eye_game.Tetris.tetris;

public abstract class Score {
    private int score;
    private int highScore;

    protected Score() {
        this.score = 0;
        this.highScore = 0;
    }

    public void init() {
        score = 0;
    }

    public int getScore() {
        return score;
    }
    public int getHighScore() { return highScore; }

    public void setHighScore(int score) {
        this.highScore = score;
    }

    protected void addScore(int score) {
        this.score += score;
    }
    public void updateHighScore() {
        if (this.score > this.highScore) {
            this.highScore = this.score;
        }
    }

    public void removeLIne(int removedLineCount) {
        calculatorScore(removedLineCount);
    }
    protected abstract void calculatorScore(int removedLineCount);
    protected abstract void ClearBoard();
}
