package org.project.eye_game.interfaces;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.project.eye_game.R;

public class MainActivity extends AppCompatActivity {
    private Toast toast;
    private long backKeyPressedTime = 0;
    ImageView characterView;

    private View decorView;
    private int	uiOption;

    private Intent intent;

    String id;
    int characterID;
    String nickname;

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference dbRef = db.getReference();

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
        setContentView(R.layout.activity_main);

        decorView = getWindow().getDecorView();
        uiOption = getWindow().getDecorView().getSystemUiVisibility();
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH )
            uiOption |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN )
            uiOption |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT )
            uiOption |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        decorView.setSystemUiVisibility( uiOption );

        characterView = findViewById(R.id.characterView);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences = getSharedPreferences("login",MODE_PRIVATE);
                id = sharedPreferences.getString("id","");

                dbRef.child("User").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String id_;
                        for(DataSnapshot keys: snapshot.getChildren()){
                            id_ = keys.child("id").getValue(String.class);
                            if(id_.equals(id)){
                                characterID = keys.child("character").getValue(int.class);
                                nickname = keys.child("nickname").getValue(String.class);
                                break;
                            }
                        }

                        String userNickname = nickname;

                        if(id.length()==0||id==null||userNickname==null) {

                            intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                            finish();
                        }
                        else {
                            intent = new Intent(MainActivity.this, FragmentActivity.class);
                            intent.putExtra("id", id);
                            intent.putExtra("characterID", characterID);
                            intent.putExtra("nickname", userNickname);
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
        },2000);
    }
}
