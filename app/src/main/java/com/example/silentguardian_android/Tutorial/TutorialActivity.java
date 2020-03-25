package com.example.silentguardian_android.Tutorial;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.example.silentguardian_android.Database.SharePreferenceHelper;
import com.example.silentguardian_android.MainActivity;
import com.example.silentguardian_android.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class TutorialActivity extends AppCompatActivity {

    protected ViewPager mScreenPager;
    protected TutorialViewpagerAdapter mTutorialViewpagerAdapter;
    protected TabLayout mTabIndicator;
    protected Button mButtonNext;
    protected int mPosition = 0 ;
    protected Button mButtonGetStarted;
    protected Animation mButtonAnim;
    protected  TextView mSkip;
    protected final List<MyImage> mList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //check if its openened before or not
        if (new SharePreferenceHelper(this).getTutorialSeen()) {
            Intent mainActivity = new Intent(getApplicationContext(),MainActivity.class );
            startActivity(mainActivity);
            finish();
        }

        // make the activity on full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_tutorial);

        // hide the action bar
        getSupportActionBar().hide();

        mButtonNext = findViewById(R.id.btn_next);
        mButtonGetStarted = findViewById(R.id.btn_get_started);
        mTabIndicator = findViewById(R.id.tab_indicator);
        mButtonAnim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.button_animation_forward);
        mSkip = findViewById(R.id.tv_skip);

        LoadViews();
        OnClickListeners();

    }

    //add images to view, set adapter
    private void LoadViews(){

        mList.add(new MyImage("Silent Guardians",
                "The fastest way to let your loved ones know.",R.drawable.soothing_circle2));
        mList.add(new MyImage("Faster access","2ctlit",R.drawable.img2));//will have the screenshots of the app
        mList.add(new MyImage("Easy Payment","Lor ct",R.drawable.img3));

        // setup viewpager
        mScreenPager =findViewById(R.id.screen_viewpager);
        mTutorialViewpagerAdapter = new TutorialViewpagerAdapter(this,mList);
        mScreenPager.setAdapter(mTutorialViewpagerAdapter);

        // setup tablayout with viewpager
        mTabIndicator.setupWithViewPager(mScreenPager);
    }

    private void OnClickListeners(){
        mButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPosition = mScreenPager.getCurrentItem();
                if (mPosition < mList.size()) {
                    mPosition++;
                    mScreenPager.setCurrentItem(mPosition);
                }
                if (mPosition == mList.size()-1) { // when we rech to the last screen
                    loadLastScreen();
                }
            }
        });
        // tablayout add change listener
        mTabIndicator.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == mList.size()-1) {
                    loadLastScreen();
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        // Get Started button click listener
        mButtonGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open main activity
                Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(mainActivity);
                SharePreferenceHelper helper = new SharePreferenceHelper(getApplicationContext());
                helper.setTutorialSeen(true);
                finish();

            }
        });
        // skip button click listener
        mSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mScreenPager.setCurrentItem(mList.size());
            }
        });
    }
    private void loadLastScreen() {

        mButtonNext.setVisibility(View.INVISIBLE);
        mButtonGetStarted.setVisibility(View.VISIBLE);
        mSkip.setVisibility(View.INVISIBLE);
        mTabIndicator.setVisibility(View.INVISIBLE);

        // setup animation
        mButtonGetStarted.setAnimation(mButtonAnim);
    }
}
