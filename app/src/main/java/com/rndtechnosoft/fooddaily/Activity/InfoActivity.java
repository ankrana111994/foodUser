package com.rndtechnosoft.fooddaily.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.merhold.extensiblepageindicator.ExtensiblePageIndicator;
import com.rndtechnosoft.fooddaily.Fragment.Info1Fragment;
import com.rndtechnosoft.fooddaily.Fragment.Info2Fragment;
import com.rndtechnosoft.fooddaily.Fragment.Info3Fragment;
import com.rndtechnosoft.fooddaily.R;

public class InfoActivity extends AppCompatActivity {

    public static ViewPager vpPager;
    MyPagerAdapter adapterViewPager;

    public static TextView btnNext;

    TextView btnSkip;
    int selectPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        btnNext = findViewById(R.id.btn_next);
        btnSkip = findViewById(R.id.btn_skip);
        vpPager = findViewById(R.id.vpPager);

        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);
        ExtensiblePageIndicator extensiblePageIndicator = (ExtensiblePageIndicator) findViewById(R.id.flexibleIndicator);
        extensiblePageIndicator.initViewPager(vpPager);
        vpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                selectPage = position;
                if (position == 0) {
                    btnNext.setText("Next");
                } else if (position == 1) {
                    btnNext.setText("Next");

                } else if (position == 2) {
                    btnNext.setText("Finish");

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectPage == 0) {
                    vpPager.setCurrentItem(1);
                } else if (selectPage == 1) {
                    vpPager.setCurrentItem(2);
                } else if (selectPage == 2) {
                    startActivity(new Intent(InfoActivity.this, LoginActivity.class));
                    finish();
                }
            }
        });

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InfoActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {
        private int NUM_ITEMS = 3;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    return Info1Fragment.newInstance("0", "Next");
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return Info2Fragment.newInstance("1", "Next");
                case 2: // Fragment # 1 - This will show SecondFragment
                    return Info3Fragment.newInstance("2", "Finish");
                default:
                    return null;
            }

        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            Log.e("page", "" + position);
            return "Page " + position;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
//
            return fragment;
        }

    }
}