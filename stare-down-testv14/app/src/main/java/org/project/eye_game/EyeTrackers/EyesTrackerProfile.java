package org.project.eye_game.EyeTrackers;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.face.Face;

import org.project.eye_game.interfaces.FragmentActivity;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class EyesTrackerProfile extends Tracker<Face> {
    private final float THRESHOLD = 0.01f;
    private Context context;
    public EyesTrackerProfile(Context context) {
        this.context = context;
    }

    @Override
    public void onUpdate(Detector.Detections<Face> detections, Face face) {
        if (face.getIsLeftEyeOpenProbability() > THRESHOLD && face.getIsRightEyeOpenProbability() > THRESHOLD) {
            Log.i(TAG, "onUpdate: Open Eyes Detected");
            ((FragmentActivity)context).updateState("FragmentProfile","USER_EYES_OPEN");
        }
        else {
            Log.i(TAG, "onUpdate: Close Eyes Detected");
            ((FragmentActivity)context).updateState("FragmentProfile","USER_EYES_CLOSED");
        }
    }

    @Override
    public void onMissing(Detector.Detections<Face> detections) {
        super.onMissing(detections);
        Log.i(TAG, "onUpdate: Face Not Detected!");
    }

    @Override
    public void onDone() {
        super.onDone();
    }
}