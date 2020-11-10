package org.project.eye_game;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.project.eye_game.EyeTrackers.FaceTrackerDaemonMenu;
import org.project.eye_game.Tetris.tetrisgame.TetrisActivity;

import java.io.IOException;

public class MenuActivity extends AppCompatActivity {
    Button profileButton;
    Button globalChatButton;
    Button rankDetailButton;
    TextView nicknameTextView;
    TextView rankTextView;
    TextView expTextView;
    ImageView characterView;

    ImageView card1;
    ImageView card2;
    ImageView card3;
    ImageView card4;

    String nickname;
    String email;
    int CHARACTER_ID;
    int rank;
    String id;
    int exp;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    private Toast toast;
    private long backKeyPressedTime = 0;

    private View decorView;
    private int	uiOption;

    CameraSource cameraSource;

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

        characterView = findViewById(R.id.characterView);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
            Toast.makeText(this, "Permission not granted!\n Grant permission and restart app", Toast.LENGTH_SHORT).show();
        }else{
            initCameraSource();
        }

        id = getIntent().getExtras().getString("id");
        CHARACTER_ID = getIntent().getExtras().getInt("characterID");
        switch (CHARACTER_ID){
            case 0:
                characterView.setImageResource(R.drawable.character_open);
                break;
            case 1:
                characterView.setImageResource(R.drawable.character2_open);
                break;
            default:
                characterView.setImageResource(R.drawable.character);
        }

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
                intent.putExtra("characterID", CHARACTER_ID);
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
                intent.putExtra("characterID", CHARACTER_ID);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                finish();
            }
        });

        card1 = findViewById(R.id.gameCard1);
        card1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), GameOneActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("characterID", CHARACTER_ID);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                finish();
            }
        });

        View.OnClickListener listener=new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getApplicationContext(), TetrisActivity.class);
                startActivity(intent);
            }
        };
        ImageView card2=(ImageView) findViewById(R.id.gameCard2);
        card2.setOnClickListener(listener);

    }

    private void initCameraSource() {
        FaceDetector detector = new FaceDetector.Builder(this)
                .setTrackingEnabled(true)
                .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                .setMode(FaceDetector.FAST_MODE)
                .build();
        detector.setProcessor(new MultiProcessor.Builder(new FaceTrackerDaemonMenu(MenuActivity.this)).build());

        cameraSource = new CameraSource.Builder(this, detector)
                .setRequestedPreviewSize(1024, 768)
                .setFacing(CameraSource.CAMERA_FACING_FRONT)
                .setRequestedFps(5.0f)
                .build();

        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            cameraSource.start();
        }
        catch (IOException e) {
            Toast.makeText(MenuActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (cameraSource!=null) {
            cameraSource.stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cameraSource!=null) {
            cameraSource.release();
        }
    }

    public void updateState(String state){
        switch (state){
            case "USER_EYES_OPEN":
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        switch (CHARACTER_ID){
                            case 0:
                                characterView.setImageResource(R.drawable.character_open);
                                break;
                            case 1:
                                characterView.setImageResource(R.drawable.character2_open);
                                break;
                            default:
                                characterView.setImageResource(R.drawable.character);
                                break;
                        }
                    }
                });
                break;
            case "USER_EYES_CLOSED":
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        switch (CHARACTER_ID){
                            case 0:
                                characterView.setImageResource(R.drawable.character_close);
                                break;
                            case 1:
                                characterView.setImageResource(R.drawable.character2_close);
                                break;
                            default:
                                characterView.setImageResource(R.drawable.character);
                                break;
                        }
                    }
                });
                break;
            case "FACE_NOT_FOUND":
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Where is your face??", Toast.LENGTH_SHORT);
                    }
                });
                break;
        }
    }

}