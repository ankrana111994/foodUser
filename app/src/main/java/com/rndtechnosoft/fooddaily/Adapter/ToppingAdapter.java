package com.rndtechnosoft.fooddaily.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rndtechnosoft.fooddaily.Activity.CartActivity;
import com.rndtechnosoft.fooddaily.Model.Toppings;
import com.rndtechnosoft.fooddaily.R;
import com.rndtechnosoft.fooddaily.Util.Constants;
import com.rndtechnosoft.fooddaily.Util.SharedPref;

import java.util.ArrayList;

public class ToppingAdapter extends RecyclerView.Adapter<ToppingAdapter.ViewHolder> {

    private Activity activity;
    ArrayList<Toppings> toppings;

    public ToppingAdapter(Activity activity, ArrayList<Toppings> toppings) {
        this.activity = activity;
        this.toppings = toppings;
    }

    @Override
    public ToppingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(activity).inflate(R.layout.topping_item, parent, false);

        return new ToppingAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ToppingAdapter.ViewHolder holder, final int position) {
        holder.tvTopppingName.setText(toppings.get(position).getName());
        holder.tvPrice.setText(SharedPref.getCurrency(activity)+" "+ toppings.get(position).getPrice());
        holder.tvRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove_topping(toppings.get(position).getId());
            }
        });
    }

    private void remove_topping(String id) {
        final RequestQueue requestQueue = Volley.newRequestQueue(activity);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.delete_cart+"&type=toppings&menu_id="+id+"&user_id="+ SharedPref.getUserId(activity), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                CartActivity cartActivity = (CartActivity) activity;
                cartActivity.getCartList();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                requestQueue.stop();
            }
        });
        requestQueue.add(stringRequest);
    }

    @Override
    public int getItemCount() {
        return toppings.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTopppingName,tvPrice,tvRemove;

        public ViewHolder(View itemView) {
            super(itemView);

            tvTopppingName = (TextView) itemView.findViewById(R.id.tvToppingName);
            tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
            tvRemove = (TextView) itemView.findViewById(R.id.tvRemove);

        }
    }
}