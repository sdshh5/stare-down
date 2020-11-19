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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout, fragmentProfile).commitAllowingStateLoss();

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());
    }

    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            switch(item.getItemId())
            {
                case R.id.profile_menu:
                    transaction.replace(R.id.frameLayout, fragmentProfile).commitAllowingStateLoss();

                    break;
                case R.id.chat_menu:
                    transaction.replace(R.id.frameLayout, fragmentChat).commitAllowingStateLoss();
                    break;
                case R.id.game_menu:
                    transaction.replace(R.id.frameLayout, fragmentGame).commitAllowingStateLoss();
                    break;
                case R.id.rank_menu:
                    transaction.replace(R.id.frameLayout, fragmentRank).commitAllowingStateLoss();
                    break;
            }
            return true;
        }
    }
}
