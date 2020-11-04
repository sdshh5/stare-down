package org.project.eye_game;

public class ChatMessage {
    String nickname;
    String message;
    String time;
    int gravity;

    public ChatMessage(String nickname, String message, String time){
        this.message = message;
        this.nickname = nickname;
        this.time = time;
        this.gravity = 0;
    }

    public int getGravity() {
        return gravity;
    }

    public void setGravity(int gravity) {
        this.gravity = gravity;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
