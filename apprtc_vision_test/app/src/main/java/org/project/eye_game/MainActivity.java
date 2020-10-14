package org.project.eye_game;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.face.FaceDetector;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private Toast toast;
    private long backKeyPressedTime = 0;

    CameraSource cameraSource;
    TextView textView;
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

        textView = findViewById(R.id.titleTextView);
        Button startButton = (Button)findViewById(R.id.button);
        startButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
            Toast.makeText(this, "Permission not granted!\n Grant permission and restart app", Toast.LENGTH_SHORT).show();
        }else{
            initCameraSource();
        }
    }

    //method to create camera source from faceFactoryDaemon class
    private void initCameraSource() {
      FaceDetector detector = new FaceDetector.Builder(this)
              .setTrackingEnabled(true)
              .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
              .setMode(FaceDetector.FAST_MODE)
              .build();
      detector.setProcessor(new MultiProcessor.Builder(new FaceTrackerDaemon(MainActivity.this)).build());

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
        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        e.printStackTrace();
      }
    }

    @Override
    protected void onResume() {
      super.onResume();
      if (cameraSource != null) {
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
          e.printStackTrace();
        }
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
              textView.setText("eye open");
            }
          });
          break;
        case "USER_EYES_CLOSED":
          runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText("eye close");
            }
          });
          break;
        case "FACE_NOT_FOUND":
          runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText("face not found");
            }
          });
          break;
      }
    }

}