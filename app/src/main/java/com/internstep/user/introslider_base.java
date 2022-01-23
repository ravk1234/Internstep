package com.internstep.user;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class introslider_base extends AppCompatActivity {

    Button Skip;
    private Button Next;
    private ViewPager viewPager;
    private IntroPref introPref;
    private TextView[] dots;
    private LinearLayout dotsLayout;
    private int[] layouts;
    MyViewPager myViewPager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.introslider_base);

        introPref = new IntroPref(this);
        if(!introPref.isFirstTimeLaunch()){
            startActivity(new Intent(introslider_base.this,StartActivity.class));
            finish();
        }


        viewPager = findViewById(R.id.viewpager);
        Skip = findViewById(R.id.skipforNow);
        Next = findViewById(R.id.next);
        dotsLayout = findViewById(R.id.layoutDots);


        layouts = new int[]{
                R.layout.introslider_screen1,
                R.layout.introslider_screen2,
                R.layout.introslider_screen3,
                R.layout.introslider_screen4,
        };

        addBottomDots(0);

        myViewPager = new MyViewPager();
        viewPager.setAdapter(myViewPager);
        viewPager.addOnPageChangeListener(onPageChangeListener);

        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = getitem(+1);
                if(current < layouts.length){
                    viewPager.setCurrentItem(current);
                }

                else{
                    launchHomeActivity();
                }
            }
        });

        Skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(introslider_base.this,StartActivity.class);
                launchHomeActivity();
            }
        });



    }

    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener(){

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);
            if(position == layouts.length -1){
                Next.setText("Take Me In");
            }
            else{
                Next.setText("Next");
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }


    };

    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    public class MyViewPager extends PagerAdapter{

        LayoutInflater inflater;

        public MyViewPager(){};

        @Override
        public int getCount() {
            return layouts.length;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {

            inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert inflater != null;
            View view = inflater.inflate(layouts[position],container,false);
            container.addView(view);
            return view;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            View view = (View)object;
            container.removeView(view);
        }


    }

    private int getitem(int i){
        return viewPager.getCurrentItem() + 1;
    }


    private void launchHomeActivity() {
        introPref.setIsfirstTimeLaunch(false);
        startActivity(new Intent(introslider_base.this,StartActivity.class));
        finish();
    }
}
