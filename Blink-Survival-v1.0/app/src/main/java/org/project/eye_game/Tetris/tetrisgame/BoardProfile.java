package org.project.eye_game.Tetris.tetrisgame;

public class BoardProfile {
    final static public String TAG = "BoardProfile";

    public static int screenW = 1080;
    public static int screenH = 1820;
    public static int startX = 0;
    public static int startY = 0;
    public static int buttonStartX = 0;
    public static int boardWidth = 10;
    public static int boardHeight = 20;
    public static int button_size = 4;
    private int block_size = 80;

    public BoardProfile(int w, int h) {
        setScreenSize(w, h);
    }

    public int screenWidth() {
        return this.screenW;
    }

    public int screenHeight() {
        return this.screenH;
    }

    public void setScreenSize(int w, int h) {
        this.screenW = w;
        this.screenH = h;

        int block_width = (int) (w / (boardWidth+6));
        int block_height = (int) (h / (boardHeight+11));
        block_size = Math.min(block_width, block_height);

        startX = (screenWidth() - block_size*(boardWidth+6))/2 + block_size;
        startY = block_size;
        buttonStartX = startX + block_size * 1;
        button_size = block_size * 3;
    }

    public int blockSize() {
        return block_size;
    }

    public int buttonSize() { return button_size; }

    public static int[] imageName = {

    };

    public int[] buttonImageName = {

    };
}
