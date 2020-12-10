package org.project.eye_game.Tetris.tetris;

public class TetrominosFactory {
    public static Tetrominos create() {
        return create ((int) (Math.random() * 7)+1);
    }

    private static Tetrominos create(int type) {
        switch(type) {
            case 1:
                return new OTetrominos();
            case 2:
                return new ITetrominos();
            case 3:
                return new LTetrominos();
            case 4:
                return new JTetrominos();
            case 5:
                return new TTetrominos();
            case 6:
                return new STetrominos();
            case 7:
                return new ZTetrominos();
            default:
                TetrisLog.e("Tetrominos Create Error! Never come to here!");
                return new ITetrominos();
        }
    }

    public static Tetrominos clone(Tetrominos t) {
        Tetrominos block = create(t.getType());
        block.clone(t);
        return block;
    }

    private TetrominosFactory() {}
}
