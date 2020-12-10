package org.project.eye_game.interfaces;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.project.eye_game.R;
import org.project.eye_game.RacingGame.RacingActivity;
import org.project.eye_game.Tetris.tetrisgame.TetrisActivity;
import org.project.eye_game.characterChange.GameOneActivity;

public class FragmentGame extends Fragment {

    TextView card1;
    TextView card2;
    TextView card3;

    String nickname;
    int CHARACTER_ID;
    String id;

    private Context context;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        context = container.getContext();
        Activity activity = (Activity) context;
        ViewGroup gameView = (ViewGroup) inflater.inflate(R.layout.activity_game, container, false);

        id = activity.getIntent().getExtras().getString("id");
        nickname = activity.getIntent().getExtras().getString("nickname");
        CHARACTER_ID = activity.getIntent().getExtras().getInt("characterID");

        Bundle bundle = getArguments();
        if(bundle!=null) {
            id = bundle.getString("id");
            nickname = bundle.getString("nickname");
            CHARACTER_ID = bundle.getInt("characterID");
        }

        card1 = gameView.findViewById(R.id.characterView);
        card1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(context, GameOneActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("characterID", CHARACTER_ID);
                intent.putExtra("nickname", nickname);
                startActivity(intent);
                activity.overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                activity.finish();
            }
        });

        card2 = gameView.findViewById(R.id.RacingTitleView);
        card2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(context, RacingActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("characterID", CHARACTER_ID);
                intent.putExtra("nickname", nickname);
                startActivity(intent);
                activity.overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                activity.finish();
            }
        });

        card3 = gameView.findViewById(R.id.TetrisTitleView);
        card3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(context, TetrisActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("characterID", CHARACTER_ID);
                intent.putExtra("nickname", nickname);
                startActivity(intent);
                activity.overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                activity.finish();
            }
        });
        return gameView;
    }
}
