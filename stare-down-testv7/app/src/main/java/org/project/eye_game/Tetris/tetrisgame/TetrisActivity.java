package org.project.eye_game.Tetris.tetrisgame;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.project.eye_game.EyeTrackers.FaceTrackerDaemonGameTwo;
import org.project.eye_game.Tetris.player.Player;
import org.project.eye_game.Tetris.player.PlayerImpl;

import java.io.IOException;

public class TetrisActivity extends AppCompatActivity {
    private TetrisViewForN8 twN8;
    PlayerInputImplForN8 playerInput;
    CameraSource cameraSource;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        int screenHeight = getWindowManager().getDefaultDisplay().getHeight();

        Log.e("Test", "W" + screenWidth + " H" + screenHeight);

        int BOARD_WIDTH = 10;
        int BOARD_HEIGHT = 20;

        Player player = new PlayerImpl(BOARD_WIDTH, BOARD_HEIGHT);

        twN8 = new TetrisViewForN8(this, player);
        twN8.setScreenSize(screenWidth,screenHeight);
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
            Toast.makeText(TetrisActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }


    }

    public void updateState(String state){
        switch(state){
            case "USER_EYES_OPEN":
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
                        playerInput.downCall();
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
        }

    }


    @Override
    public void onResume() {
        super.onResume();
        if (twN8 != null) {
            twN8.resumeGame();
        }
    }
}
