package com.example.praca_inz_test;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
//-------------an activity that displays instructions for a localization activity-------------------

public class instructionActivity extends AppCompatActivity {

    private LinearLayout mDotLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction);

        mDotLayout = (LinearLayout) findViewById(R.id.dotsLayout);
        ViewPager mSlideViewPager = (ViewPager) findViewById(R.id.slideViewPager);
        SliderAdapter sliderAdapter = new SliderAdapter(getApplicationContext());
        mSlideViewPager.setAdapter(sliderAdapter);
        addDotsIndicator(0);
        mSlideViewPager.addOnPageChangeListener(viewListener);
    }

    public void addDotsIndicator(int position) {
        TextView[] mDots = new TextView[4];
        mDotLayout.removeAllViews();

        for (int i = 0; i < mDots.length; i++) {
            mDots[i] = new TextView(this);
            mDots[i].setText(".");
            mDots[i].setTextSize(45);
            mDots[i].setTextColor(getResources().getColor(R.color.lightGray));

            mDotLayout.addView(mDots[i]);
        }

        mDots[position].setTextColor(getResources().getColor(R.color.darkGray));
        mDots[position].setTextSize(55);
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDotsIndicator(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
