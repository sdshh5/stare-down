package org.project.eye_game.interfaces;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.project.eye_game.R;
import org.project.eye_game.interfaces.FriendData;

import java.util.ArrayList;

public class FriendListAdapter extends BaseAdapter {
    ArrayList<FriendData> items = new ArrayList<FriendData>();
    Context context;

    public void addItem(FriendData item){
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
        FriendData listItem = items.get(position);

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.friend_item, parent, false);
        }

        TextView nickname = convertView.findViewById(R.id.nicknameTextView);

        nickname.setText(listItem.friendNickname);

        return convertView;
    }
}
