package org.project.eye_game.interfaces;

public class GameData {
    String gameTitle;
    int gameEXP;


    public GameData(String title, int exp){
        this.gameTitle = title;
        this.gameEXP = exp;

    }

    public String getGameTitle() {
        return gameTitle;
    }
    public int getGameEXP(){
        return gameEXP;
    }



    public void setGameTitle(String gameTitle) {
        this.gameTitle = gameTitle;
    }

    public void setGameEXP(int gameEXP) {
        this.gameEXP = gameEXP;
    }


}
