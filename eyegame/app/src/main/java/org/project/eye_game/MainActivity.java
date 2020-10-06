package org.project.eye_game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView titleLoadTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        titleLoadTextView = (TextView)findViewById(R.id.titleLoadTextView);
        playTitleLoadTextView();

        Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
        startActivity(intent);
    }

    private void playTitleLoadTextView(){
        for(int i=0; i<3; i++){
            titleLoadTextView.setText("Loading");
            for(int j=0; j<3; j++){
                try{
                    Thread.sleep(500);
                }catch(Exception e){ }
                titleLoadTextView.append(".");
            }
            try{
                Thread.sleep(500);
            }catch(Exception e){ }
        }
    }
}