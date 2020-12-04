package org.project.eye_game.interfaces;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.project.eye_game.R;
import org.project.eye_game.Tetris.tetris.Tetris;

public class FragmentRank extends Fragment {

    GameDataAdapter adapterRace;
    GameDataAdapter adapterTetris;

    ListView RacingRankingView;
    ListView TetrisRankingView;

    String id;
    String nickname;
    String nick;
    int i=0, j=0, x=0;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference dbRef = firebaseDatabase.getReference();
    private DatabaseReference raceRef = dbRef.child("RankingRacing").getRef();
    private DatabaseReference tetRef = dbRef.child("RankingTetris").getRef();
    private DatabaseReference userRef = dbRef.child("User").getRef();

    GameData RaceData[] = new GameData[10];
    GameData TetData[] = new GameData[10];

    private Context context;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = container.getContext();
        Activity activity = (Activity) context;

        ViewGroup rankView = (ViewGroup) inflater.inflate(R.layout.activity_rank, container, false);

        RacingRankingView = (ListView) rankView.findViewById(R.id.RacingRankView);
        TetrisRankingView = (ListView) rankView.findViewById(R.id.TetrisRankView);

        Bundle bundle = getArguments();
        if(bundle!=null)
            id = bundle.getString("id");

        adapterRace = new GameDataAdapter();
        adapterTetris = new GameDataAdapter();

        raceRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot keys : snapshot.getChildren()) {
                        GameData tmp = new GameData(keys.child("nickname").getValue(String.class),
                            keys.child("score").getValue(int.class), keys.child("rank").getValue(int.class));
                        adapterRace.addItem(tmp);
                        Log.d("RANK FRAGMENT", tmp.getNickname());
                    }
                    RacingRankingView.setAdapter(adapterRace);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            }
        );
        tetRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot keys: snapshot.getChildren()){
                        GameData tmp = new GameData(keys.child("nickname").getValue(String.class),
                            keys.child("score").getValue(int.class), keys.child("rank").getValue(int.class));
                        adapterTetris.addItem(tmp);
                        Log.d("RANK FRAGMENT", tmp.getNickname());
                    }
                    TetrisRankingView.setAdapter(adapterTetris);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            }
        );

        return rankView;
    }

}
