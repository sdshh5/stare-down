package org.project.eye_game;

public class UserData {
    private String userEmail;
    private String userNickname;

    public UserData(String email, String nickname){
        this.userNickname = nickname;
        this.userEmail = email;
    }
    public String getUserNickname(){ return userNickname; }
    public String getUserEmail(){ return userEmail; }
}
