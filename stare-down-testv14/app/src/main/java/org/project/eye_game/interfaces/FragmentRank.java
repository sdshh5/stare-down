package org.project.eye_game.interfaces;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.vision.CameraSource;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.project.eye_game.R;

public class FragmentRank extends Fragment {

    int Rank1;
    int Rank2;
    int Rank3;
    int EXP1;
    int EXP2;
    int EXP3;
    GameDataAdapter adapter;
    GridView gridView;
    String id;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    private Context context;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = container.getContext();
        Activity activity = (Activity) context;
        ViewGroup rankView = (ViewGroup) inflater.inflate(R.layout.activity_rank, container, false);

        Bundle bundle = getArguments();
        if(bundle!=null)
            id = bundle.getString("id");

        databaseReference.child("User").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot keys : snapshot.getChildren()) {
                    String _id = keys.child("id").getValue(String.class);
                    if (id.equals(_id)) {
                        Rank1 = keys.child("rank1").getValue(int.class);
                        Rank2 = keys.child("rank2").getValue(int.class);
                        Rank3 = keys.child("rank3").getValue(int.class);
                        EXP1 = keys.child("exp1").getValue(int.class);
                        EXP2 = keys.child("exp2").getValue(int.class);
                        EXP3 = keys.child("exp3").getValue(int.class);

                        gridView = rankView.findViewById(R.id.characterGridView);
                        adapter = new GameDataAdapter();
                        adapter.addItem(new GameData("Game1", EXP1, Rank1));
                        adapter.addItem(new GameData("Game2", EXP2, Rank2));
                        adapter.addItem(new GameData("Game3", EXP3, Rank3));
                        gridView.setAdapter(adapter);
                        break;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        return rankView;
    }
}
