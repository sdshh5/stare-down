package org.project.eye_game.EyeTrackers;

import android.content.Context;

import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.face.Face;

public class FaceTrackerDaemonCharacter implements MultiProcessor.Factory<Face> {
    private Context context;
    public FaceTrackerDaemonCharacter(Context context) {
        this.context = context;
    }

    @Override
    public Tracker<Face> create(Face face) {
        return new EyesTrackerCharacter(context);
    }
}
