package org.project.eye_game.interfaces;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.project.eye_game.R;

public class NicknameChangeActivity extends AppCompatActivity {
    Button changeButton;
    EditText newNicknameTextView;

    private View decorView;
    private int	uiOption;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private DatabaseReference userRef;

    String nickname;
    String id;

    boolean isChange = true;

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(getApplicationContext(), FragmentActivity.class);
        intent.putExtra("id", getIntent().getExtras().getString("id"));
        intent.putExtra("nickname", getIntent().getExtras().getString("nickname"));
        intent.putExtra("characterID", getIntent().getExtras().getInt("characterID"));
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nickname_change);

        decorView = getWindow().getDecorView();
        uiOption = getWindow().getDecorView().getSystemUiVisibility();
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH )
            uiOption |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN )
            uiOption |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT )
            uiOption |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        decorView.setSystemUiVisibility( uiOption );

        id = getIntent().getExtras().getString("id");
        nickname = getIntent().getExtras().getString("nickname");

        databaseReference.child("User").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String id_;
                for(DataSnapshot keys: snapshot.getChildren()){
                    id_ = keys.child("id").getValue(String.class);
                    if(id_.equals(id)){
                        userRef = keys.getRef();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        newNicknameTextView = findViewById(R.id.newNicknameEditText);
        changeButton = findViewById(R.id.changeButton);
        changeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String newNickname = newNicknameTextView.getText().toString().trim();
                databaseReference.child("User").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String nick;
                        for(DataSnapshot keys: snapshot.getChildren()){
                            nick = keys.child("nickname").getValue(String.class);
                            if(nick.equals(newNickname)){
                                Toast.makeText(getApplicationContext(), "Being Used Nickname", Toast.LENGTH_SHORT).show();
                                newNicknameTextView.setText("");
                                isChange = false;
                                return;
                            }

                        }
                        isChange = true;
                        nickname = newNickname;
                        userRef.child("nickname").setValue(newNickname);
                        databaseReference.child("RankingRacing").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String id__;
                                for(DataSnapshot keys: snapshot.getChildren()){
                                    id__ = keys.child("id").getValue(String.class);
                                    if(id__.equals(id)&&isChange){
                                        keys.getRef().child("nickname").setValue(newNickname);
                                        return;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        databaseReference.child("RankingTetris").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String id__;
                                for(DataSnapshot keys: snapshot.getChildren()){
                                    id__ = keys.child("id").getValue(String.class);
                                    if(id__.equals(id)&&isChange){
                                        keys.getRef().child("nickname").setValue(newNickname);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        databaseReference.child("friendList").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot keys: snapshot.getChildren()){
                                    for(DataSnapshot k: keys.getChildren()){
                                        if(k.child("friendID").getValue(String.class).equals(id)&&isChange){
                                            k.getRef().child("friendNickname").setValue(newNickname);
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        if(isChange){
                            Intent intent = new Intent(getApplicationContext(), FragmentActivity.class);
                            intent.putExtra("id", id);
                            intent.putExtra("nickname", nickname);
                            intent.putExtra("characterID", getIntent().getExtras().getInt("characterID"));
                            startActivity(intent);
                            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
           }

        });
    }
}