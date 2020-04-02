package com.example.silentguardian_android.Tutorial;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

//import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.silentguardian_android.R;

import java.util.List;

import javax.sql.DataSource;

public class TutorialViewpagerAdapter extends PagerAdapter {

    private Context mContext ;
    private List<MyImage> mListScreen;
    private boolean enableAnimation;
    private GifDrawable gifDrawable;



    public TutorialViewpagerAdapter(Context mContext, List<MyImage> mListScreen,boolean enableAnimations) {
        this.mContext = mContext;
        this.mListScreen = mListScreen;
        this.enableAnimation = enableAnimations;
    }
    public TutorialViewpagerAdapter(Context mContext, List<MyImage> mListScreen) {
        this.mContext = mContext;
        this.mListScreen = mListScreen;
        this.enableAnimation = true;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layoutScreen = inflater.inflate(R.layout.to_inflate_tutorial_layout,null);//idddk why it does this

        ImageView imgSlide = layoutScreen.findViewById(R.id.intro_img);
        Animation imageAnim = null,titleAnim = null;
        TextView title = layoutScreen.findViewById(R.id.intro_title);
        TextView description = layoutScreen.findViewById(R.id.intro_description);

        if(enableAnimation){
            imageAnim = AnimationUtils.loadAnimation(mContext,R.anim.image_animation_landing);
            titleAnim= AnimationUtils.loadAnimation(mContext,R.anim.title_animation_landing);
        }

        //filling the view
        if (position == 0) {// title.setTextSize(40f);
            if (imageAnim != null && titleAnim != null && !mListScreen.get(position).isGif()) {
                imgSlide.setAnimation(imageAnim);
                title.setAnimation(titleAnim);
            }
        }
        if(mListScreen.get(position).isGif()){//wont work
           Glide.with(mContext)
                   .load(mListScreen.get(position).getMyPicture())
                   .into(imgSlide);
            if (gifDrawable != null) {
                if (gifDrawable.isRunning()) {
                    gifDrawable.stop();
                } else {
                    gifDrawable.start();
                }
            }
        }else{
            imgSlide.setImageResource(mListScreen.get(position).getMyPicture());
        }


        title.setText(mListScreen.get(position).getTitle());
        description.setText(mListScreen.get(position).getDescription());

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
