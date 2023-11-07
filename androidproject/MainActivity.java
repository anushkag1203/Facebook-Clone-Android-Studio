package com.example.androidproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.androidproject.Fragement.FriendsFragment;
import com.example.androidproject.Fragement.HomeFragment;
import com.example.androidproject.Fragement.NotificationFragment;
import com.example.androidproject.Fragement.ProfileFragment;
import com.example.androidproject.Fragement.addPostFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    FrameLayout fl;
    BottomNavigationView bnv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        fl=(FrameLayout) findViewById(R.id.framelayout_one);
        bnv=(BottomNavigationView) findViewById(R.id.readableBottomBar);

        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_one, new HomeFragment()).commit();

        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.home) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_one, new HomeFragment()).commit();
                } else if (itemId == R.id.friends) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_one, new FriendsFragment()).commit();
                } else if (itemId == R.id.video) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_one, new addPostFragment()).commit();
                } else if (itemId == R.id.profile) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_one, new ProfileFragment()).commit();
                } else if (itemId == R.id.notification) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_one, new NotificationFragment()).commit();

               // changes about notification
                }

                return true;
            }
        });
    }
}