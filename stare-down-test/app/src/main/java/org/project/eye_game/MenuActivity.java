package org.project.eye_game;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;
import java.util.Random;

public class MenuActivity extends AppCompatActivity {
    ImageButton settingButton;
    ImageButton quickPlayButton;
    Button makeRoomButton;
    Button enterRoomButton;
    TextView nicknameTextView;
    String nickname;
    String email;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    private Toast toast;
    private long backKeyPressedTime = 0;

    @Override
    public void onBackPressed(){
        if (System.currentTimeMillis() > backKeyPressedTime + 2500) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(this, "Press One More Time", Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2500) {
            finish();
            toast.cancel();
            toast = Toast.makeText(this,"Bye bye~",Toast.LENGTH_LONG);
            toast.show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Intent receivedIntent = getIntent();
        nickname = receivedIntent.getExtras().getString("nickname");
        email = receivedIntent.getExtras().getString("email");

        nicknameTextView = (TextView)findViewById(R.id.nicknameTextView);
        nicknameTextView.append(nickname+"!");

        settingButton = (ImageButton)findViewById(R.id.SettingButton);
        settingButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
                intent.putExtra("nickname", nickname);
                intent.putExtra("email", email);
                startActivity(intent);
                finish();
            }
        });

        quickPlayButton = (ImageButton)findViewById(R.id.quickStartButton);
        quickPlayButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), ConnectActivity.class);
                databaseReference.child("randomRooms").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        boolean room_exist = false;
                        if(snapshot.exists()==false){
                            room_exist = false;
                        }
                        else{
                            for(DataSnapshot keys : snapshot.getChildren()){
                                boolean room_is_full = keys.child("roomFull").getValue(boolean.class);
                                String searchedRoomID = keys.child("roomID").getValue(String.class);
                                if(room_is_full==false){
                                    DatabaseReference ref =  keys.getRef();
                                    RoomData tmp = new RoomData(searchedRoomID, true);
                                    intent.putExtra("roomID", searchedRoomID);
                                    room_exist = true;
                                    ref.setValue(tmp);
                                    break;
                                }
                            }
                        }
                        if(room_exist==false){
                            Random rnd = new Random();
                            StringBuffer randomRoomID = new StringBuffer();
                            for(int i=0; i<5; i++){
                                randomRoomID.append((char) ((int) (rnd.nextInt(26)) + 65));
                            }
                            intent.putExtra("roomID", randomRoomID.toString());
                            RoomData newRoom = new RoomData(randomRoomID.toString());
                            databaseReference.child("randomRooms").push().setValue(newRoom);
                        }
                        intent.putExtra("nickname", nickname);
                        intent.putExtra("email", email);
                        startActivity(intent);
                        finish();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                });
            }
        });

        makeRoomButton = (Button)findViewById(R.id.generateRoomButton);
        makeRoomButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), GenerateRoomActivity.class);
                intent.putExtra("nickname",nickname);
                intent.putExtra("email", email);
                startActivity(intent);
                finish();
            }
        });

        enterRoomButton = (Button)findViewById(R.id.enterRoomButton);
        enterRoomButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), EnterRoomActivity.class);
                intent.putExtra("nickname",nickname);
                intent.putExtra("email", email);
                startActivity(intent);
                finish();
            }
        });
    }
}