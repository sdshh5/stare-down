package org.project.eye_game.interfaces;

public class UserData {
    String email;
    String nickname;

    int EXP2;
    int EXP3;

    String id;

    int character;

    public UserData(String email, String nickname, String id){

        this.nickname = nickname;
        this.email = email;

        this.EXP2 = 0;
        this.EXP3 = 0;




        this.id = id;

        this.character=0;
    }

    public int getCharacter() {
        return character;
    }

    public void setCharacter(int character) {
        this.character = character;
    }

    public String getNickname(){ return nickname; }
    public String getEmail(){ return email; }

    public void setEmail(String email) {
        email = email;
    }



    public void setNickname(String nickname) {
        nickname = nickname;
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



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
