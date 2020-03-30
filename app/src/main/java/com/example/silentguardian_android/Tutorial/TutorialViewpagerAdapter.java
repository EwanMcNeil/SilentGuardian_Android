package com.example.silentguardian_android.Tutorial;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.silentguardian_android.R;

import java.util.List;

public class TutorialViewpagerAdapter extends PagerAdapter {

    protected Context mContext ;
    protected List<MyImage> mListScreen;

    public TutorialViewpagerAdapter(Context mContext, List<MyImage> mListScreen) {
        this.mContext = mContext;
        this.mListScreen = mListScreen;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layoutScreen = inflater.inflate(R.layout.to_inflate_tutorial_layout,null);//idddk why it does this

        ImageView imgSlide = layoutScreen.findViewById(R.id.intro_img);

        Animation imageAnim = AnimationUtils.loadAnimation(mContext,R.anim.image_animation_landing);
        Animation titleAnim= AnimationUtils.loadAnimation(mContext,R.anim.title_animation_landing);

        //Dont know what im doing here
//        AnimationSet animSet = new AnimationSet(true);
//        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, -.75f, 0);
//        translateAnimation.setDuration(300);
//        animSet.addAnimation(translateAnimation);


        TextView title = layoutScreen.findViewById(R.id.intro_title);
        TextView description = layoutScreen.findViewById(R.id.intro_description);

        //filling the view
        switch(position){
            case 0:{
               // title.setTextSize(40f);
                imgSlide.setAnimation(imageAnim);
                title.setAnimation(titleAnim);
                break;
            }
        }
        title.setText(mListScreen.get(position).getTitle());
        description.setText(mListScreen.get(position).getDescription());
        imgSlide.setImageResource(mListScreen.get(position).getMyPicture());
        container.addView(layoutScreen);
        return layoutScreen;
    }

    @Override
    public int getCount() {
        return mListScreen.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == object);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        //super.destroyItem(container, position, object);
        container.removeView((View)object);
    }


}
