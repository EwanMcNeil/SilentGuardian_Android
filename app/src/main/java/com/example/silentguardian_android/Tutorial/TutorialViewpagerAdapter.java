package com.example.silentguardian_android.Tutorial;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        View layoutScreen = inflater.inflate(R.layout.to_inflate_tutorial_layout,null);

        ImageView imgSlide = layoutScreen.findViewById(R.id.intro_img);
//        imgSlide.getLayoutParams().width = 50000;
//
//        imgSlide.setLayoutParams(imgSlide.getLayoutParams());
        TextView title = layoutScreen.findViewById(R.id.intro_title);
        TextView description = layoutScreen.findViewById(R.id.intro_description);

        //filling the view
        switch(position){
            case 0:{
                title.setTextSize(40f);
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
