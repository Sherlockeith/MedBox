package com.example.jwtapplication.module;

import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.jwtapplication.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class HomeActivity extends AppCompatActivity {

    String account ;
    String jwt;
    String id;
    TabLayout tabLayout;
    ViewPager viewPager;

    ArrayList<String> mName = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        account = getIntent().getStringExtra("username");
        id = getIntent().getStringExtra("medid");
        jwt = getIntent().getStringExtra("jwt");

        viewPager=findViewById(R.id.viewpager_content_view);
        tabLayout=findViewById(R.id.tab_layout_view);
        tabLayout.setTabTextColors(R.color.black, R.color.skyblue);

        viewPager.setAdapter(new CustomAdapter(getSupportFragmentManager(),getApplicationContext()));
        tabLayout.setupWithViewPager(viewPager);


    }
    private class CustomAdapter extends FragmentPagerAdapter {
        private String fragments [] ={"Home", "Dashboard", "Notification"};

        public CustomAdapter(FragmentManager supportFragmentManager, Context applicationContext) {
            super(supportFragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment3 fragment3 = new Fragment3();
            Bundle bundle = new Bundle();
            bundle.putString("account",account);
            bundle.putString("jwt",jwt);
            bundle.putString("id",id);

            fragment3.setArguments(bundle);
            switch (position){
                case 0:
                    return new Fragment1();
                case 1:
                    return fragment3;
                case 2:
                    return new Fragment2();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return fragments.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragments[position];
        }
    }

    /**
     * c
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            exitBy2Click();
        }
        return false;
    }
    /**
     * Double tap for exit
     */
    private static Boolean isExit = false;

    private void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // Prepare for exit
            Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // Quit exit
                }
            }, 2000); // Quit exit if not press the button second time

        } else {
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);

        }
    }


}