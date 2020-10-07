package org.project.eye_game;

public class UserData {
    private String userEmail;
    private String userNickname;
    private String userPW;
    public UserData(String email, String nickname, String pw){
        this.userNickname = nickname;
        this.userEmail = email;
        this.userPW = pw;
    }
    public String getUserNickname(){ return userNickname; }
    public String getUserEmail(){ return userEmail; }
}
