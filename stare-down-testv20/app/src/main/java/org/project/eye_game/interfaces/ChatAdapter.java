package org.project.eye_game.interfaces;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.project.eye_game.R;

import java.util.ArrayList;

public class ChatAdapter extends BaseAdapter {
    ArrayList<ChatMessage> items = new ArrayList<ChatMessage>();
    Context context;
    String nickname;
    LayoutInflater inflater;
    public ChatAdapter(String nickname) { this.nickname = nickname; }
    public void addItem(ChatMessage item){
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
        ChatMessage listItem = items.get(position);

        if(convertView == null){
            inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if(listItem.getNickname().equals(nickname)) {
            convertView = inflater.inflate(R.layout.my_msgbox, parent, false);
        }
        else {
            convertView = inflater.inflate(R.layout.other_msgbox, parent, false);
        }

        TextView tvName= convertView.findViewById(R.id.tv_name);
        TextView tvMsg= convertView.findViewById(R.id.tv_msg);
        TextView tvTime= convertView.findViewById(R.id.tv_time);

        tvName.setText(listItem.getNickname());
        tvMsg.setText(listItem.getMessage());
        tvTime.setText(listItem.getTime());

        return convertView;
    }
}
