package org.project.eye_game.interfaces;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.project.eye_game.EyeTrackers.FaceTrackerDaemonProfile;
import org.project.eye_game.R;

import java.io.IOException;

import static android.content.Context.MODE_PRIVATE;

public class FragmentProfile extends Fragment {

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    String email;
    String nickname;
    int TotalRank;
    int TotalEXP;
    String id_;
    int CHARACTER_ID;

    ListView friendList;
    String[] roomKeyList;
    String[] nicknameList;
    int roomKeyPointer;
    FriendListAdapter adapter;

    TextView nicknameView;
    TextView EXPView;
    TextView rankView;
    TextView emailView;
    ImageView characterView;
    ImageView iv_add_friend;
    ImageView iv_edit_nickname;
    ImageView iv_logout;

    CameraSource cameraSource;

    private Context context;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = container.getContext();
        Activity activity = (Activity) context;
        ViewGroup profileView = (ViewGroup) inflater.inflate(R.layout.activity_profile, container, false);

        Bundle bundle = getArguments();
        if(bundle!=null) {
            id_ = bundle.getString("id");
            CHARACTER_ID = bundle.getInt("characterID");
        }
        else {
            id_ = activity.getIntent().getExtras().getString("id");
            CHARACTER_ID = activity.getIntent().getExtras().getInt("characterID");
        }

        characterView = profileView.findViewById(R.id.characterView);
        switch (CHARACTER_ID){
            case 0:
                characterView.setImageResource(R.drawable.character_open);
                break;
            case 1:
                characterView.setImageResource(R.drawable.character2_open);
                break;
            default:
                characterView.setImageResource(R.drawable.character);
        }

        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CAMERA}, 1);
            Toast.makeText(context, "Permission not granted!\n Grant permission and restart app", Toast.LENGTH_SHORT).show();
        }else{
            initCameraSource();
        }

        nicknameView = profileView.findViewById(R.id.nicknameTextView);
        emailView = profileView.findViewById(R.id.emailTextView);

        databaseReference.child("User").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot keys : snapshot.getChildren()) {
                    String _id = keys.child("id").getValue(String.class);
                    if (id_.equals(_id)) {

                        nickname = keys.child("nickname").getValue(String.class);
                        email = keys.child("email").getValue(String.class);

                        nicknameView.append(nickname);
                        emailView.append(email);

                        break;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        roomKeyList = new String[100];
        nicknameList = new String[100];
        roomKeyPointer = -1;

        friendList = profileView.findViewById(R.id.friendListView);
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

        friendList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                databaseReference.child("User").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot keys : snapshot.getChildren()){
                            String _id = keys.child("id").getValue(String.class);
                            if(_id.equals(id_)){
                                Intent intent = new Intent(context, PrivatechatActivity.class);
                                intent.putExtra("id", id_);
                                intent.putExtra("nickname", nickname);
                                intent.putExtra("friendNickname", nicknameList[position]);
                                intent.putExtra("roomKey", roomKeyList[position]);
                                intent.putExtra("characterID", CHARACTER_ID);
                                startActivity(intent);
                                activity.overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                                activity.finish();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });

        iv_edit_nickname = profileView.findViewById(R.id.iv_edit_nickname);
        iv_edit_nickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NicknameChangeActivity.class);
                intent.putExtra("id", id_);
                if (cameraSource!=null) {
                    cameraSource.stop();
                }
                startActivity(intent);
                activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                activity.finish();
            }
        });

        iv_add_friend = profileView.findViewById(R.id.iv_add_friend);
        iv_add_friend.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(context, AddFriendActivity.class);
                intent.putExtra("id", id_);
                intent.putExtra("nickname", nickname);
                intent.putExtra("characterID", CHARACTER_ID);
                startActivity(intent);
                activity.overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                activity.finish();
            }
        });

        iv_logout = profileView.findViewById((R.id.iv_logout));
        iv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = activity.getSharedPreferences("login",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                Intent intent = new Intent(context, LoginActivity.class);
                startActivity(intent);
                activity.overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                activity.finish();
            }
        });
        return profileView;
    }

    private void initCameraSource() {
        FaceDetector detector = new FaceDetector.Builder(context)
                .setTrackingEnabled(true)
                .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                .setMode(FaceDetector.FAST_MODE)
                .build();
        detector.setProcessor(new MultiProcessor.Builder(new FaceTrackerDaemonProfile(context)).build());

        cameraSource = new CameraSource.Builder(context, detector)
                .setRequestedPreviewSize(1024, 768)
                .setFacing(CameraSource.CAMERA_FACING_FRONT)
                .setRequestedFps(5.0f)
                .build();

        try {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            cameraSource.start();
        }
        catch (IOException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        if (cameraSource!=null) {
            cameraSource.stop();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (cameraSource!=null) {
            cameraSource.release();
        }
    }

    public void updateState(String state){
        switch (state){
            case "USER_EYES_OPEN":
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        switch (CHARACTER_ID){
                            case 0:
                                characterView.setImageResource(R.drawable.character_open);
                                break;
                            case 1:
                                characterView.setImageResource(R.drawable.character2_open);
                                break;
                            default:
                                characterView.setImageResource(R.drawable.character);
                                break;
                        }
                    }
                });
                break;
            case "USER_EYES_CLOSED":
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        switch (CHARACTER_ID){
                            case 0:
                                characterView.setImageResource(R.drawable.character_close);
                                break;
                            case 1:
                                characterView.setImageResource(R.drawable.character2_close);
                                break;
                            default:
                                characterView.setImageResource(R.drawable.character);
                                break;
                        }
                    }
                });
                break;
        }
    }
}
