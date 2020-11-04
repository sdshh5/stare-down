package org.project.eye_game;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Map;
import java.util.Random;

public class MenuActivity extends AppCompatActivity {
    Button profileButton;
    Button globalChatButton;
    Button rankDetailButton;
    TextView nicknameTextView;
    TextView rankTextView;
    TextView expTextView;
    String nickname;
    String email;
    int rank;
    String id;
    int exp;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    private Toast toast;
    private long backKeyPressedTime = 0;

    private View decorView;
    private int	uiOption;

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
        nicknameTextView = (TextView)findViewById(R.id.nicknameTextView);
        expTextView = (TextView)findViewById(R.id.expTextView);

        databaseReference.child("User").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot keys : snapshot.getChildren()){
                    String _id = keys.child("id").getValue(String.class);
                    if(id.equals(_id)){
                        nickname = keys.child("nickname").getValue(String.class);
                        email = keys.child("email").getValue(String.class);
                        rank = keys.child("totalRank").getValue(int.class);
                        exp = keys.child("totalEXP").getValue(int.class);
                        nicknameTextView.append(nickname);
                        expTextView.append(Integer.toString(exp));
                        rankTextView = (TextView)findViewById(R.id.rankingTextView);
                        if(rank==0){
                            rankTextView.append("NONE");
                        }else{
                            rankTextView.append(Integer.toString(rank));
                        }

                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
        profileButton = findViewById(R.id.SettingButton);
        profileButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), CharacterActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                finish();
            }
        });

        globalChatButton = findViewById(R.id.globalChatButton);
        globalChatButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), GlobalChatActivity.class);
                intent.putExtra("nickname", nickname);
                intent.putExtra("id",id);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                finish();
            }
        });

    }
}