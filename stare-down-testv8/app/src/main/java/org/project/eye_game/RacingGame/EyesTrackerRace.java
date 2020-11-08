package org.project.eye_game.RacingGame;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.face.Face;

public class EyesTrackerRace extends Tracker<Face> {
    private final float THRESHOLD = 0.3f;
    private Context context;
    public EyesTrackerRace(Context context) {
        this.context = context;
    }

    @Override
    public void onUpdate(Detector.Detections<Face> detections, Face face) {
        if (face.getIsRightEyeOpenProbability() <= THRESHOLD) {
            Log.i("EyesTrackerRace", "onUpdate: Left Eye close Detected");
            ((RacingActivity)context).updateState("LEFT_EYE_CLOSE");
        }
        else {
            Log.i("EyesTrackerRace", "onUpdate: Left Eye open Detected");
            ((RacingActivity)context).updateState("LEFT_EYE_OPEN");
        }
        if (face.getIsLeftEyeOpenProbability() <= THRESHOLD) {
            Log.i("EyesTrackerRace", "onUpdate: Right Eye close Detected");
            ((RacingActivity)context).updateState("RIGHT_EYE_CLOSE");
        }
        else {
            Log.i("EyesTrackerRace", "onUpdate: Right Eye open Detected");
            ((RacingActivity)context).updateState("RIGHT_EYE_OPEN");
        }
    }

    @Override
    public void onMissing(Detector.Detections<Face> detections) {
        super.onMissing(detections);
        Log.i("EyesTrackerRace", "onMissing: Face Not Detected!");
    }

    @Override
    public void onDone() {
        super.onDone();
    }
}
