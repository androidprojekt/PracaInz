package com.example.praca_inz_test;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;
    Button button;

    public SliderAdapter(Context context){
        this.context = context;
    }

    //------------------------------------------Arrays----------------------------------------------

    public int[] slide_Images = {
            R.drawable.podejdz_image_info,
            R.drawable.click_image_info,
            R.drawable.ocena_image_info,
            R.drawable.location_icon_info
    } ;

    public String[] slide_Headings = {
            "Podejdź",
            "Kliknij",
            "Oceń",
            "Znajdź mnie"
    };

    public String[] slide_Desc = {
            "Podejście do eksponatu spowoduje jego podświetlenie",
            "Kliknij na dany eksponat na mapce, \naby zasięgnąć po więcej informacji.",
            "Możesz ocenić eksponat,\n a także napisać swoją opinię\n siegając do zakładki Opinie",
            "Naciśnij przycisk znajdź mnie, \naby wyświetlić swoją lokalizację na mapie"
    };


    @Override
    public int getCount() {

        return slide_Headings.length;

    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == (LinearLayout)object);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout,container,false);
        LinearLayout layoutSlide = (LinearLayout) view.findViewById(R.id.slidelinearLayout);

        button = view.findViewById(R.id.button);

        ImageView imageslide = (ImageView) view.findViewById(R.id.slideImageId);
        TextView headingstxt = (TextView) view.findViewById(R.id.slideHeadingTvId);
        TextView desctxt = (TextView) view.findViewById(R.id.slideDescTvId);

        imageslide.setImageResource(slide_Images[position]);
        headingstxt.setText(slide_Headings[position]);
        desctxt.setText(slide_Desc[position]);

        if(position==3)
        {
            button.setVisibility(View.VISIBLE);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(context,LozalizationActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });

        }

        container.addView(view);
        return view;
    }
}
