package com.example.praca_inz_test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ExhibitionActivity extends AppCompatActivity {

    ViewPager viewPager;
    Adapter adapter;
    Integer[] colors = null;
    List<Model> models ;
    ArgbEvaluator argbEvaluator = new ArgbEvaluator();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exhibition);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        overridePendingTransition(R.anim.fade_in, R.anim.fadeout);
        models = new ArrayList<>();
        models.add(new Model(R.drawable.pic1, "Meble","Zbiory mebli składają się z około 1400 obiektów i obejmują przedmioty powstałe od średniowiecza do współczesności. W ogromnej większości są to sprzęty europejskie."));
        models.add(new Model(R.drawable.pic2, "Ceramika","Zbiór ceramiki obejmuje wszystkie gatunki dziedziny i liczy ok. 3.500 eksponatów."));
        models.add(new Model(R.drawable.pic3, "test3","opis obrazka 3"));
        models.add(new Model(R.drawable.pic4, "test4","opis obrazka 4"));

        adapter=new Adapter(models,this);
        viewPager=findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        viewPager.setPadding(130,0,130,0);

        Integer[] colors_temp = {
                getResources().getColor(R.color.color1),
                getResources().getColor(R.color.color2),
                getResources().getColor(R.color.color3),
                getResources().getColor(R.color.color4)
        };
        colors = colors_temp;

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(position<(adapter.getCount()-1)&& position<(colors.length-1)){
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
        overridePendingTransition(R.anim.fade_in, R.anim.fadeout);
    }

    public void moreInfoMethod(View view) {
        Context context = getApplicationContext();
        CharSequence text = "Tutaj więcej informacji :)";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}

