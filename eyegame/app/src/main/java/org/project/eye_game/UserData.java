package org.project.eye_game;

public class UserData {
    private String userEmail;
    private String userNickname;
    private String userPW;
    private int win;
    private int lose;
    private int draw;

    public UserData(String email, String nickname, String pw){
        this.userNickname = nickname;
        this.userEmail = email;
        this.userPW = pw;
        this.win = 0;
        this.lose = 0;
        this.draw = 0;
    }
    public String getUserNickname(){ return userNickname; }
    public String getUserEmail(){ return userEmail; }
}
