package org.project.eye_game.interfaces;

import android.content.Context;
import android.view.Gravity;
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
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.message_item, parent, false);
        }

        TextView message = convertView.findViewById(R.id.messageTextView_);
        TextView time = convertView.findViewById(R.id.timeTextView_);

        message.setText(listItem.getNickname());
        message.append(" : " + listItem.getMessage());
        time.setText(listItem.getTime());

        if(listItem.getGravity()==0){
            message.setGravity(Gravity.LEFT);
            time.setGravity(Gravity.LEFT);
        }
        else{
            message.setGravity(Gravity.RIGHT);
            time.setGravity(Gravity.RIGHT);
        }

        return convertView;
    }
}
