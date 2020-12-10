package org.project.eye_game.interfaces;

public class GameData {
    String id;
    int rank;
    String nickname;
    int score;


    public GameData(String nickname, int score, int rank){
        this.score = score;
        this.rank = rank;
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getId() {
        return id;
    }

    public int getRank() {
        return rank;
    }

    public int getScore() {
        return score;
    }
}
