package org.project.eye_game.Tetris.tetrisgame;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.project.eye_game.EyeTrackers.FaceTrackerDaemonGameTwo;
import org.project.eye_game.interfaces.MenuActivity;
import org.project.eye_game.Tetris.player.Player;
import org.project.eye_game.Tetris.player.PlayerImpl;
import org.project.eye_game.interfaces.*;

import java.io.IOException;

public class TetrisActivity extends AppCompatActivity {
    private TetrisViewForN8 twN8;
    PlayerInputImplForN8 playerInput;
    CameraSource cameraSource;
    int CHARACTER_ID;
    String id;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference dbRef = db.getReference();
    Boolean isLeft = true;
    Boolean isRight = true;
    Boolean isBoth=true;

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
        intent.putExtra("id", getIntent().getExtras().getString("id"));
        intent.putExtra("characterID", CHARACTER_ID);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        finish();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        int screenHeight = getWindowManager().getDefaultDisplay().getHeight();


        Log.e("Test", "W" + screenWidth + " H" + screenHeight);

        int BOARD_WIDTH = 10;
        int BOARD_HEIGHT = 20;

        Player player = new PlayerImpl(getIntent().getExtras().getString("id"), BOARD_WIDTH, BOARD_HEIGHT);
        playerInput = new PlayerInputImplForN8();
        playerInput.registerPlayer(player);

        twN8 = new TetrisViewForN8(this, player);
        twN8.setScreenSize(screenWidth,screenHeight);
        id = getIntent().getExtras().getString("id");
        dbRef.child("User").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot keys: snapshot.getChildren()){
                    String id_ = keys.child("id").getValue(String.class);
                    if(id.equals(id_)){
                        twN8.loadHIghScore(keys.child("exp3").getValue(int.class));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        setContentView(twN8);

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
        detector.setProcessor(new MultiProcessor.Builder(new FaceTrackerDaemonGameTwo(TetrisActivity.this)).build());

        cameraSource = new CameraSource.Builder(this, detector)
                .setRequestedPreviewSize(1024, 768)
                .setFacing(CameraSource.CAMERA_FACING_FRONT)
                .setRequestedFps(15.0f)
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
            Toast.makeText(TetrisActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }


    }

    public void updateState(String state){
        switch (state) {
            case "FACE_NOT_FOUND":
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Where is your face??", Toast.LENGTH_SHORT);
                    }
                });
                break;
            case "USER_EYES_CLOSED":
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        isBoth=false;
                        isLeft=true;
                        isRight=true;
                    }
                });
                break;
            case "LEFT_EYE_CLOSE":
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        isLeft=false;
                        isRight=true;
                        isBoth=true;
                    }
                });
                break;
            case "RIGHT_EYE_CLOSE":
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        isRight=false; isLeft=true; isBoth=true;
                    }
                });
                break;
            case "USER_EYES_OPEN":
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(isBoth==false)
                            playerInput.downCall();
                        else if(isRight==false)
                            playerInput.leftCall();
                        else if(isLeft==false)
                            playerInput.rightCall();
                        isBoth=true; isLeft=true; isRight=true;
                    }
                });
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (twN8 != null) {
            twN8.pauseGame();
            dbRef.child("User").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot keys : snapshot.getChildren()){
                        String id_ = keys.child("id").getValue(String.class);
                        if(id_.equals(id)){
                            UserData newUser = new UserData(keys.child("email").getValue(String.class),
                                    keys.child("nickname").getValue(String.class), id);
                            newUser.setTotalEXP(keys.child("totalEXP").getValue(int.class));
                            newUser.setTotalRank(keys.child("totalRank").getValue(int.class));
                            newUser.setRank1(keys.child("rank1").getValue(int.class));
                            newUser.setRank2(keys.child("rank2").getValue(int.class));
                            newUser.setRank3(keys.child("rank3").getValue(int.class));
                            newUser.setRank4(keys.child("rank4").getValue(int.class));
                            newUser.setEXP1(keys.child("exp1").getValue(int.class));
                            newUser.setCharacter(keys.child("character").getValue(int.class));
                            newUser.setEXP2(keys.child("exp2").getValue(int.class));
                            newUser.setEXP3(twN8.saveScore());
                            newUser.setEXP4(keys.child("exp4").getValue(int.class));
                            keys.getRef().setValue(newUser);
                            break;
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
        if (cameraSource!=null) {
            cameraSource.stop();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (twN8 != null) {
            twN8.resumeGame();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cameraSource!=null) {
            cameraSource.release();
        }
    }


}
