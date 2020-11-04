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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.project.eye_game.EyeTrackers.FaceTrackerDaemon;
import org.project.eye_game.EyeTrackers.FaceTrackerDaemonGameOne;

import java.io.IOException;

public class GameOneActivity extends AppCompatActivity {
    private View decorView;
    private int	uiOption;

    boolean hammerShake;
    int hammerCount;
    CameraSource cameraSource;

    ImageView target;
    int CHARACTER_ID;
    int newCharacterID;
    String id;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(getApplicationContext(), FriendActivity.class);
        intent.putExtra("id", getIntent().getExtras().getString("id"));
        intent.putExtra("characterID", CHARACTER_ID);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_one);

        decorView = getWindow().getDecorView();
        uiOption = getWindow().getDecorView().getSystemUiVisibility();
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH )
            uiOption |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN )
            uiOption |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT )
            uiOption |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        decorView.setSystemUiVisibility( uiOption );

        CHARACTER_ID = getIntent().getExtras().getInt("character");
        id = getIntent().getExtras().getString("id");

        hammerShake=true;
        hammerCount=0;

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
            Toast.makeText(this, "Permission not granted!\n Grant permission and restart app", Toast.LENGTH_SHORT).show();
        }else{
            initCameraSource();
        }

        while(true){
            if(hammerCount>=30){
                target=findViewById(R.id.eggView);
                target.setImageResource(R.drawable.bomb_effect);
                try{
                    Thread.sleep(2000);
                } catch(Exception e){

                }
                while(true){
                    newCharacterID = (int)Math.random()*2;
                    if(newCharacterID!=CHARACTER_ID) {
                        CHARACTER_ID = newCharacterID;
                        break;
                    }
                }
                switch(newCharacterID){
                    case 0:
                        target.setImageResource(R.drawable.character_open);
                        break;
                    case 1:
                        target.setImageResource(R.drawable.character2_open);
                        break;
                    default:
                        target.setImageResource(R.drawable.character);
                }

                databaseReference.child("User").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot keys : snapshot.getChildren()) {
                            String _id = keys.child("id").getValue(String.class);
                            if (id.equals(_id)) {
                                int TotalEXP = keys.child("totalEXP").getValue(int.class);
                                int TotalRank = keys.child("totalRank").getValue(int.class);
                                int Rank1 = keys.child("rank1").getValue(int.class);
                                int Rank2 = keys.child("rank2").getValue(int.class);
                                int Rank3 = keys.child("rank3").getValue(int.class);
                                int Rank4 = keys.child("rank4").getValue(int.class);
                                int EXP1 = keys.child("exp1").getValue(int.class);
                                int EXP2 = keys.child("exp2").getValue(int.class);
                                int EXP3 = keys.child("exp3").getValue(int.class);
                                int EXP4 = keys.child("exp4").getValue(int.class);
                                String email = keys.child("email").getValue(String.class);
                                String nickname = keys.child("nickname").getValue(String.class);

                                UserData newUser = new UserData(email, nickname, id);
                                newUser.setTotalEXP(TotalEXP);
                                newUser.setTotalRank(TotalRank);
                                newUser.setRank1(Rank1);
                                newUser.setRank2(Rank2);
                                newUser.setRank3(Rank3);
                                newUser.setRank4(Rank4);
                                newUser.setEXP1(EXP1);
                                newUser.setEXP2(EXP2);
                                newUser.setEXP3(EXP3);
                                newUser.setEXP4(EXP4);
                                newUser.setCharacter(CHARACTER_ID);
                                keys.getRef().setValue(newUser);

                                try{
                                    Thread.sleep(3000);
                                } catch(Exception e){

                                }

                                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                                intent.putExtra("id", id);
                                intent.putExtra("characterID", CHARACTER_ID);
                                startActivity(intent);
                                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                                finish();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }

    }

    private void initCameraSource() {
        FaceDetector detector = new FaceDetector.Builder(this)
                .setTrackingEnabled(true)
                .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                .setMode(FaceDetector.FAST_MODE)
                .build();
        detector.setProcessor(new MultiProcessor.Builder(new FaceTrackerDaemonGameOne(GameOneActivity.this)).build());

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
            Toast.makeText(GameOneActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                        hammerShake=true;
                    }
                });
                break;
            case "USER_EYES_CLOSED":
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(hammerShake==true){
                            ShackHammer();
                            hammerCount++;
                            hammerShake=false;
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

    public void ShackHammer() {
        Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
        ImageView imgBell= (ImageView) findViewById(R.id.hammerView);
        imgBell.setAnimation(shake);
    }
}