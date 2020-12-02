package org.project.eye_game.interfaces;

public class RankData {
    String id;
    String nickname;
    int score;
    int rank;

    public RankData(String id, String nickname, int score, int rank){
        this.id = id;
        this.nickname = nickname;
        this.score = score;
        this.rank = rank;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
