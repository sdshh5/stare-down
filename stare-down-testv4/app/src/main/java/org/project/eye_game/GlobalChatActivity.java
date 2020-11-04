package org.project.eye_game;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GlobalChatActivity extends AppCompatActivity {
    EditText messageView;
    Button sendButton;
    String nickname;

    GridView listView;
    ChatAdapter adapter;
    SimpleDateFormat format1;

    private View decorView;
    private int	uiOption;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
        intent.putExtra("id", getIntent().getExtras().getString("id"));
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_global_chat);

        decorView = getWindow().getDecorView();
        uiOption = getWindow().getDecorView().getSystemUiVisibility();
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH )
            uiOption |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN )
            uiOption |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT )
            uiOption |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        decorView.setSystemUiVisibility( uiOption );

        format1 = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");
        nickname = getIntent().getExtras().getString("nickname");

        messageView = findViewById(R.id.messageEditText);
        sendButton = findViewById(R.id.sendButton);
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

        listView =  findViewById(R.id.listView);
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


    }
}