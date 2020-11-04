package org.project.eye_game;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddFriendActivity extends AppCompatActivity {
    private View decorView;
    private int	uiOption;

    ImageButton searchButton;
    EditText nicknameEditText;
    TextView nicknameTextView;
    ImageButton addFriendButton;

    String TargetID;
    String TargetNickname;

    SimpleDateFormat format1;

    boolean iconActivate;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    String id;
    String email;
    String nickname;

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(getApplicationContext(), FriendActivity.class);
        intent.putExtra("id", getIntent().getExtras().getString("id"));
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        decorView = getWindow().getDecorView();
        uiOption = getWindow().getDecorView().getSystemUiVisibility();
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH )
            uiOption |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN )
            uiOption |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT )
            uiOption |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        decorView.setSystemUiVisibility( uiOption );

        format1 = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");

        id = getIntent().getExtras().getString("id");

        nicknameEditText = findViewById(R.id.nicknameSearchEditText);
        nicknameTextView = findViewById(R.id.nicknameTextView);

        iconActivate = false;

        searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String searchNickname;
                searchNickname = nicknameEditText.getText().toString();
                databaseReference.child("User").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot keys : snapshot.getChildren()) {
                            String targetNickname = keys.child("nickname").getValue(String.class);
                            if(searchNickname.equals(targetNickname)){
                                nicknameTextView.setText(targetNickname);
                                TargetNickname = targetNickname;
                                TargetID = keys.child("id").getValue(String.class);
                                iconActivate = true;
                                nicknameEditText.setText("");
                                return;
                            }
                        }
                        Toast.makeText(getApplicationContext(), "No Such User", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        addFriendButton = findViewById(R.id.addFriendButton);
        addFriendButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(iconActivate==false){
                    return;
                }
                else{
                    FriendData friend = new FriendData(TargetNickname, TargetID, id+"TO"+TargetID);
                    FriendData user = new FriendData(nickname, id, id+ "TO" + TargetID);
                    databaseReference.child("chatRooms").child(friend.roomKey).push().setValue(new ChatMessage(nickname, "Hi!", format1.format(new Date())));
                    databaseReference.child("friendList").child(id).push().setValue(friend);
                    databaseReference.child("friendList").child(TargetID).push().setValue(user);
                }
            }
        });
    }
}