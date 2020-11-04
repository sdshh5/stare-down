package org.project.eye_game;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.util.Strings;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FriendActivity extends AppCompatActivity {
    Button friendAddButton;

    String id_;

    ListView friendList;
    String[] roomKeyList;
    String[] nicknameList;
    int roomKeyPointer;
    FriendListAdapter adapter;

    private View decorView;
    private int	uiOption;

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = db.getReference();

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(getApplicationContext(), CharacterActivity.class);
        intent.putExtra("id", getIntent().getExtras().getString("id"));
        intent.putExtra("characterID", getIntent().getExtras().getInt("characterID"));
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        decorView = getWindow().getDecorView();
        uiOption = getWindow().getDecorView().getSystemUiVisibility();
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH )
            uiOption |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN )
            uiOption |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT )
            uiOption |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        decorView.setSystemUiVisibility( uiOption );

        roomKeyList = new String[100];
        nicknameList = new String[100];
        roomKeyPointer = -1;

        id_ = getIntent().getExtras().getString("id");

        friendList = (ListView) findViewById(R.id.friendListView);
        adapter = new FriendListAdapter();
        databaseReference.child("friendList").child(id_).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot keys : snapshot.getChildren()) {
                    FriendData friend = new FriendData(keys.child("friendNickname").getValue(String.class),
                            keys.child("friendID").getValue(String.class),
                            keys.child("roomKey").getValue(String.class));
                    adapter.addItem(friend);
                    roomKeyList[++roomKeyPointer] = friend.getRoomKey();
                    nicknameList[roomKeyPointer] = friend.getFriendNickname();
                }
                friendList.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        friendList = (ListView) findViewById(R.id.friendListView);
        friendList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                databaseReference.child("User").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot keys : snapshot.getChildren()){
                            String _id = keys.child("id").getValue(String.class);
                            if(_id.equals(id_)){
                                Log.d("INTOPRIVATE", "True");
                                Intent intent = new Intent(getApplicationContext(), PrivatechatActivity.class);
                                intent.putExtra("id", id_);
                                intent.putExtra("nickname", keys.child("nickname").getValue(String.class));
                                intent.putExtra("friendNickname", nicknameList[position]);
                                intent.putExtra("roomKey", roomKeyList[position]);
                                intent.putExtra("characterID", getIntent().getExtras().getInt("characterID"));
                                startActivity(intent);
                                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                                finish();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        friendAddButton = findViewById(R.id.goSearchButton);
        friendAddButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), AddFriendActivity.class);
                intent.putExtra("id", getIntent().getExtras().getString("id"));
                intent.putExtra("characterID", getIntent().getExtras().getInt("characterID"));
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                finish();
            }
        });
    }
}