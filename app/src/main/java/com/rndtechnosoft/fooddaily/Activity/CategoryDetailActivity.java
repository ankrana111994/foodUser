package com.rndtechnosoft.fooddaily.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.rndtechnosoft.fooddaily.Adapter.CategoryPagerAdapter;
import com.rndtechnosoft.fooddaily.Fragment.CategoryFragment;
import com.rndtechnosoft.fooddaily.Model.CategoryList;
import com.rndtechnosoft.fooddaily.Model.CategoryMenuList;
import com.rndtechnosoft.fooddaily.R;
import com.rndtechnosoft.fooddaily.Util.Constants;
import com.rndtechnosoft.fooddaily.Util.Method;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class CategoryDetailActivity extends AppCompatActivity {

    private TabLayout tab;
    public ViewPager viewPager;
    Toolbar toolbar;
    AVLoadingIndicatorView progress;
    TextView txtNoData;
    public ArrayList<CategoryMenuList> menuLists;
    public ArrayList<CategoryList> categoryLists;
    TextView tvToolbarTitle,tvCartCount,tvTotalPrice;
    private ImageView imgNotification,imgCart;
    private int selectPosition;
    private String cat_id;
    RelativeLayout cart_lay;
    LottieAnimationView loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_detail);

        toolbar =(Toolbar) findViewById(R.id.toolbar_main);
        tvToolbarTitle =(TextView) findViewById(R.id.tvToolbarTitle);
        tvCartCount =(TextView) findViewById(R.id.tvCartCount);
        tvTotalPrice =(TextView) findViewById(R.id.tvTotalPrice);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorBlack));
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        tvToolbarTitle.setText(getResources().getString(R.string.menu));
        setSupportActionBar(toolbar);

        Intent in = getIntent();
        selectPosition = in.getIntExtra("position", 0);
        cat_id = in.getStringExtra("cat_id");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        imgCart = (ImageView) findViewById(R.id.imgCart);
        imgNotification = (ImageView) findViewById(R.id.imgNotification);
        tab = (TabLayout) findViewById(R.id.tabs);
        progress=findViewById(R.id.progresbar_rest);
        txtNoData=findViewById(R.id.txtNoData);
        cart_lay=findViewById(R.id.cart_lay);
        loader=findViewById(R.id.loader);
        cart_lay.setVisibility(View.GONE);
        tab.setTabMode(TabLayout.MODE_SCROLLABLE);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(
                tab));
        tab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        imgNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CategoryDetailActivity.this,NotificationActivity.class));
            }
        });

        imgCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CategoryDetailActivity.this,CartActivity.class));
            }
        });

        cart_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CategoryDetailActivity.this,CartActivity.class));
            }
        });

        getCategory();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cartcount();
    }

    public void cartcount() {
        Method.getCartCount(CategoryDetailActivity.this,tvCartCount);
        Method.getTotalPrice(CategoryDetailActivity.this,tvTotalPrice,cart_lay);
        if (Integer.parseInt(Constants.CART_COUNT)>0){
            tvCartCount.setVisibility(View.VISIBLE);
            tvCartCount.setText(Constants.CART_COUNT);
        }else{
            tvCartCount.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void getCategory() {
        menuLists=new ArrayList<>();
        categoryLists=new ArrayList<>();
        progress.hide();
        txtNoData.setVisibility(View.GONE);
        loader.setVisibility(View.VISIBLE);
        final RequestQueue requestQueue = Volley.newRequestQueue(CategoryDetailActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.category, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("CATEGORY_LIST");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject object = jsonArray.getJSONObject(i);
                        String id = object.getString("id");
                        String category_name = object.getString("category_name");
                        String category_image = object.getString("image");
                        String count = object.getString("count");
                        categoryLists.add(new CategoryList(id,category_name,category_image,count));
                    }
                    setTab();
                    progress.hide();
                    loader.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                    progress.hide();
                    loader.setVisibility(View.GONE);
                    txtNoData.setVisibility(View.VISIBLE);
                    viewPager.setVisibility(View.GONE);
                    tab.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                requestQueue.stop();
                loader.setVisibility(View.GONE);
            }
        });
        requestQueue.add(stringRequest);
    }

    private void setTab() {

        if (categoryLists!=null && categoryLists.size()>0) {
            setupViewPager(viewPager);

            tab.setupWithViewPager(viewPager);
        }else {
            txtNoData.setVisibility(View.VISIBLE);
            viewPager.setVisibility(View.GONE);
            tab.setVisibility(View.GONE);
        }

        for (int i = 0; i < tab.getTabCount(); i++) {
//            View view = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_tab, null, false);

            tab.getTabAt(i).setCustomView(R.layout.tab_custom_view);
            TextView tab_name = (TextView) tab.getTabAt(i).getCustomView().findViewById(R.id.tab_text);
            ImageView tab_img = (ImageView) tab.getTabAt(i).getCustomView().findViewById(R.id.tab_img);
            tab_name.setText(categoryLists.get(i).getCategory_name());
            Glide.with(CategoryDetailActivity.this).load(categoryLists.get(i).getCategory_image()).thumbnail(Glide.with(this).load(R.drawable.loading)).into(tab_img);
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        CategoryPagerAdapter adapter = new CategoryPagerAdapter(getSupportFragmentManager());

        for(int i=0;i<categoryLists.size();i++) {
            CategoryFragment categoryFragment = new CategoryFragment();
            Bundle bundle = new Bundle();
            bundle.putString("id",categoryLists.get(i).getId());
            bundle.putString("category_name",categoryLists.get(i).getCategory_name());
            categoryFragment.setArguments(bundle);
            adapter.addFragment(categoryFragment, categoryLists.get(i).getCategory_name());
            if (cat_id!=null && cat_id.equals(categoryLists.get(i).getId()))
                selectPosition = i;
        }
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(selectPosition);
    }
}
