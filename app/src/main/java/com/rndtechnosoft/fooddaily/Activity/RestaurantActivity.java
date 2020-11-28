package com.rndtechnosoft.fooddaily.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rndtechnosoft.fooddaily.Adapter.HomeMenuAdapter;
import com.rndtechnosoft.fooddaily.Adapter.MultiViewTypeAdapter;
import com.rndtechnosoft.fooddaily.Adapter.RestaurantAdapter;
import com.rndtechnosoft.fooddaily.Model.CategoryList;
import com.rndtechnosoft.fooddaily.Model.RestaurantCategoryList;
import com.rndtechnosoft.fooddaily.R;
import com.rndtechnosoft.fooddaily.Util.Constants;
import com.rndtechnosoft.fooddaily.Util.Method;
import com.rndtechnosoft.fooddaily.Util.SharedPref;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RestaurantActivity extends AppCompatActivity {
    RecyclerView recycler_restaurant;
    LottieAnimationView loader;
    AVLoadingIndicatorView progress;
    TextView txtNoData;
int selectPosition;
String catId;
    Toolbar toolbar;

    TextView tvToolbarTitle,tvCartCount,tvTotalPrice;

    ArrayList<RestaurantCategoryList> mRestaurantCategoryLists =new ArrayList<>();
    RestaurantAdapter restaurantAdapter;
    private ImageView imgNotification,imgCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reataurent);
        recycler_restaurant = findViewById(R.id.recycler_restaurant);
        progress=findViewById(R.id.progresbar_rest);
        loader=findViewById(R.id.loader);
        txtNoData=findViewById(R.id.txtNoData);
        toolbar =(Toolbar) findViewById(R.id.toolbar_main);
        tvToolbarTitle =(TextView) findViewById(R.id.tvToolbarTitle);
        tvCartCount =(TextView) findViewById(R.id.tvCartCount);
        tvTotalPrice =(TextView) findViewById(R.id.tvTotalPrice);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorBlack));
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        tvToolbarTitle.setText(getResources().getString(R.string.shops));
        tvToolbarTitle.setVisibility(View.VISIBLE);
        setSupportActionBar(toolbar);

        imgCart = (ImageView) findViewById(R.id.imgCart);
        imgNotification = (ImageView) findViewById(R.id.imgNotification);
        Intent in = getIntent();
        selectPosition = in.getIntExtra("position", 0);
        catId = in.getStringExtra("cat_id");
        getCategory();
        findViewById(R.id.toolbar_logo).setVisibility(View.GONE);


        recycler_restaurant.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//        recycler_restaurant.setLayoutManager((new GridLayoutManager(this, 2)));
         restaurantAdapter= new RestaurantAdapter(mRestaurantCategoryLists,this,catId);

        recycler_restaurant.setAdapter(restaurantAdapter);

        imgNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RestaurantActivity.this,NotificationActivity.class));
            }
        });

        imgCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RestaurantActivity.this,CartActivity.class));
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        cartcount();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    public void cartcount() {
        Method.getCartCount(RestaurantActivity.this,tvCartCount);
     //   Method.getTotalPrice(CategoryDetailActivity.this,tvTotalPrice,cart_lay);
        if (Integer.parseInt(Constants.CART_COUNT)>0){
            tvCartCount.setVisibility(View.VISIBLE);
            tvCartCount.setText(Constants.CART_COUNT);
        }else{
            tvCartCount.setVisibility(View.GONE);
        }
    }
    private void getCategory() {
      //  menuLists=new ArrayList<>();
      //  categoryLists=new ArrayList<>();
        progress.hide();
        txtNoData.setVisibility(View.GONE);
        loader.setVisibility(View.VISIBLE);
        final RequestQueue requestQueue = Volley.newRequestQueue(RestaurantActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.restaurants+"&user_id="+ SharedPref.getUserId(this)+"&cat_id="+catId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("CATEGORY_LIST");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject object = jsonArray.getJSONObject(i);
                        String id = object.getString("id");
                        String restaurant_title = object.getString("restaurant_title");
                        String restaurant_image = object.getString("restaurant_image");
                        String restaurant_address = object.getString("restaurant_address");
                        String restaurant_description = object.getString("restaurant_description");
                        String rating = object.getString("rating");
                        String delivery_charges = object.getString("delivery_charges");
                        String cost_for_two = object.getString("cost_for_two");
                        String opening_time = object.getString("opening_time");
                        String closing_time = object.getString("closing_time");

                     //   String count = object.getString("count");
                        mRestaurantCategoryLists.add(new RestaurantCategoryList(id,restaurant_title,restaurant_image,restaurant_address
                                ,restaurant_description,
                                rating,delivery_charges,cost_for_two,opening_time,
                                closing_time));

                    }
                    restaurantAdapter.notifyDataSetChanged();
                  //  setTab();
                    progress.hide();
                    loader.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                    progress.hide();
                    loader.setVisibility(View.GONE);
                    txtNoData.setVisibility(View.VISIBLE);
                   // viewPager.setVisibility(View.GONE);
                   // tab.setVisibility(View.GONE);
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
}