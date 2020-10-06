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

public class MenuActivity extends AppCompatActivity {
    private Button loginButton;
    private Button registButton;
    private EditText emailEditText;
    private EditText pwEditText;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

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

                firebaseAuth.signInWithEmailAndPassword(email, pw).addOnCompleteListener(MenuActivity.this, new OnCompleteListener<AuthResult>(){
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task){
                        if(task.isSuccessful()){
                            Intent intent = new Intent(MenuActivity.this, MenuActivity2.class);
                            startActivity(intent);
                        } else{
                            Toast.makeText(MenuActivity.this, "Log In Failure", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        registButton.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v){
               Intent intent = new Intent(MenuActivity.this, RegistActivity.class);
               startActivity(intent);
           }
        });
    }
}