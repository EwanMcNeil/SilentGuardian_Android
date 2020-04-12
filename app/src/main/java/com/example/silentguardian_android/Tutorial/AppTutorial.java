package com.example.silentguardian_android.Tutorial;
/*
*
 To create a new tutorial in any activity, you ll need to create:

    //an image button wherever you want in your activity layout.

    protected ImageButton mImageButtonTutorial;//used for a png of what you re describing
    final Dialog mInfoDialog = new Dialog(ThresholdSettingActivity.this, R.style.Theme_AppCompat);
    //Setup tutorial
    loadActivityTutorial(mInfoDialog);
    ////then something like this
    if(!helper.getTutorialSeen() ){
        Log.d("__Guardians","Gooing to perform click on tutorial button");
        mImageButtonTutorial.performClick();
    }

       //helper function , could figure out a better way to structure this
    //for now id rather have it working
//////////////////////////////////////////////////
    private void loadActivityTutorial( final Dialog mInfoDialog){//this is here you add your content

        final List<MyImage> mList = new ArrayList<>();
        mList.add(new MyImage("Add Contacts to the SilentGuardians App",
                "Either manually add contacts by pressing the Add Contact button or import existing phone contacts by pressing Import Contacts."
                ,R.drawable.guardians_act_info1));

        mList.add(new MyImage("Assign Contacts as Guardians",
                "After adding contacts, press the Setting icon to edit your Guardians. "
                ,R.drawable.guardians_act_info));

        mList.add(new MyImage("Assign Contacts as Guardians",
                "After adding contacts, you assign Guardians by clicking on the contact name. "
                ,R.drawable.guardians_act_info));

        mImageButtonTutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInfoDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
                mInfoDialog.setContentView(R.layout.activity_tutorial);
                //UI elements
                ViewPager mScreenPager = mInfoDialog.findViewById(R.id.screen_viewpager);
                TabLayout mTabIndicator  = mInfoDialog.findViewById(R.id.tab_indicator);
                TextView  mSkip = mInfoDialog.findViewById(R.id.tv_skip);
                final Button mDialogButton = mInfoDialog.findViewById(R.id.btn_get_started)
                mSkip.setVisibility(View.INVISIBLE);
                //decodeSampledBitmapFromResource(getResources(),R.drawable.guardians_act_info, 220, 220);
                TutorialViewpagerAdapter mTutorialViewpagerAdapter = new TutorialViewpagerAdapter(getApplicationContext(),mList,false);
                mScreenPager.setAdapter(mTutorialViewpagerAdapter);
                // setup tablayout with viewpager
                mTabIndicator.setupWithViewPager(mScreenPager);
                mDialogButton.setText(R.string.end_tutorial_button_text);
                // if button is clicked, close the custom dialog
                mTabIndicator.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        if (tab.getPosition() == mList.size()-1) {
                            mDialogButton.setVisibility(View.VISIBLE);
                        }
                    }
                    @Override//must have these two here
                    public void onTabUnselected(TabLayout.Tab tab) {
                    }
                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                    }
                });
                mDialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mInfoDialog.dismiss();
                    }
                });
                mInfoDialog.show();
            }
        });
    }
//////////////
*
*
* */
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
import com.example.silentguardian_android.profileActivity;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class AppTutorial extends AppCompatActivity {

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
        mList.add(new MyImage("Let your guardians know where you are, when you need it.",
                "Use the Silent Guardian companion device to reach your friends or family."
                +" Simply press and hold the button on the device until the LEDs turn on and blink rapidly."
                ,R.mipmap.hand_holding_device2));

        //introduce guardians/bubble concept
        mList.add(new MyImage("Designate your Guardians",
                "Create up to two separate groups of contacts, i.e. your Guardians in two distinct Guardian Levels, to reach in case of emergencies.",R.mipmap.group1_2_tutorial));

        //describe activation
        mList.add(new MyImage("Smart Companion",
                "The device distinguishes pressure intensities:\n"
                        +"The lower LED turns on when a soft pressure is applied: "
                        +" This is Level 1. A harder press corresponds to Level 2. Give it a try!\n Press and hold at either pressure intensity until the LEDs blink to alert the corresponding Guardians."
                ,R.mipmap.hand_pressing_device_pressure_1));

        //show view from guardian perspective
        mList.add(new MyImage("Your location is shared with your Guardians.",
                "After activating your device, a text will be sent out to all your Guardians containing:\n-your location at the time of pressing the device\n-your custom message.",
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
                Intent intent = new Intent(getApplicationContext(), profileActivity.class);
                startActivity(intent);
                SharePreferenceHelper helper = new SharePreferenceHelper(getApplicationContext());
               // helper.setTutorialSeen(true);
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
