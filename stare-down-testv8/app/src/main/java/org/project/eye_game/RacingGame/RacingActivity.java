package org.project.eye_game.RacingGame;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.face.FaceDetector;

import org.project.eye_game.EyeTrackers.FaceTrackerDaemonGameOne;
import org.project.eye_game.GameOneActivity;
import org.project.eye_game.MenuActivity;
import org.project.eye_game.R;

import java.io.IOException;

public class RacingActivity extends AppCompatActivity {

    private static final int INIT_SPEED = 5;
    private static final int SPEED_INTERVAL = 1;
    private static final int MAX_SPEED = 10;

    private View contLabel;
    private TextView tvNotify;
    private TextView tvScore, tvLevel, tvBest;
    private ImageView ivCenter;
    private RacingView racingView;
    private int score, level, bestScore;
    private int playCount;

    Boolean isLeft = true;
    Boolean isRight = true;
    CameraSource cameraSource;
    String id;

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
        intent.putExtra("id", getIntent().getExtras().getString("id"));
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_race);

        id = getIntent().getExtras().getString("id");

        contLabel = findViewById(R.id.contNotify);
        tvNotify = (TextView) findViewById(R.id.notify);

        tvScore = (TextView) findViewById(R.id.score);
        tvLevel = (TextView) findViewById(R.id.level);
        tvBest = (TextView) findViewById(R.id.best);

        ivCenter = (ImageView) findViewById(R.id.imgCenter);
        ivCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play();
            }
        });

        racingView = (RacingView) findViewById(R.id.racingView);

        playCount = 0;
        initialize();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
            Toast.makeText(this, "Permission not granted!\n Grant permission and restart app", Toast.LENGTH_SHORT).show();
        } else {
            initCameraSource();
        }
    }

    private void initCameraSource() {
        FaceDetector detector = new FaceDetector.Builder(this)
                .setTrackingEnabled(true)
                .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                .setMode(FaceDetector.FAST_MODE)
                .build();
        detector.setProcessor(new MultiProcessor.Builder(new FaceTrackerDaemonRace(RacingActivity.this)).build());

        cameraSource = new CameraSource.Builder(this, detector)
                .setRequestedPreviewSize(1024, 768)
                .setFacing(CameraSource.CAMERA_FACING_FRONT)
                .setRequestedFps(20.0f)
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
        } catch (IOException e) {
            Toast.makeText(RacingActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (racingView != null && racingView.getPlayState() == RacingView.PlayState.Pause) {
            racingView.resume();
        }
        if (cameraSource != null) {
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
            try {
                cameraSource.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onPause() {
        if (racingView != null && racingView.getPlayState() == RacingView.PlayState.Playing) {
            pause();
        }
        super.onPause();
        if (cameraSource!=null) {
            cameraSource.stop();
        }
    }

    @Override
    protected void onDestroy() {
        racingView.reset();
        super.onDestroy();
        if (cameraSource!=null) {
            cameraSource.release();
        }
    }

    public void updateState(String state){
        switch (state){
            case "LEFT_EYE_CLOSE":
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        isLeft = false;
                    }
                });
                break;
            case "RIGHT_EYE_CLOSE":
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        isRight = false;
                    }
                });
                break;
            case "LEFT_EYE_OPEN":
                if(isLeft==false){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("left","moving left");
                            racingView.left();
                            isLeft = true;
                        }
                    });
                }
                break;
            case "RIGHT_EYE_OPEN":
                if(isRight==false) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("right","moving right");
                            racingView.right();
                            isRight = true;
                        }
                    });
                }
                break;
        }
    }

    private Handler racingHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RacingView.MSG_SCORE:
                    score = score + (level);
                    tvScore.setText(String.valueOf(score));
                    break;
                case RacingView.MSG_COLLISION:
                    boolean achieveBest = false;
                    if (bestScore < score) {
                        tvBest.setText(String.valueOf(score));
                        bestScore = score;
                        saveBestScore(bestScore);
                        achieveBest = true;
                    }
                    collision(achieveBest);
                    break;
                case RacingView.MSG_COMPLETE:
                    level++;

                    if (racingView.getSpeed() < MAX_SPEED) {
                        racingView.setSpeed(racingView.getSpeed() + SPEED_INTERVAL);
                    }

                    tvLevel.setText(String.valueOf(level));
                    prepare();
                    break;
                default:
                    break;
            }
        }
    };

    private void initialize() {
        reset();
        prepare();
    }

    private int loadBestScore() {
        SharedPreferences preferences = getSharedPreferences("RacingGame", Context.MODE_PRIVATE);
        if (preferences.contains("BestScore")) {
            return preferences.getInt("BestScore", 0);
        } else {
            return 0;
        }
    }

    private void saveBestScore(int bestScore) {
        SharedPreferences preferences = getSharedPreferences("RacingGame", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("BestScore", bestScore);
        editor.commit();
    }

    private void reset() {
        score = 0;
        level = 1;
        bestScore = loadBestScore();

        racingView.setSpeed(INIT_SPEED);
        racingView.setPlayState(RacingView.PlayState.Ready);

        tvScore.setText(String.valueOf(score));
        tvLevel.setText(String.valueOf(level));
        tvBest.setText(String.valueOf(bestScore));
    }

    private void prepare() {
        tvLevel.setText(String.valueOf(level));
        tvNotify.setText("LEVEL " + level);
        showLabelContainer();

        ivCenter.setImageResource(R.drawable.ic_play);
    }

    private void play() {
        if (racingView.getPlayState() == RacingView.PlayState.Collision) {
            initialize();

            racingView.reset();
            return;
        }

        // Click on playing
        if (racingView.getPlayState() == RacingView.PlayState.Playing) {
            pause();
        } else {
            ivCenter.setImageResource(R.drawable.ic_pause);

            showArrowToast();

            // Click on pause
            if (racingView.getPlayState() == RacingView.PlayState.Pause) {
                racingView.resume();
            } else if (racingView.getPlayState() == RacingView.PlayState.LevelUp) {
                racingView.resume();
                hideLabelContainer();
            } else {
                // Click on stop
                playCount++;
                hideLabelContainer();
                racingView.play(racingHandler);
            }
        }
    }

    private void pause() {
        ivCenter.setImageResource(R.drawable.ic_play);
        racingView.pause();
    }

    private void collision(boolean achieveBest) {
        if (achieveBest) {
            tvNotify.setText("Congratulation!\nYou are the Best!");
        } else {
            tvNotify.setText("Try again!");
        }

        contLabel.setVisibility(View.VISIBLE);
        contLabel.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left));

        ivCenter.setImageResource(R.drawable.ic_retry);
    }

    private void showLabelContainer() {
        contLabel.setVisibility(View.VISIBLE);
        contLabel.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left));
    }

    private void hideLabelContainer() {
        Animation anim = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                contLabel.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        contLabel.startAnimation(anim);
    }

    private void showArrowToast() {
        final View v = findViewById(R.id.toast);
        Animation anim = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                v.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        hideArrowToast();
                    }
                }, 1000);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        v.startAnimation(anim);
    }

    private void hideArrowToast() {
        final View v = findViewById(R.id.toast);
        Animation anim = AnimationUtils.loadAnimation(this, android.R.anim.fade_out);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                v.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        v.startAnimation(anim);
    }
}
