package org.project.eye_game;

public class GameData {
    String gameTitle;
    int gameEXP;
    int gameRanking;

    public GameData(String title, int exp, int rank){
        this.gameTitle = title;
        this.gameEXP = exp;
        this.gameRanking = rank;
    }

    public String getGameTitle() {
        return gameTitle;
    }
    public int getGameEXP(){
        return gameEXP;
    }

    public int getGameRanking() {
        return gameRanking;
    }

    public void setGameTitle(String gameTitle) {
        this.gameTitle = gameTitle;
    }

    public void setGameEXP(int gameEXP) {
        this.gameEXP = gameEXP;
    }

    public void setGameRanking(int gameRanking) {
        this.gameRanking = gameRanking;
    }
}
