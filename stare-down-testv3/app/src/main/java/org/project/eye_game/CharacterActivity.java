package org.project.eye_game;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CharacterActivity extends AppCompatActivity {
    private View decorView;
    private int	uiOption;
    Intent receivedIntent;
    String email;
    String nickname;
    int TotalRank;
    int TotalEXP;
    int Rank1;
    int Rank2;
    int Rank3;
    int Rank4;
    int EXP1;
    int EXP2;
    int EXP3;
    int EXP4;

    TextView nicknameView;
    TextView EXPView;
    TextView rankView;
    TextView emailView;

    GridView gridView;
    GameDataAdapter adapter;
    Button changeNicknameButton;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
        receivedIntent = getIntent();
        intent.putExtra("nickname",receivedIntent.getExtras().getString("nickname"));
        intent.putExtra("email", receivedIntent.getExtras().getString("email"));
        intent.putExtra("rank", receivedIntent.getExtras().getInt("rank"));
        intent.putExtra("exp", receivedIntent.getExtras().getInt("exp"));
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character);

        decorView = getWindow().getDecorView();
        uiOption = getWindow().getDecorView().getSystemUiVisibility();
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH )
            uiOption |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN )
            uiOption |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT )
            uiOption |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        decorView.setSystemUiVisibility( uiOption );

        nicknameView = findViewById(R.id.nicknameTextView);
        EXPView = findViewById(R.id.EXPTextView);
        rankView = findViewById(R.id.totalRankingTextView);
        emailView = findViewById(R.id.emailTextView);

        receivedIntent = getIntent();
        email = receivedIntent.getExtras().getString("email");
        nickname = receivedIntent.getExtras().getString("nickname");
        nicknameView.append(nickname);
        emailView.append(email);

        databaseReference.child("User").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot keys : snapshot.getChildren()){
                    String userEmail = keys.child("email").getValue(String.class);
                    if(email.equals(userEmail)){
                        TotalEXP = keys.child("totalEXP").getValue(int.class);
                        TotalRank = keys.child("totalRank").getValue(int.class);
                        Rank1 = keys.child("rank1").getValue(int.class);
                        Rank2 = keys.child("rank2").getValue(int.class);
                        Rank3 = keys.child("rank3").getValue(int.class);
                        Rank4 = keys.child("rank4").getValue(int.class);
                        EXP1 = keys.child("exp1").getValue(int.class);
                        EXP2 = keys.child("exp2").getValue(int.class);
                        EXP3 = keys.child("exp3").getValue(int.class);
                        EXP4 = keys.child("exp4").getValue(int.class);

                        EXPView.append(Integer.toString(TotalEXP));
                        if(TotalRank==0){
                            rankView.append("NONE");
                        }else{
                            rankView.append(Integer.toString(TotalRank));
                        }

                        gridView =  findViewById(R.id.characterGridView);
                        adapter = new GameDataAdapter();

                        adapter.addItem(new GameData("Game1", EXP1, Rank1));
                        adapter.addItem(new GameData("Game2", EXP2, Rank2));
                        adapter.addItem(new GameData("Game3", EXP3, Rank3));
                        adapter.addItem(new GameData("Game4", EXP4, Rank4));
                        gridView.setAdapter(adapter);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        changeNicknameButton = findViewById(R.id.nicknameChangeButton);
        changeNicknameButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), NicknameChangeActivity.class);
                intent.putExtra("nickname",receivedIntent.getExtras().getString("nickname"));
                intent.putExtra("email", receivedIntent.getExtras().getString("email"));
                intent.putExtra("rank", receivedIntent.getExtras().getInt("rank"));
                intent.putExtra("exp", receivedIntent.getExtras().getInt("exp"));
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                finish();
            }
        });
    }
}