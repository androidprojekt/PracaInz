package com.example.praca_inz_test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class instructionActivity extends AppCompatActivity {

    private ViewPager mSlideViewPager;
    private SliderAdapter sliderAdapter;
    private LinearLayout mDotLayout;
    private TextView[] mDots;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction);

        mDotLayout = (LinearLayout) findViewById(R.id.dotsLayout);

        mSlideViewPager = (ViewPager) findViewById(R.id.slideViewPager);
        sliderAdapter = new SliderAdapter(getApplicationContext());
        mSlideViewPager.setAdapter(sliderAdapter);


        addDotsIndicator(0);
        mSlideViewPager.addOnPageChangeListener(viewListener);


    }

    public void addDotsIndicator(int position) {
        mDots = new TextView[4];
        mDotLayout.removeAllViews();

        for (int i = 0; i < mDots.length; i++) {
            mDots[i] = new TextView(this);
            mDots[i].setText(".");
            mDots[i].setTextSize(45);
            mDots[i].setTextColor(getResources().getColor(R.color.lightGray));

            mDotLayout.addView(mDots[i]);
        }

        if (mDots.length > 0) {
            mDots[position].setTextColor(getResources().getColor(R.color.darkGray));
            mDots[position].setTextSize(55);
        }


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
