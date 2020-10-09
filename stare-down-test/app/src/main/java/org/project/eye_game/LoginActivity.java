package org.project.eye_game;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
                                        String userEmail = keys.child("userEmail").getValue(String.class);
                                        if(email.equals(userEmail)){
                                            String userNickname = keys.child("userNickname").getValue(String.class);
                                            Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                                            intent.putExtra("nickname", userNickname);
                                            intent.putExtra("email", userEmail);
                                            Toast.makeText(getApplicationContext(), "Hello, " + userNickname + "!", Toast.LENGTH_LONG).show();
                                            emailEditText.setText("");
                                            pwEditText.setText("");
                                            startActivity(intent);
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
                finish();
            }
        });
    }

}