package com.chobocho.tetrisgame;

import com.chobocho.tetris.Score;

public class PlayerScoreImpl extends Score {
    private int additionalScore;

    protected void  calculatorScore(int removedLineCount) {
        if (removedLineCount == 0) {
            this.additionalScore = 1;
        }

        int lineScore[] = { 0, 88, 188, 288, 8888 };

        additionalScore *= 10;

        if (additionalScore > 10000) {
            additionalScore = 10000;
        }
        
        addScore(removedLineCount * 10 * additionalScore + lineScore[removedLineCount]);
    }
    protected void ClearBoard() {
        addScore(100000);
    }
}
