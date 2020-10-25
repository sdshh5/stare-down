package org.project.eye_game;

public class UserData {
    String Email;
    String Nickname;

    int TotalRank;
    int TotalEXP;
    int Rank1;
    int Rank2;
    int Rank3;
    int Rank4;
    int EXP1;
    int EXP2;
    int EXP3;
    int EXP4;

    public UserData(String email, String nickname){
        this.Nickname = nickname;
        this.Email = email;
        this.Rank1 = 0;
        this.Rank2 = 0;
        this.Rank3 = 0;
        this.Rank4 = 0;
        this.EXP1 = 0;
        this.EXP2 = 0;
        this.EXP3 = 0;
        this.EXP4 = 0;

        this.TotalRank = 0;
        this.TotalEXP = 0;
    }

    public String getNickname(){ return Nickname; }
    public String getEmail(){ return Email; }

    public void setEmail(String email) {
        Email = email;
    }

    public int getTotalRank() {
        return TotalRank;
    }

    public void setTotalRank(int totalRank) {
        TotalRank = totalRank;
    }

    public void setNickname(String nickname) {
        Nickname = nickname;
    }

    public int getTotalEXP() {
        return TotalEXP;
    }

    public void setTotalEXP(int totalEXP) {
        TotalEXP = totalEXP;
    }

    public int getRank1() {
        return Rank1;
    }

    public void setRank1(int rank1) {
        Rank1 = rank1;
    }

    public int getEXP4() {
        return EXP4;
    }

    public void setEXP4(int EXP4) {
        this.EXP4 = EXP4;
    }

    public int getEXP3() {
        return EXP3;
    }

    public void setEXP3(int EXP3) {
        this.EXP3 = EXP3;
    }

    public int getEXP2() {
        return EXP2;
    }

    public void setEXP2(int EXP2) {
        this.EXP2 = EXP2;
    }

    public int getEXP1() {
        return EXP1;
    }

    public void setEXP1(int EXP1) {
        this.EXP1 = EXP1;
    }

    public int getRank4() {
        return Rank4;
    }

    public void setRank4(int rank4) {
        Rank4 = rank4;
    }

    public int getRank3() {
        return Rank3;
    }

    public void setRank3(int rank3) {
        Rank3 = rank3;
    }

    public int getRank2() {
        return Rank2;
    }

    public void setRank2(int rank2) {
        Rank2 = rank2;
    }
}
