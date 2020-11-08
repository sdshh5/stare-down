package org.project.eye_game.RacingGame;

import android.content.Context;

import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.face.Face;

public class FaceTrackerDaemonRace implements MultiProcessor.Factory<Face> {
    private Context context;
    public FaceTrackerDaemonRace(Context context) {
        this.context = context;
    }

    @Override
    public Tracker<Face> create(Face face) {return new EyesTrackerRace(context);}
}
