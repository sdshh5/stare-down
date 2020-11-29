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
import org.project.eye_game.R;
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
    private DatabaseReference thisRef;

    Boolean isLeft = true;
    Boolean isRight = true;
    Boolean isBoth=true;

    BoardProfile profile;

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(getApplicationContext(), FragmentActivity.class);
        intent.putExtra("id", getIntent().getExtras().getString("id"));
        intent.putExtra("characterID", CHARACTER_ID);
        intent.putExtra("fragmentId", R.id.game_menu);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        finish();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        int screenHeight = getWindowManager().getDefaultDisplay().getHeight();
        profile = new BoardProfile(screenWidth, screenHeight);


        Log.e("Test", "W" + screenWidth + " H" + screenHeight);

        int BOARD_WIDTH = 10;
        int BOARD_HEIGHT = 20;

        Player player = new PlayerImpl(getIntent().getExtras().getString("id"), profile);
        playerInput = new PlayerInputImplForN8(profile);
        playerInput.registerPlayer(player);

        twN8 = new TetrisViewForN8(this, player, profile);
        twN8.setScreenSize(screenWidth,screenHeight);
        id = getIntent().getExtras().getString("id");
        dbRef.child("User").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot keys : snapshot.getChildren()) {
                    String id_ = keys.child("id").getValue(String.class);
                    if (id.equals(id_)) {
                        twN8.loadHIghScore(keys.child("exp3").getValue(int.class));
                        thisRef = keys.getRef();
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
            int scored = twN8.saveScore();
            Log.d("Tetris", "update score1");
            thisRef.child("exp3").setValue(scored);

            id = getIntent().getExtras().getString("id");

            dbRef.child("RankingTetris").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    RankData xx[] = new RankData[10];
                    int i = 0;
                    int minScore = 0;
                    int minIDX = -1;
                    for(DataSnapshot keys: snapshot.getChildren()){
                        if(minScore==0){
                            minScore = keys.child("score").getValue(int.class);
                            minIDX = 0;
                        }
                        xx[i++] = new RankData(keys.child("id").getValue(String.class),
                                keys.child("score").getValue(int.class),
                                keys.child("rank").getValue(int.class));
                        if(keys.child("score").getValue(int.class) < minScore){
                            minScore = keys.child("score").getValue(int.class);
                            minIDX = i-1;
                        }
                        if(keys.child("id").getValue(String.class).equals(id)){
                            xx[i-1].setScore(scored);
                        }
                    }
                    if(i==0){
                        dbRef.child("RankingTetris").push().setValue(new RankData(id, scored, 1));
                        Log.d("Tetris", "update score1");
                        return;
                    }

                    if(minScore < scored){
                        xx[minIDX] = new RankData(id, scored, 0);
                        for(int j = 0; j < i; j++) {
                            for(int k = 0 ; k < i - j - 1 ; k++) {
                                if(xx[k].getScore() < xx[k+1].getScore()) {
                                    RankData temp = new RankData(xx[k+1].getId(), xx[k+1].getScore(), xx[k+1].getRank());
                                    xx[k+1] = xx[k];
                                    xx[k] = temp;
                                }
                            }
                        }
                        dbRef.child("RankingTetris").removeValue();
                        for(int j = 0; j <i; j++){
                            xx[j].setRank(j+1);
                            dbRef.child("RankingTetris").push().setValue(xx[j]);
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
