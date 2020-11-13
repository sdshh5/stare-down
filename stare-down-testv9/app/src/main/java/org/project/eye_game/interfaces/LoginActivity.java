package org.project.eye_game;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

public class LoginActivity extends AppCompatActivity {
    private Button loginButton;
    private Button registButton;
    private EditText emailEditText;
    private EditText pwEditText;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    private Toast toast;
    private long backKeyPressedTime = 0;

    private View decorView;
    private int	uiOption;

    @Override
    public void onBackPressed(){
        if (System.currentTimeMillis() > backKeyPressedTime + 2500) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(this, "Press One More Time", Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2500) {
            finish();
            toast.cancel();
            toast = Toast.makeText(this,"Bye bye~",Toast.LENGTH_LONG);
            toast.show();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        decorView = getWindow().getDecorView();
        uiOption = getWindow().getDecorView().getSystemUiVisibility();
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH )
            uiOption |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN )
            uiOption |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT )
            uiOption |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        decorView.setSystemUiVisibility( uiOption );

        loginButton = (Button)findViewById(R.id.logInButton);
        registButton = (Button)findViewById(R.id.registerButton);
        emailEditText = (EditText)findViewById(R.id.loginEmailEditText);
        pwEditText = (EditText)findViewById(R.id.loginPasswordEditText);

        firebaseAuth = firebaseAuth.getInstance();

        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String email = emailEditText.getText().toString().trim();
                String pw = pwEditText.getText().toString().trim();
                if(email.equals("")||pw.equals("")){
                    Toast.makeText(getApplicationContext(), "Retry!", Toast.LENGTH_LONG);
                    return;
                }
                firebaseAuth.signInWithEmailAndPassword(email, pw).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>(){
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task){
                        if(task.isSuccessful()){
                            databaseReference.child("User").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for(DataSnapshot keys : snapshot.getChildren()){
                                        String userEmail = keys.child("email").getValue(String.class);
                                        if(email.equals(userEmail)){
                                            String userNickname = keys.child("nickname").getValue(String.class);
                                            String id = keys.child("id").getValue(String.class);
                                            int characterID = keys.child("character").getValue(int.class);
                                            Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                                            intent.putExtra("id", id);
                                            intent.putExtra("characterID", characterID);
                                            Toast.makeText(getApplicationContext(), "Hello, " + userNickname + "!", Toast.LENGTH_LONG).show();
                                            emailEditText.setText("");
                                            pwEditText.setText("");
                                            startActivity(intent);
                                            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                                            finish();
                                        }
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) { }
                            });
                        } else{
                            Toast.makeText(LoginActivity.this, "Log In Failure", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        registButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(LoginActivity.this, RegistActivity.class);
                emailEditText.setText("");
                pwEditText.setText("");
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                finish();
            }
        });
    }

}