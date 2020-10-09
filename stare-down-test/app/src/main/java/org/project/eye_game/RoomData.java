package org.project.eye_game;

public class RoomData {
    private String roomID;
    private boolean roomFull;

    public RoomData(){}
    public RoomData(String id){
        this.roomID = id;
        this.roomFull = false;
    }
    public RoomData(String id, boolean status){
        this.roomID = id;
        this.roomFull = status;
    }
    public String getRoomID(){ return roomID; }
    public boolean isRoomFull(){ return roomFull; }
    public void setRoomStatus(boolean status){
        roomFull = status;
    }
}
