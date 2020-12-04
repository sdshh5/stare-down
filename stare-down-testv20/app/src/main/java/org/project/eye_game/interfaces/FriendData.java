package org.project.eye_game.interfaces;

import android.graphics.drawable.Drawable;

public class FriendData {
    String friendNickname;
    String friendID;
    String roomKey;

    public FriendData( String friendNickname, String friend, String key){
        this.friendNickname = friendNickname;
        this.friendID = friend;
        this.roomKey = key;
    }

    public String getFriendID() {
        return friendID;
    }

    public String getFriendNickname() { return friendNickname; }

    public String getRoomKey() {
        return roomKey;
    }
}
