package org.project.eye_game.util;

public class FriendList {
    private String userNickname;
    private boolean friendExist;

    FriendList(String nickname){
        this.userNickname = nickname;
        this.friendExist = false;
    }
    FriendList(String nickname, boolean exist){
        this.userNickname = nickname;
        this.friendExist = exist;
    }
    public void setFriendExist(boolean exist) {
        this.friendExist = exist;
    }
}
