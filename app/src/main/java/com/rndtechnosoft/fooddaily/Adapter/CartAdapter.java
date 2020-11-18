package com.rndtechnosoft.fooddaily.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.rndtechnosoft.fooddaily.Activity.CartActivity;
import com.rndtechnosoft.fooddaily.Activity.ForgotPasswordActivity;
import com.rndtechnosoft.fooddaily.Model.Cart;
import com.rndtechnosoft.fooddaily.R;
import com.rndtechnosoft.fooddaily.Util.Constants;
import com.rndtechnosoft.fooddaily.Util.SharedPref;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private Activity activity;
    ArrayList<Cart> carts;

    public CartAdapter(Activity activity, ArrayList<Cart> carts) {
        this.activity = activity;
        this.carts = carts;
    }

    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(activity).inflate(R.layout.cart_item, parent, false);

        return new CartAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(CartAdapter.ViewHolder holder, final int position) {
        final Cart cart=carts.get(position);
        Glide.with(activity).load(carts.get(position).getMenu_image()).thumbnail(Glide.with(activity).load(R.drawable.loading)).centerCrop().into(holder.imgMenu);
        holder.tvMenu.setText(carts.get(position).getMenu_name());
        holder.tvPrice.setText(SharedPref.getCurrency(activity)+" "+carts.get(position).getVariant_price());
        holder.tvQty.setText(carts.get(position).getVariant_qty());

//        GradientDrawable gd = new GradientDrawable();
//        if (SharedPref.getDASHED(activity).equalsIgnoreCase("1")) {
////            gd.setColor(activity.getResources().getColor(R.color.colorGrey));
//            gd.setCornerRadius(5);
////        gd.setStroke(2, Color.RED);
//          //  gd.setStroke(4, activity.getResources().getColor(R.color.colorGrey1), 12, 16);
//        }else{
////            gd.setColor(activity.getResources().getColor(R.color.colorGrey));
//            gd.setCornerRadius(55);
//           // gd.setStroke(4, activity.getResources().getColor(R.color.colorGrey1));
//        }
//        holder.rel_layout.setBackground(gd);

        holder.tvPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addtocart(cart.getCategory_id(),cart.getMenu_id(),cart.getMenu_name(),cart.getMenu_image(),cart.getVariant_id(),cart.getVariant_volume_type(),cart.getVariant_price(),Constants.INCREMENT);
            }
        });

        holder.tvMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addtocart(cart.getCategory_id(),cart.getMenu_id(),cart.getMenu_name(),cart.getMenu_image(),cart.getVariant_id(),cart.getVariant_volume_type(),cart.getVariant_price(),Constants.DECREMENT);
            }
        });

        if (carts.get(position).getToppings().size()>0){
            holder.extra_layout.setVisibility(View.VISIBLE);
            holder.recycler_extra.setLayoutManager(new LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false));
            holder.recycler_extra.setAdapter(new ToppingAdapter(activity,carts.get(position).getToppings()));
        }else{
            holder.extra_layout.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return carts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgMenu;
        TextView tvMenu,tvPrice,tvQty;
        ImageView tvPlus,tvMinus;
        RecyclerView recycler_extra;
        RelativeLayout extra_layout;
        CardView rel_layout;

        public ViewHolder(View itemView) {
            super(itemView);

            imgMenu = (ImageView) itemView.findViewById(R.id.imgMenu);
            tvMenu = (TextView) itemView.findViewById(R.id.tvMenu);
            tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
            tvQty = (TextView) itemView.findViewById(R.id.tvQty);
            tvPlus = (ImageView) itemView.findViewById(R.id.tvPlus);
            tvMinus = (ImageView) itemView.findViewById(R.id.tvMinus);
            recycler_extra = (RecyclerView) itemView.findViewById(R.id.recycler_extra);
            extra_layout = (RelativeLayout) itemView.findViewById(R.id.extra_layout);
            rel_layout = (CardView) itemView.findViewById(R.id.rel_layout);

        }
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
                            CartActivity cartActivity = (CartActivity) activity;
                            cartActivity.getCartList();
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
}