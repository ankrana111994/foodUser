package com.rndtechnosoft.fooddaily.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rndtechnosoft.fooddaily.Activity.CategoryDetailActivity;
import com.rndtechnosoft.fooddaily.Model.MenuList;
import com.rndtechnosoft.fooddaily.Model.VariantList;
import com.rndtechnosoft.fooddaily.R;
import com.rndtechnosoft.fooddaily.Util.Constants;
import com.rndtechnosoft.fooddaily.Util.SharedPref;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VariantAdapter extends RecyclerView.Adapter<VariantAdapter.ViewHolder> {

    private Activity activity;
    ArrayList<VariantList> variantLists;
    MenuList menuList;
    LinearLayout sublayout;
    RecyclerView topping_recycler;

    public VariantAdapter(Activity activity, MenuList menuList, ArrayList<VariantList> variantLists, LinearLayout sublayout, RecyclerView topping_recycler) {
        this.activity = activity;
        this.menuList = menuList;
        this.variantLists = variantLists;
        this.sublayout = sublayout;
        this.topping_recycler = topping_recycler;
    }

    @Override
    public VariantAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(activity).inflate(R.layout.menu_variant, parent, false);

        return new VariantAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final VariantAdapter.ViewHolder holder, final int position) {
        holder.tvVariant.setText(variantLists.get(position).getVolume());
        holder.tvPrice.setText(SharedPref.getCurrency(activity)+variantLists.get(position).getPrice());
        holder.tvPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (variantLists.get(position).getSubMenuLists().size()>0) {
                    sublayout.setVisibility(View.VISIBLE);
                    topping_recycler.setLayoutManager(new LinearLayoutManager(activity));
                    topping_recycler.setAdapter(new SubMenuAdapter(activity,menuList,variantLists.get(position),variantLists.get(position).getSubMenuLists()));
                }else
                    sublayout.setVisibility(View.GONE);

                int qty = Integer.parseInt(holder.tvQty.getText().toString());
                holder.tvQty.setText(String.valueOf(qty+1));

                //add to cart or update cart
                addtocart(menuList.getCat_id(),menuList.getId(),menuList.getName(),menuList.getImage(),variantLists.get(position).getId(),variantLists.get(position).getVolume(),variantLists.get(position).getPrice(),Constants.INCREMENT);
            }
        });

        holder.tvMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = Integer.parseInt(holder.tvQty.getText().toString());
                if (qty>0) {
                    if (variantLists.get(position).getSubMenuLists().size()>0) {
                        sublayout.setVisibility(View.VISIBLE);
                        topping_recycler.setLayoutManager(new LinearLayoutManager(activity));
                        topping_recycler.setAdapter(new SubMenuAdapter(activity, menuList, variantLists.get(position), variantLists.get(position).getSubMenuLists()));
                    } else {
                        sublayout.setVisibility(View.GONE);
                    }

                    holder.tvQty.setText(String.valueOf(qty - 1));

                    //add to cart or update cart
                    addtocart(menuList.getCat_id(), menuList.getId(), menuList.getName(), menuList.getImage(), variantLists.get(position).getId(), variantLists.get(position).getVolume(), variantLists.get(position).getPrice(), Constants.DECREMENT);
                }
            }
        });

        holder.tvQty.setText(variantLists.get(position).getQty());
    }

    private void addtocart(final String cat_id, final String id, final String name, final String image, final String variant_id, final String volume, final String price, final String operation) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.add_cart,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Response
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject jsonObject1 = jsonObject.getJSONObject("MANAGE_CART");
                            String message = jsonObject1.getString("message");
                            CategoryDetailActivity detailActivity = (CategoryDetailActivity) activity;
                            detailActivity.cartcount();
                            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Error
                        Log.d("AddToCart->",error.toString());
                    }
                }) {
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", SharedPref.getUserId(activity));
                params.put("category_id", cat_id);
                params.put("menu_id", id);
                params.put("menu_name", name);
                params.put("menu_image", image);
                params.put("variant_id", variant_id);
                params.put("variant_type", volume);
                params.put("variant_price", price);
                params.put("variant_qty", Constants.QUANTITY);
                params.put("operation", operation);
                return params;
            }
        };
        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        //adding the string request to request queue
        requestQueue.add(stringRequest);

    }

    @Override
    public int getItemCount() {
        return variantLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgBanner;
        TextView tvVariant,tvPrice,tvMinus,tvPlus,tvQty;

        public ViewHolder(View itemView) {
            super(itemView);

            imgBanner = (ImageView) itemView.findViewById(R.id.imgBanner);
            tvVariant = (TextView) itemView.findViewById(R.id.tvVariant);
            tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
            tvMinus = (TextView) itemView.findViewById(R.id.tvMinus);
            tvPlus = (TextView) itemView.findViewById(R.id.tvPlus);
            tvQty = (TextView) itemView.findViewById(R.id.tvQty);

        }
    }
}