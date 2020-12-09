package org.project.eye_game.interfaces;

public class ChatMessage {
    String id;
    String nickname;
    String message;
    String time;
    int gravity;

    public ChatMessage(String id, String nickname, String message, String time){
        this.id = id;
        this.message = message;
        this.nickname = nickname;
        this.time = time;
        this.gravity = 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
