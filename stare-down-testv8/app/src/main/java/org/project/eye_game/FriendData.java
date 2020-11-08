package org.project.eye_game;

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

    public void setFriendID(String friendID) {
        this.friendID = friendID;
    }

    public String getFriendNickname() {
        return friendNickname;
    }

    public void setFriendNickname(String friendNickname) {
        this.friendNickname = friendNickname;
    }

    public String getRoomKey() {
        return roomKey;
    }

    public void setRoomKey(String roomKey) {
        this.roomKey = roomKey;
    }
}
