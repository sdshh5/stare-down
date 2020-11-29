package org.project.eye_game.interfaces;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.project.eye_game.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FragmentChat extends Fragment {

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    EditText messageView;
    Button sendButton;
    String nickname;
    GridView listView;
    ChatAdapter adapter;
    SimpleDateFormat format1;

    private Context context;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        context = container.getContext();
        Activity activity = (Activity) context;
        ViewGroup chatView = (ViewGroup) inflater.inflate(R.layout.activity_global_chat, container, false);

        Bundle bundle = getArguments();
        if(bundle!=null) {
            nickname = bundle.getString("nickname");
        }
        format1 = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");

        messageView = chatView.findViewById(R.id.messageEditText);
        sendButton = chatView.findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String message = messageView.getText().toString();
                if(message.equals("")){
                    return;
                }
                ChatMessage chat = new ChatMessage(nickname, message, format1.format(new Date()));
                databaseReference.child("GlobalChat").push().setValue(chat);
                messageView.setText("");
            }
        });

        listView =  chatView.findViewById(R.id.listView);
        adapter = new ChatAdapter();

        databaseReference.child("GlobalChat").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()==false){
                    return;
                }
                for(DataSnapshot keys : snapshot.getChildren()){
                    String nickname_ = keys.child("nickname").getValue(String.class);
                    String message = keys.child("message").getValue(String.class);
                    String time = keys.child("time").getValue(String.class);
                    ChatMessage chat = new ChatMessage(nickname_, message, time);
                    if(nickname_.equals(nickname)){
                        chat.setGravity(1);
                    }
                    else{
                        chat.setGravity(0);
                    }
                    adapter.addItem(chat);
                }
                listView.setAdapter(adapter);
                listView.post(new Runnable() {
                    @Override
                    public void run() {
                        listView.setSelection(adapter.getCount() - 1);
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        databaseReference.child("GlobalChat").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int count=0;
                if(snapshot.exists()==false){
                    return;
                }
                for(DataSnapshot keys : snapshot.getChildren()){
                    count++;
                    if(count>adapter.getCount()){
                        String nickname_ = keys.child("nickname").getValue(String.class);
                        String message = keys.child("message").getValue(String.class);
                        String time = keys.child("time").getValue(String.class);
                        ChatMessage chat = new ChatMessage(nickname_, message, time);
                        if(nickname_.equals(nickname)){
                            chat.setGravity(1);
                        }
                        else{
                            chat.setGravity(0);
                        }
                        adapter.addItem(chat);
                    }
                }
                listView.setAdapter(adapter);
                listView.post(new Runnable() {
                    @Override
                    public void run() {
                        listView.setSelection(adapter.getCount() - 1);
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
        return chatView;
    }
}
