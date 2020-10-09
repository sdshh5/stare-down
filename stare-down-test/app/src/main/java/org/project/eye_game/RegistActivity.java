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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistActivity extends AppCompatActivity {
    private EditText newEmailEditText;
    private EditText newPWEditText;
    private EditText nicknameEditText;
    private Button registButton;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        newEmailEditText = (EditText)findViewById(R.id.newEmailEditText);
        newPWEditText = (EditText)findViewById(R.id.newPasswordEditText);
        nicknameEditText = (EditText)findViewById(R.id.nicknameEditText);
        registButton = (Button)findViewById(R.id.registerButton);

        firebaseAuth = firebaseAuth.getInstance();
        registButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String email = newEmailEditText.getText().toString().trim();
                String pw = newPWEditText.getText().toString().trim();
                String nickname = nicknameEditText.getText().toString().trim();
                UserData userData = new UserData(email, nickname);
                if(email.equals("")||pw.equals("")||nickname.equals("")){
                    Toast.makeText(getApplicationContext(), "Retry!", Toast.LENGTH_LONG).show();
                    return;
                }
                firebaseAuth.createUserWithEmailAndPassword(email, pw).addOnCompleteListener(RegistActivity.this, new OnCompleteListener<AuthResult>(){
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task){
                        if(task.isSuccessful()){
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            databaseReference.child("User").push().setValue(userData);
                            Toast.makeText(getApplicationContext(), "Welcome, " + nickname + "!", Toast.LENGTH_LONG).show();
                            newEmailEditText.setText("");
                            newPWEditText.setText("");
                            nicknameEditText.setText("");
                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(RegistActivity.this, "REGIST Failure", Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                });
            }
        });
    }

}

