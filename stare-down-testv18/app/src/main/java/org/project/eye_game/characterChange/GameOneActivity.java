package org.project.eye_game.characterChange;

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

import org.project.eye_game.EyeTrackers.FaceTrackerDaemonGameOne;
import org.project.eye_game.interfaces.FragmentActivity;
import org.project.eye_game.interfaces.FragmentGame;
import org.project.eye_game.R;
import org.project.eye_game.interfaces.UserData;


import java.io.IOException;

public class GameOneActivity extends AppCompatActivity {
    private View decorView;
    private int	uiOption;

    boolean hammerShake;
    int hammerCount;
    boolean characterAvailable;
    CameraSource cameraSource;

    ImageView target;
    ImageView hammerView;
    int CHARACTER_ID;
    int newCharacterID;
    String id;
    Animation shake;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(getApplicationContext(), FragmentActivity.class);
        intent.putExtra("id", getIntent().getExtras().getString("id"));
        intent.putExtra("characterID", CHARACTER_ID);
        intent.putExtra("fragmentId",R.id.game_menu);
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

        CHARACTER_ID = getIntent().getExtras().getInt("characterID");
        id = getIntent().getExtras().getString("id");

        hammerView = findViewById(R.id.hammerView);

        hammerShake=true;
        characterAvailable = true;
        hammerCount=0;
        shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
            Toast.makeText(this, "Permission not granted!\n Grant permission and restart app", Toast.LENGTH_SHORT).show();
        }else{
            initCameraSource();
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
                        hammerView.setImageResource(R.drawable.hammer);
                        hammerShake=true;
                    }
                });
                break;
            case "USER_EYES_CLOSED":
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(hammerShake==true){
                            hammerView.setImageResource(R.drawable.hammer2);
                            hammerCount++;
                            hammerShake=false;
                            if(hammerCount>=15&&characterAvailable==true){
                                characterAvailable = false;
                                target=findViewById(R.id.eggView);
                                //target.setImageResource(R.drawable.bomb_effect);

                                newCharacterID = (int)Math.random()*2;
                                if(newCharacterID==CHARACTER_ID) {
                                    CHARACTER_ID = (CHARACTER_ID+1)%2;
                                }else{
                                    CHARACTER_ID = newCharacterID;
                                }
                                switch(CHARACTER_ID){
                                    case 0:
                                        target.setImageResource(R.drawable.character_open);
                                        target.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                                        break;
                                    case 1:
                                        target.setImageResource(R.drawable.character2_open);
                                        target.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                                        break;
                                    default:
                                        target.setImageResource(R.drawable.character);
                                        target.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                                }

                                databaseReference.child("User").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for(DataSnapshot keys : snapshot.getChildren()) {
                                            String _id = keys.child("id").getValue(String.class);
                                            if (id.equals(_id)) {


                                                int EXP2 = keys.child("exp2").getValue(int.class);
                                                int EXP3 = keys.child("exp3").getValue(int.class);

                                                String email = keys.child("email").getValue(String.class);
                                                String nickname = keys.child("nickname").getValue(String.class);

                                                UserData newUser = new UserData(email, nickname, id);


                                                newUser.setEXP2(EXP2);
                                                newUser.setEXP3(EXP3);

                                                newUser.setCharacter(CHARACTER_ID);
                                                keys.getRef().setValue(newUser);
                                                Intent intent = new Intent(getApplicationContext(), FragmentActivity.class);
                                                intent.putExtra("id", id);
                                                intent.putExtra("characterID", CHARACTER_ID);
                                                intent.putExtra("fragmentId",R.id.profile_menu);
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