package com.example.silentguardian_android.Tutorial;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
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

    protected int mPosition = 0 ;
    protected Button mButtonGetStarted;
    protected Animation mButtonAnim;
    protected TextView mSkip;
    protected final List<MyImage> mList = new ArrayList<>();
    protected AnimationDrawable mZoomIn;
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
        mButtonGetStarted = findViewById(R.id.btn_get_started);
        mTabIndicator = findViewById(R.id.tab_indicator);
        mButtonAnim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.button_animation_forward);
        mSkip = findViewById(R.id.tv_skip);

        LoadViews();
        OnClickListeners();

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    //add images to view, set adapter
    private void LoadViews(){


        //Silent guardians description
        mList.add(new MyImage("The fastest way to call for help.",
                "Silent Guardians is here to assists you, if ever you are unsafe or feel threatened.",R.mipmap.bubble_from_outside_title));


        //introduce device
        mList.add(new MyImage("Quick access",
                "Use the Silent Guardian companion device to reach your friends or family. ",R.mipmap.hand_holding_device2));

        //introduce guardians/bubble concept
        mList.add(new MyImage("Designate your Guardians",
                "Create up to two separate groups of contacts, i.e. your Guardians in two distinct Guardian Levels, to reach in case of emergencies.",R.mipmap.group1_2_tutorial));

        //describe activation
        mList.add(new MyImage("Let your guardians know where you are, when you need it.",
                "Press and hold the button on the device so that the LED turns on and blinks rapidly.",R.mipmap.hand_pressing_device_pressure_1));

        //show view from guardian perspective
        mList.add(new MyImage("Your location is shared with your Guardians.",
                "After activating your device, a text will be sent out to all your Guardians containing:\n-Location at the time of activation\n-Custom message you define.",
                R.mipmap.hand_friend_receive_text_tiny));

        // setup viewpager
        mScreenPager =findViewById(R.id.screen_viewpager);
        mTutorialViewpagerAdapter = new TutorialViewpagerAdapter(this,mList);
        mScreenPager.setAdapter(mTutorialViewpagerAdapter);
        mScreenPager.setPageTransformer(true,new ZoomOutPageTransformer());
        // setup tablayout with viewpager
        mTabIndicator.setupWithViewPager(mScreenPager);


    }

    private void OnClickListeners(){
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
                //last view with getstarted button
                mScreenPager.setCurrentItem(mList.size());
            }
        });
       ;
    }
    private void loadLastScreen() {


        mButtonGetStarted.setVisibility(View.VISIBLE);
        mSkip.setVisibility(View.INVISIBLE);
        mTabIndicator.setVisibility(View.INVISIBLE);

        // setup animation
        mButtonGetStarted.setAnimation(mButtonAnim);

    }

    public class ZoomOutPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0f);

            } else if (position <= 1) { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }

                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA +
                        (scaleFactor - MIN_SCALE) /
                                (1 - MIN_SCALE) * (1 - MIN_ALPHA));

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0f);
            }
        }
    }





    //////// END
}
