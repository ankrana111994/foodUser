package com.rndtechnosoft.fooddaily.Fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rndtechnosoft.fooddaily.Activity.CategoryDetailActivity;
import com.rndtechnosoft.fooddaily.Adapter.CategoryMenuAdapter;
import com.rndtechnosoft.fooddaily.Model.MenuList;
import com.rndtechnosoft.fooddaily.Model.SubMenuList;
import com.rndtechnosoft.fooddaily.Model.VariantList;
import com.rndtechnosoft.fooddaily.R;
import com.rndtechnosoft.fooddaily.Util.Constants;
import com.rndtechnosoft.fooddaily.Util.SharedPref;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CategoryFragment extends Fragment {

    View view;
    private String cat_id,res_id;
    ArrayList<MenuList> menuLists;
    RecyclerView recyclerView_menu;
    CategoryMenuAdapter adapter;
    TextView tvNoData;
    AVLoadingIndicatorView progress_bar;
    LottieAnimationView loader,loaderMain;

    public CategoryFragment() {
        // Required empty public constructor
    }

    public static CategoryFragment newInstance(String position) {
        CategoryFragment fragment=new CategoryFragment();
        Bundle args=new Bundle();
        args.putString("someInt",position);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_category, container, false);

        menuLists = new ArrayList<>();

        recyclerView_menu=view.findViewById(R.id.recyclerView_menu);
        tvNoData=view.findViewById(R.id.tvNoData);
        progress_bar=view.findViewById(R.id.progresbar_menu);
        loader = view.findViewById(R.id.loader);
        loaderMain = view.findViewById(R.id.loaderMain);
        recyclerView_menu.setLayoutManager(new LinearLayoutManager(getActivity()));

        if(getArguments()!=null) {
            Bundle bundle = getArguments();
            cat_id = bundle.getString("id");
            res_id = bundle.getString("res_id","");

            CategoryDetailActivity activity= (CategoryDetailActivity) getActivity();
        }

        progress_bar.hide();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        menuLists.clear();
        recyclerView_menu.setVisibility(View.GONE);
        getMenus();
    }

    private void getMenus() {
        tvNoData.setVisibility(View.GONE);
        loader.setVisibility(View.GONE);
        loaderMain.setVisibility(View.VISIBLE);
        final RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.menu_cat_wise+ cat_id +"&user_id="+ SharedPref.getUserId(getActivity())+"&restaurant_id="+res_id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json = new JSONObject(response);
                    JSONArray jsonArray = json.getJSONArray("MAIN_MENU_LIST");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject object = jsonArray.getJSONObject(i);
                        String id = object.getString("id");
                        String name = object.getString("name");
                        String image = object.getString("image");
                        String food_type_icon = object.getString("food_type_icon");
                        String des = object.getString("des");
                        String food_opening_time = object.getString("food_opening_time");
                        String food_closing_time = object.getString("food_closing_time");
                        String food_time_msg = object.getString("food_time_msg");
                        JSONArray arrayVariant = object.getJSONArray("variant_list");
                        ArrayList<VariantList> variantLists = new ArrayList<>();
                        for (int j = 0; j < arrayVariant.length(); j++){
                            JSONObject jsonObject = arrayVariant.getJSONObject(j);
                            String id_variant = jsonObject.getString("id");
                            String volume = jsonObject.getString("volume");
                            String price = jsonObject.getString("price");
                            String cart_status = jsonObject.getString("cart_status");
                            String variant_qty = jsonObject.getString("variant_qty");
                            JSONArray arraySubMenu = jsonObject.getJSONArray("sub_menu_list");
                            ArrayList<SubMenuList> subMenuLists = new ArrayList<>();
                            for (int k = 0; k < arraySubMenu.length(); k++){
                                JSONObject jsonObject1 = arraySubMenu.getJSONObject(k);
                                if (jsonObject1.getString("error").equals("False")){
                                    String id_submenu = jsonObject1.getString("id");
                                    String sub_menu = jsonObject1.getString("sub_menu");
                                    String pricesub = jsonObject1.getString("price");
                                    String status = jsonObject1.getString("topping_status");
                                    subMenuLists.add(new SubMenuList(id_submenu, sub_menu, pricesub, status));
                                }
                            }
                            variantLists.add(new VariantList(id_variant,volume,price,cart_status,variant_qty,subMenuLists));
                        }

                        menuLists.add(new MenuList(id,cat_id,name,image,food_type_icon,des,food_opening_time,food_closing_time,food_time_msg,variantLists));
                    }

                    if (menuLists.size()>0) {
                        adapter = new CategoryMenuAdapter(getActivity(), menuLists);
                        recyclerView_menu.setAdapter(adapter);
                        tvNoData.setVisibility(View.GONE);
                        loader.setVisibility(View.GONE);
                        loaderMain.setVisibility(View.GONE);
                        recyclerView_menu.setVisibility(View.VISIBLE);
                    }else{
                        tvNoData.setVisibility(View.VISIBLE);
                        loader.setVisibility(View.VISIBLE);
                        loaderMain.setVisibility(View.GONE);
                        recyclerView_menu.setVisibility(View.GONE);
                    }
                    progress_bar.hide();

                } catch (JSONException e) {
                    e.printStackTrace();
                    tvNoData.setVisibility(View.VISIBLE);
                    loader.setVisibility(View.VISIBLE);
                    loaderMain.setVisibility(View.GONE);
                    recyclerView_menu.setVisibility(View.GONE);
                    progress_bar.hide();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                requestQueue.stop();
                tvNoData.setVisibility(View.VISIBLE);
                loader.setVisibility(View.VISIBLE);
                loaderMain.setVisibility(View.GONE);
                recyclerView_menu.setVisibility(View.GONE);
                progress_bar.hide();
            }
        });
        requestQueue.add(stringRequest);
    }
}
