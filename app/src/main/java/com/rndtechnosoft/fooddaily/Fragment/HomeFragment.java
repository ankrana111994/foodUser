package com.rndtechnosoft.fooddaily.Fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rndtechnosoft.fooddaily.Activity.SplashActivity;
import com.rndtechnosoft.fooddaily.Adapter.BannerAdapter;
import com.rndtechnosoft.fooddaily.Adapter.MultiViewTypeAdapter;
import com.rndtechnosoft.fooddaily.Model.Banner;
import com.rndtechnosoft.fooddaily.Model.HomeCategoryList;
import com.rndtechnosoft.fooddaily.Model.HomeMenuList;
import com.rndtechnosoft.fooddaily.Model.Model;
import com.rndtechnosoft.fooddaily.Model.Shops;
import com.rndtechnosoft.fooddaily.Model.Steps;
import com.rndtechnosoft.fooddaily.Model.Strip;
import com.rndtechnosoft.fooddaily.Model.SubMenuList;
import com.rndtechnosoft.fooddaily.Model.VariantList;
import com.rndtechnosoft.fooddaily.R;
import com.rndtechnosoft.fooddaily.Util.Constants;
import com.rndtechnosoft.fooddaily.Util.Method;
import com.rndtechnosoft.fooddaily.Util.SharedPref;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private BannerAdapter adapter;
    RecyclerView recycler_home;
    ArrayList<Banner> bannerList;
    AVLoadingIndicatorView progresbar_home;
    MultiViewTypeAdapter multiViewTypeAdapter;
    NestedScrollView main_layout;
    RelativeLayout no_data,no_connection;
    LottieAnimationView loader;
    SwipeRefreshLayout swipe_refresh;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recycler_home = view.findViewById(R.id.recycler_home);
        swipe_refresh = view.findViewById(R.id.swipe_refresh);
        swipe_refresh.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.colorWhite));
        int col = getResources().getColor(R.color.colorAccent);
        swipe_refresh.setColorSchemeColors(col,col,col);
        progresbar_home = view.findViewById(R.id.progresbar_home);
        main_layout = view.findViewById(R.id.main_layout);
        no_data = view.findViewById(R.id.no_data);
        no_connection = view.findViewById(R.id.no_connection);
        loader = view.findViewById(R.id.loader);
        recycler_home.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        swipe_refresh.setOnRefreshListener(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
//        home();
//        getCategory();
        if (Method.haveNetworkConnection(getActivity())) {
            getHome();
        }else{
            Toast.makeText(getActivity(),getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
            no_connection.setVisibility(View.VISIBLE);
            progresbar_home.setVisibility(View.GONE);
            loader.setVisibility(View.GONE);
            main_layout.setVisibility(View.GONE);
            no_data.setVisibility(View.GONE);
            swipe_refresh.setRefreshing(false);
        }
    }

    private void getHome() {
        main_layout.setVisibility(View.GONE);
        no_data.setVisibility(View.GONE);
        no_connection.setVisibility(View.GONE);
        progresbar_home.setVisibility(View.GONE);
        loader.setVisibility(View.VISIBLE);
        final RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.home_list+"&user_id="+ SharedPref.getUserId(getActivity()), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ArrayList<Model> randomModelList = new ArrayList<>();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArrayCategory = jsonObject.getJSONArray("APP_HOME_LIST");
                    for (int i=0;i<jsonArrayCategory.length();i++){
                        JSONObject jsonObjectCategory = jsonArrayCategory.getJSONObject(i);
                        String type = jsonObjectCategory.getString("type");
                        String id = jsonObjectCategory.getString("id");
                        String title = jsonObjectCategory.getString("title");
                        String subtitle = jsonObjectCategory.getString("subtitle");
                        String link = jsonObjectCategory.getString("link_type");
                        String bg_color = jsonObjectCategory.getString("bg_color");
                        String border_color = jsonObjectCategory.getString("border_color");
                        String Strip_link = jsonObjectCategory.getString("Strip_link");
                        String icon = jsonObjectCategory.getString("icon");
                        String cat_list = jsonObjectCategory.getString("cat_list");

                        Model model = new Model();
                        ArrayList<Shops> shops=new ArrayList<>();
                        ArrayList<Banner> banners=new ArrayList<>();
                        ArrayList<Strip> strips=new ArrayList<>();
                        ArrayList<HomeCategoryList> categoryLists=new ArrayList<>();
                        ArrayList<HomeMenuList> menuLists=new ArrayList<>();
                        ArrayList<Steps> steps=new ArrayList<>();

                        if (jsonObjectCategory.has("list")){
                            JSONArray jsonArrayCatList = jsonObjectCategory.getJSONArray("list");
                            for (int j=0;j<jsonArrayCatList.length();j++) {
                                JSONObject jsonObject1 = jsonArrayCatList.getJSONObject(j);
                                switch (type) {
                                    case Model.SHOPS:
                                        String restaurant_id = jsonObject1.getString("id");
                                        String restaurant_title = jsonObject1.getString("restaurant_title");
//                                        String restaurant_image = jsonObject1.getString("restaurant_image");
                                        String restaurant_image = "http://3.6.141.131//images/83470_download.jpeg";

                                        String restaurant_address = jsonObject1.getString("restaurant_address");
                                        String restaurant_description = jsonObject1.getString("restaurant_description");
                                        String rating = jsonObject1.getString("rating");
                                        String delivery_charges = jsonObject1.getString("delivery_charges");
                                        String cost_for_two = jsonObject1.getString("cost_for_two");
                                        String opening_time = jsonObject1.getString("opening_time");
                                        String closing_time = jsonObject1.getString("closing_time");
                                        shops.add(new Shops(restaurant_id,restaurant_title,restaurant_image,restaurant_address,
                                                restaurant_description,rating,delivery_charges,cost_for_two,opening_time,closing_time));
                                        break;
                                    case Model.BANNER:
                                        String banner_id = jsonObject1.getString("id");
                                        String banner_name = jsonObject1.getString("banner_name");
                                        String banner_image = jsonObject1.getString("banner_image");
                                        String banner_type = jsonObject1.getString("banner_type");
                                        String banner_link = jsonObject1.getString("banner_link");
                                        banners.add(new Banner(banner_id,banner_name,banner_image,banner_type,banner_link));
                                        break;

                                    case Model.CATEGORY:
                                        String category_id = jsonObject1.getString("id");
                                        String category_name = jsonObject1.getString("category_name");
                                        String image = jsonObject1.getString("image");
                                        String color_code = jsonObject1.getString("color_code");
                                        String cat_bg_color = jsonObject1.getString("bg_color");
                                        categoryLists.add(new HomeCategoryList(category_id,category_name,image,color_code,cat_bg_color));
                                        break;

                                    case Model.MENU:
                                        String menu_id = jsonObject1.getString("id");
                                        String menu_name = jsonObject1.getString("menu_name");
                                        String menu_image = jsonObject1.getString("image");
                                        String cat_id = link;//jsonObject1.getString("cat_id");
                                        String food_opening_time = jsonObject1.getString("food_opening_time");
                                        String food_closing_time = jsonObject1.getString("food_closing_time");
                                        String food_time_msg = jsonObject1.getString("food_time_msg");
                                        JSONArray jsonArray = jsonObject1.getJSONArray("variant_list");
                                        ArrayList<VariantList> variantLists = new ArrayList<>();
                                        for (int k=0; k<jsonArray.length(); k++){
                                            JSONObject jsonObject11 = jsonArray.getJSONObject(k);
                                            String var_id = jsonObject11.getString("id");
                                            String volume = jsonObject11.getString("volume");
                                            String price = jsonObject11.getString("price");
                                            String cart_status = jsonObject11.getString("cart_status");
                                            String var_qty = jsonObject11.getString("variant_qty");
                                            variantLists.add(new VariantList(var_id,volume,price,cart_status,var_qty,new ArrayList<SubMenuList>()));
                                        }
                                        menuLists.add(new HomeMenuList(menu_id,menu_name,menu_image,cat_id,food_opening_time,food_closing_time,food_time_msg,variantLists));
                                        break;
                                    case Model.TRENDING:
                                        String tmenu_id = jsonObject1.getString("id");
                                        String tmenu_name = jsonObject1.getString("menu_name");
                                        String tmenu_image = jsonObject1.getString("image");
                                        String tcat_id = jsonObject1.getString("cat_id");
                                        String tfood_opening_time = jsonObject1.getString("food_opening_time");
                                        String tfood_closing_time = jsonObject1.getString("food_closing_time");
                                        String tfood_time_msg = jsonObject1.getString("food_time_msg");
                                        JSONArray tjsonArray = jsonObject1.getJSONArray("variant_list");
                                        ArrayList<VariantList> tvariantLists = new ArrayList<>();
                                        for (int k=0; k<tjsonArray.length(); k++){
                                            JSONObject jsonObject11 = tjsonArray.getJSONObject(k);
                                            String var_id = jsonObject11.getString("id");
                                            String volume = jsonObject11.getString("volume");
                                            String price = jsonObject11.getString("price");
                                            String cart_status = jsonObject11.getString("cart_status");
                                            String var_qty = jsonObject11.getString("variant_qty");
                                            tvariantLists.add(new VariantList(var_id,volume,price,cart_status,var_qty,new ArrayList<SubMenuList>()));
                                        }
                                        menuLists.add(new HomeMenuList(tmenu_id,tmenu_name,tmenu_image,tcat_id,tfood_opening_time,tfood_closing_time,tfood_time_msg,tvariantLists));
                                        break;

                                    case Model.STEPS:
                                        String steps_id = jsonObject1.getString("id");
                                        String steps_title = jsonObject1.getString("title");
                                        String steps_image = jsonObject1.getString("image");
                                        steps.add(new Steps(steps_id,steps_title,steps_image));
                                        break;

                                }
                            }
                            switch (type) {
                                case Model.SHOPS:
                                    model.setId(id);
                                    model.setTitle(title);
                                    model.setLink(link);
                                    model.setType(Model.HOME_SHOP);
                                    model.setShops(shops);
                                    randomModelList.add(model);
                                    break;
                                case Model.BANNER:
                                    model.setId(id);
                                    model.setTitle(title);
                                    model.setLink(link);
                                    model.setType(Model.HOME_BANNER);
                                    model.setBanners(banners);
                                    randomModelList.add(model);
                                    break;
                                case Model.CATEGORY:
                                    model.setId(id);
                                    model.setTitle(title);
                                    model.setLink(link);
                                    model.setCat_list(cat_list);
                                    model.setType(Model.HOME_CATEGORY);
                                    model.setCategoryLists(categoryLists);
                                    randomModelList.add(model);
                                    break;
                                case Model.MENU:
                                    model.setId(id);
                                    model.setTitle(title);
                                    model.setLink(link);
                                    model.setType(Model.HOME_MENU);
                                    model.setMenuLists(menuLists);
                                    randomModelList.add(model);
                                    break;
                                case Model.STRIP:
                                    model.setId(id);
                                    model.setTitle(title);
                                    model.setSubtitle(subtitle);
                                    model.setLink(link);
                                    model.setBg_color(bg_color);
                                    model.setBorder_color(border_color);
                                    model.setStrip_link(Strip_link);
                                    model.setType(Model.HOME_STRIP);
                                    model.setIcon(icon);
                                    randomModelList.add(model);
                                    break;
                                case Model.TRENDING:
                                    model.setId(id);
                                    model.setTitle(title);
                                    model.setLink(link);
                                    model.setType(Model.HOME_TRENDING);
                                    model.setMenuLists(menuLists);
                                    randomModelList.add(model);
                                    break;
                                case Model.STEPS:
                                    model.setId(id);
                                    model.setTitle(title);
                                    model.setLink(link);
                                    model.setType(Model.HOME_STEPS);
                                    model.setSteps(steps);
                                    randomModelList.add(model);
                                    break;
                            }
                        }
                        if (type.equals(Model.SEARCH)){
                            model.setId(id);
                            model.setTitle(title);
                            model.setLink(link);
                            model.setType(Model.HOME_SEARCH);
                            randomModelList.add(model);
                        }

                    }

                    if (randomModelList.size()>0) {
                        multiViewTypeAdapter = new MultiViewTypeAdapter(getActivity(), randomModelList, HomeFragment.this);
                        recycler_home.setAdapter(multiViewTypeAdapter);
                        progresbar_home.setVisibility(View.GONE);
                        loader.setVisibility(View.GONE);
                        main_layout.setVisibility(View.VISIBLE);
                        no_data.setVisibility(View.GONE);
                        no_connection.setVisibility(View.GONE);
                    }else{
                        loader.setVisibility(View.GONE);
                        progresbar_home.setVisibility(View.GONE);
                        main_layout.setVisibility(View.GONE);
                        no_connection.setVisibility(View.GONE);
                        no_data.setVisibility(View.VISIBLE);
                    }
                    swipe_refresh.setRefreshing(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                    progresbar_home.setVisibility(View.GONE);
                    loader.setVisibility(View.GONE);
                    main_layout.setVisibility(View.GONE);
                    no_connection.setVisibility(View.GONE);
                    no_data.setVisibility(View.VISIBLE);
                    swipe_refresh.setRefreshing(false);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                requestQueue.stop();
                progresbar_home.setVisibility(View.GONE);
                loader.setVisibility(View.GONE);
                main_layout.setVisibility(View.GONE);
                no_connection.setVisibility(View.GONE);
                no_data.setVisibility(View.VISIBLE);
                swipe_refresh.setRefreshing(false);
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
//        requestQueue.add(stringRequest);
    }

    @Override
    public void onRefresh() {
        getHome();
    }
}