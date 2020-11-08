package org.project.eye_game.EyeTrackers;

import android.content.Context;

import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.face.Face;

public class FaceTrackerDaemonGameOne implements MultiProcessor.Factory<Face> {
    private Context context;
    public FaceTrackerDaemonGameOne(Context context) {
        this.context = context;
    }

    @Override
    public Tracker<Face> create(Face face) {
        return new EyesTrackerGameOne(context);
    }
}
