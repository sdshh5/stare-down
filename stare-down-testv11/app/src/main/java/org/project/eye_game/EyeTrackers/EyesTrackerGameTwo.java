package org.project.eye_game.EyeTrackers;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.face.Face;

import org.project.eye_game.Tetris.tetrisgame.TetrisActivity;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class EyesTrackerGameTwo extends Tracker<Face> {
    private final float THRESHOLD = 0.03f;
    private Context context;
    public EyesTrackerGameTwo(Context context) {
        this.context = context;
    }

    public void onUpdate(Detector.Detections<Face> detections, Face face) {
        if(face.getIsLeftEyeOpenProbability()<=THRESHOLD && face.getIsRightEyeOpenProbability()<=THRESHOLD){
            Log.i("EyesTrackerGameTwo", "onUpdate: Eye CLOSE Detected");
            ((TetrisActivity)context).updateState(("USER_EYES_CLOSED"));
        }
        else if(face.getIsLeftEyeOpenProbability()<=THRESHOLD && face.getIsRightEyeOpenProbability()>THRESHOLD){
            Log.i("EyesTrackerGameTwo", "onUpdate: Left EYE CLOSE Detected");
            ((TetrisActivity)context).updateState(("LEFT_EYE_CLOSE"));
        }
        else if(face.getIsLeftEyeOpenProbability()>THRESHOLD && face.getIsRightEyeOpenProbability()<=THRESHOLD){
            Log.i("EyesTrackerGameTwo", "onUpdate: Right EYE CLOSE Detected");
            ((TetrisActivity)context).updateState(("RIGHT_EYE_CLOSE"));
        }
        else{
            Log.i("EyesTrackerGameTwo", "onUpdate: Eye OPEN Detected");
            ((TetrisActivity)context).updateState(("USER_EYES_OPEN"));
        }
    }

    @Override
    public void onMissing(Detector.Detections<Face> detections) {
        super.onMissing(detections);
        Log.i(TAG, "onUpdate: Face Not Detected!");
        ((TetrisActivity)context).updateState("FACE_NOT_FOUND");
    }

    @Override
    public void onDone() {
        super.onDone();
    }
}
