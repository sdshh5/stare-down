package org.project.eye_game.EyeTrackers;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.face.Face;

import org.project.eye_game.GameOneActivity;
import org.project.eye_game.MainActivity;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class EyesTrackerGameOne extends Tracker<Face> {
    private final float THRESHOLD = 0.01f;
    private Context context;
    public EyesTrackerGameOne(Context context) {
        this.context = context;
    }

    @Override
    public void onUpdate(Detector.Detections<Face> detections, Face face) {
        if (face.getIsLeftEyeOpenProbability() > THRESHOLD && face.getIsRightEyeOpenProbability() > THRESHOLD) {
            Log.i(TAG, "onUpdate: Open Eyes Detected");
            ((GameOneActivity)context).updateState("USER_EYES_OPEN");
        }
        else {
            Log.i(TAG, "onUpdate: Close Eyes Detected");
            ((GameOneActivity)context).updateState("USER_EYES_CLOSED");
        }
    }






    @Override
    public void onMissing(Detector.Detections<Face> detections) {
        super.onMissing(detections);
        Log.i(TAG, "onUpdate: Face Not Detected!");
        ((GameOneActivity)context).updateState("FACE_NOT_FOUND");
    }

    @Override
    public void onDone() {
        super.onDone();
    }
}