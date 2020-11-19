package org.project.eye_game.interfaces;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.project.eye_game.R;
import org.project.eye_game.interfaces.GameData;

import java.util.ArrayList;

public class GameDataAdapter extends BaseAdapter {
    ArrayList<GameData> items = new ArrayList<GameData>();
    Context context;

    public void addItem(GameData item){
        items.add(item);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        context = parent.getContext();
        GameData listItem = items.get(position);

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.game_data_item, parent, false);
        }

        TextView gameTitle = convertView.findViewById(R.id.gameTitleTextView);
        TextView gameRank = convertView.findViewById(R.id.gameRankingTextView);
        TextView gameEXP = convertView.findViewById(R.id.gameEXPTextView);

        gameTitle.setText(listItem.getGameTitle());
        if(listItem.getGameRanking()==0){
            gameRank.setText("RANK: NONE");
        }else{
            gameRank.setText("RANK: "+Integer.toString(listItem.getGameRanking()));
        }

        gameEXP.setText("EXP: "+Integer.toString(listItem.getGameEXP()));

        return convertView;
    }
}
