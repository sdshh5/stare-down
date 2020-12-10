package org.project.eye_game.interfaces;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.project.eye_game.R;

public class FragmentActivity extends AppCompatActivity {
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private FragmentProfile fragmentProfile = new FragmentProfile();
    private FragmentChat fragmentChat = new FragmentChat();
    private FragmentGame fragmentGame = new FragmentGame();
    private FragmentRank fragmentRank = new FragmentRank();
    private BottomNavigationView bottomNavigationView;
    int CHARACTER_ID;
    String id;
    String nickname;
    int fragmentId;

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        id = getIntent().getExtras().getString("id");
        CHARACTER_ID = getIntent().getExtras().getInt("characterID");
        nickname = getIntent().getExtras().getString("nickname");

        bottomNavigationView = findViewById(R.id.navigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());

        FragmentTransaction transaction = fragmentManager.beginTransaction();

        if(getIntent().getExtras().getInt("fragmentId")==0) {
            fragmentId = R.id.profile_menu;
            bottomNavigationView.setSelectedItemId(fragmentId);
            transaction.replace(R.id.frameLayout, fragmentProfile).commitAllowingStateLoss();
            Bundle bundle = new Bundle();
            bundle.putString("id", id);
            bundle.putString("nickname",nickname);
            bundle.putInt("characterID",CHARACTER_ID);
            fragmentProfile.setArguments(bundle);
        }
        else {
            fragmentId = getIntent().getExtras().getInt("fragmentId");
            bottomNavigationView.setSelectedItemId(fragmentId);
            transaction.replace(R.id.frameLayout, fragmentGame).commitAllowingStateLoss();
            Bundle bundle = new Bundle();
            bundle.putString("id", id);
            bundle.putString("nickname",nickname);
            bundle.putInt("characterID",CHARACTER_ID);
            fragmentGame.setArguments(bundle);
        }
    }

    public void updateState(String frag,String state) {
        switch(frag) {
            case "FragmentProfile":
                fragmentProfile = (FragmentProfile)fragmentManager.findFragmentById(R.id.frameLayout);
                fragmentProfile.updateState(state);
                break;
        }
    }

    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            switch(item.getItemId())
            {
                case R.id.profile_menu:
                    transaction.replace(R.id.frameLayout, fragmentProfile).addToBackStack(null).commitAllowingStateLoss();
                    Bundle bundle_profile = new Bundle();
                    bundle_profile.putString("id", id);
                    bundle_profile.putString("nickname",nickname);
                    bundle_profile.putInt("characterID",CHARACTER_ID);
                    fragmentProfile.setArguments(bundle_profile);
                    break;
                case R.id.chat_menu:
                    transaction.replace(R.id.frameLayout, fragmentChat).addToBackStack(null).commitAllowingStateLoss();
                    Bundle bundle_chat = new Bundle();
                    bundle_chat.putString("id", id);
                    bundle_chat.putString("nickname",nickname);
                    fragmentChat.setArguments(bundle_chat);
                    break;
                case R.id.game_menu:
                    transaction.replace(R.id.frameLayout, fragmentGame).addToBackStack(null).commitAllowingStateLoss();
                    Bundle bundle_game = new Bundle();
                    bundle_game.putString("id", id);
                    bundle_game.putString("nickname",nickname);
                    bundle_game.putInt("characterID",CHARACTER_ID);
                    fragmentGame.setArguments(bundle_game);
                    break;
                case R.id.rank_menu:
                    transaction.replace(R.id.frameLayout, fragmentRank).addToBackStack(null).commitAllowingStateLoss();
                    Bundle bundle_rank = new Bundle();
                    bundle_rank.putString("id", id);
                    bundle_rank.putString("nickname",nickname);
                    fragmentRank.setArguments(bundle_rank);
                    break;
            }
            return true;
        }
    }
}
