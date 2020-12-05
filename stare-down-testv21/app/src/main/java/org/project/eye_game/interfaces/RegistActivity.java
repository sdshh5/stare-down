package org.project.eye_game.interfaces;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.project.eye_game.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RegistActivity extends AppCompatActivity {
    private EditText newEmailEditText;
    private EditText newPWEditText;
    private EditText nicknameEditText;
    private Button registButton;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    SimpleDateFormat format1;

    private View decorView;
    private int	uiOption;

    boolean result;

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        decorView = getWindow().getDecorView();
        uiOption = getWindow().getDecorView().getSystemUiVisibility();
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH )
            uiOption |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN )
            uiOption |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT )
            uiOption |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        decorView.setSystemUiVisibility( uiOption );

        format1 = new SimpleDateFormat("yyyyMMddHHmmss");

        newEmailEditText = (EditText)findViewById(R.id.newEmailEditText);
        newPWEditText = (EditText)findViewById(R.id.newPasswordEditText);
        nicknameEditText = (EditText)findViewById(R.id.nicknameEditText);
        registButton = (Button)findViewById(R.id.registerButton);


        firebaseAuth = firebaseAuth.getInstance();
        registButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                databaseReference.child("User").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String tar;
                        String email = newEmailEditText.getText().toString().trim();
                        String pw = newPWEditText.getText().toString().trim();
                        String nickname = nicknameEditText.getText().toString().trim();
                        for(DataSnapshot keys: snapshot.getChildren()){
                            tar = keys.child("nickname").getValue(String.class);
                            if(tar.equals(nickname)){
                                Toast.makeText(getApplicationContext(), "Being Used Nickname", Toast.LENGTH_SHORT).show();
                                newEmailEditText.setText("");
                                newPWEditText.setText("");
                                nicknameEditText.setText("");
                                return;
                            }
                        }
                        if(email.equals("")||pw.equals("")||nickname.equals("")){
                            Toast.makeText(getApplicationContext(), "Retry!", Toast.LENGTH_LONG).show();
                            return;
                        }

                        firebaseAuth.createUserWithEmailAndPassword(email, pw).addOnCompleteListener(RegistActivity.this, new OnCompleteListener<AuthResult>(){
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task){
                                if(task.isSuccessful()){
                                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                    UserData userData = new UserData(email, nickname, format1.format(new Date()));
                                    databaseReference.child("User").push().setValue(userData);
                                    Toast.makeText(getApplicationContext(), "Welcome, " + nickname + "!", Toast.LENGTH_LONG).show();
                                    newEmailEditText.setText("");
                                    newPWEditText.setText("");
                                    nicknameEditText.setText("");
                                    startActivity(intent);
                                    overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                                    finish();
                                }else{
                                    Toast.makeText(RegistActivity.this, "REGIST Failure", Toast.LENGTH_LONG).show();
                                    return;
                                }
                            }
                        });
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });
    }

}

