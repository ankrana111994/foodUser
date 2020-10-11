package com.rndtechnosoft.fooddaily.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rndtechnosoft.fooddaily.Adapter.OrderAdapter;
import com.rndtechnosoft.fooddaily.Model.OrderList;
import com.rndtechnosoft.fooddaily.Model.OrderMenu;
import com.rndtechnosoft.fooddaily.Model.OrderTopping;
import com.rndtechnosoft.fooddaily.R;
import com.rndtechnosoft.fooddaily.Util.Constants;
import com.rndtechnosoft.fooddaily.Util.SharedPref;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    RecyclerView recyclerView;
    AVLoadingIndicatorView progress_order;
    OrderAdapter adapter;
    TextView tvNoData;
    LottieAnimationView animation_view,loader;
    SwipeRefreshLayout swipe_refresh;

    public OrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        recyclerView = view.findViewById(R.id.recycler_order);
        tvNoData = view.findViewById(R.id.tvNoData);
        animation_view = view.findViewById(R.id.animation_view);
        loader = view.findViewById(R.id.loader);
        progress_order = view.findViewById(R.id.progress_order);
        swipe_refresh = view.findViewById(R.id.swipe_refresh);
        swipe_refresh.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.colorWhite));
        int col = getResources().getColor(R.color.colorAccent);
        swipe_refresh.setColorSchemeColors(col,col,col);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        swipe_refresh.setOnRefreshListener(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getOrderList();
    }

    private void getOrderList() {
        final ArrayList<OrderList> lists = new ArrayList<>();
        progress_order.setVisibility(View.GONE);
        tvNoData.setVisibility(View.GONE);
        animation_view.setVisibility(View.GONE);
        loader.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        final RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.order_list, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress_order.setVisibility(View.GONE);
                tvNoData.setVisibility(View.GONE);
                animation_view.setVisibility(View.GONE);
                loader.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("ORDER_LIST");
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String order_id = jsonObject1.getString("order_id");
                        String total_price = jsonObject1.getString("total_price");
                        String sub_price = jsonObject1.getString("sub_price");
                        String discount = jsonObject1.getString("discount");
                        String unique_order_id = jsonObject1.getString("unique_order_id");
                        String delivery = jsonObject1.getString("delivery");
                        String wallet_amount = jsonObject1.getString("wallet_amount");
                        String delivery_time = jsonObject1.getString("delivery_time");
                        String order_place_time = jsonObject1.getString("order_place_time");
                        String delmsg = jsonObject1.getString("delmsg");
                        String deldone = jsonObject1.getString("deldone");
                        String status = jsonObject1.getString("status");
                        ArrayList<OrderMenu> menus = new ArrayList<>();
                        if (jsonObject1.has("menu_list")) {
                            JSONArray arrayMenu = jsonObject1.getJSONArray("menu_list");
                            for (int j = 0; j < arrayMenu.length(); j++) {
                                JSONObject jsonMenu = arrayMenu.getJSONObject(j);
                                String menu_name = jsonMenu.getString("menu_name");
                                String variant_price = jsonMenu.getString("variant_price");
                                String variant_type = jsonMenu.getString("variant_type");
                                String variant_qty = jsonMenu.getString("variant_qty");
//                                JSONArray arrayTopping = jsonMenu.getJSONArray("topping_list");
                                ArrayList<OrderTopping> toppings = new ArrayList<>();
//                                for (int k = 0; k < arrayTopping.length(); k++) {
//                                    JSONObject jsonTopping = arrayTopping.getJSONObject(k);
//                                    if (!jsonTopping.has("message")) {
//                                        String topping_name = jsonTopping.getString("topping_name");
//                                        String topping_price = jsonTopping.getString("topping_price");
//                                        toppings.add(new OrderTopping(topping_name, topping_price));
//                                    }
//                                }
                                menus.add(new OrderMenu(menu_name, variant_price, variant_type, variant_qty, toppings));
                            }
                        }
                        lists.add(new OrderList(order_id,unique_order_id,total_price,sub_price,discount,delivery,wallet_amount,delivery_time,order_place_time,delmsg,deldone,status,menus));
                    }
                    if (lists.size()>0) {
                        recyclerView.setVisibility(View.VISIBLE);
                        recyclerView.setAdapter(new OrderAdapter(getActivity(), lists));
                        swipe_refresh.setRefreshing(false);
                    }else{
                        recyclerView.setVisibility(View.GONE);
                        progress_order.setVisibility(View.GONE);
                        tvNoData.setVisibility(View.VISIBLE);
                        animation_view.setVisibility(View.VISIBLE);
                        loader.setVisibility(View.GONE);
                        swipe_refresh.setRefreshing(false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    recyclerView.setVisibility(View.GONE);
                    progress_order.setVisibility(View.GONE);
                    tvNoData.setVisibility(View.VISIBLE);
                    animation_view.setVisibility(View.VISIBLE);
                    loader.setVisibility(View.GONE);
                    swipe_refresh.setRefreshing(false);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                requestQueue.stop();
                recyclerView.setVisibility(View.GONE);
                progress_order.setVisibility(View.GONE);
                loader.setVisibility(View.GONE);
                tvNoData.setVisibility(View.VISIBLE);
                animation_view.setVisibility(View.VISIBLE);
                swipe_refresh.setRefreshing(false);
            }
        }){
            @Override
            public Map<String, String> getParams(){
                Map<String, String>  params = new HashMap<String, String>();
                params.put("user_id", SharedPref.getUserId(getActivity()));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    @Override
    public void onRefresh() {
        getOrderList();
    }
}
