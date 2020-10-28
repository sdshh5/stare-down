package org.project.eye_game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EnterRoomActivity extends AppCompatActivity {
    EditText roomCodeEditText;
    Button enterButton;
    Intent receivedIntent;

    private View decorView;
    private int	uiOption;

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
        receivedIntent = getIntent();
        intent.putExtra("nickname",receivedIntent.getExtras().getString("nickname"));
        intent.putExtra("email", receivedIntent.getExtras().getString("email"));
        intent.putExtra("rank", receivedIntent.getExtras().getInt("rank"));
        intent.putExtra("exp", receivedIntent.getExtras().getInt("exp"));
        intent.putExtra("id", receivedIntent.getExtras().getString("id"));
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_room);

        decorView = getWindow().getDecorView();
        uiOption = getWindow().getDecorView().getSystemUiVisibility();
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH )
            uiOption |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN )
            uiOption |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT )
            uiOption |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        decorView.setSystemUiVisibility( uiOption );


        roomCodeEditText = (EditText)findViewById(R.id.enterCodeEditText);
        enterButton = (Button)findViewById(R.id.enterButton);

        enterButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), ConnectActivity.class);
                String roomID = roomCodeEditText.getText().toString().trim();
                if(roomID.equals("")){
                    Toast.makeText(getApplicationContext(), "Room Code is Empty. Retry!", Toast.LENGTH_LONG);
                    return;
                }
                intent.putExtra("roomID", roomID);
                receivedIntent = getIntent();
                intent.putExtra("nickname",receivedIntent.getExtras().getString("nickname"));
                intent.putExtra("email", receivedIntent.getExtras().getString("email"));
                intent.putExtra("rank", receivedIntent.getExtras().getInt("rank"));
                intent.putExtra("exp", receivedIntent.getExtras().getInt("exp"));;
                intent.putExtra("id", receivedIntent.getExtras().getString("id"));
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                finish();
            }
        });

    }
}