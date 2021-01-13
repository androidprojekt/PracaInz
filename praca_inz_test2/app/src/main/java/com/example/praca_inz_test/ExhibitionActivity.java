package com.example.praca_inz_test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

//-------------------------Activity in which exhibitions from the museum are displayed--------------

public class ExhibitionActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private ExhibitionAdapter exhibitionAdapter;
    private Integer[] colors = null;
    private ArgbEvaluator argbEvaluator = new ArgbEvaluator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exhibition);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        List<Model> models = new ArrayList<>();
        models.add(new Model(R.drawable.pic1, "Meble",getString(R.string.furniture_desc)));
        models.add(new Model(R.drawable.pic2, "Ceramika",getString(R.string.ceramics_desc)));
        models.add(new Model(R.drawable.pic3, "Metale",getString(R.string.metals_desc)));
        models.add(new Model(R.drawable.pic4, "Tkaniny",getString(R.string.fabrics_desc)));

        exhibitionAdapter =new ExhibitionAdapter(models,this);
        viewPager=findViewById(R.id.viewPager);
        viewPager.setAdapter(exhibitionAdapter);
        viewPager.setPadding(130,0,130,0);

        colors = new Integer[]{
                getResources().getColor(R.color.color1),
                getResources().getColor(R.color.color2),
                getResources().getColor(R.color.color3),
                getResources().getColor(R.color.color4)
        };

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(position<(exhibitionAdapter.getCount()-1)&& position<(colors.length-1)){
                    viewPager.setBackgroundColor(
                            (Integer) argbEvaluator.evaluate
                                    (positionOffset, colors[position],colors[position +1]));
                }
                else{
                    viewPager.setBackgroundColor(colors[colors.length-1]);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void moreInfoMethod(View view) {
        Context context = getApplicationContext();
        CharSequence text = "Zajrzyj w zakładkę 'strona internetowa' po więcej informacji!";
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}

