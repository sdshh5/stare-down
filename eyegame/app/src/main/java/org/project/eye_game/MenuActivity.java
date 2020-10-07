package org.project.eye_game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MenuActivity extends AppCompatActivity {
    ImageButton settingButton;
    TextView nicknameTextView;
    String nickname;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Intent receivedIntent = getIntent();
        nickname = receivedIntent.getExtras().getString("nickname");
        email = receivedIntent.getExtras().getString("email");

        Toast.makeText(getApplicationContext(), "Hello, " + nickname + "!", Toast.LENGTH_LONG);

        nicknameTextView = (TextView)findViewById(R.id.nicknameTextView);
        nicknameTextView.append(nickname+"!");

        settingButton = (ImageButton)findViewById(R.id.SettingButton);
        settingButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
                intent.putExtra("nickname", nickname);
                intent.putExtra("email", email);
                startActivity(intent);
            }
        });

    }
}