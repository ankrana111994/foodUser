package com.rndtechnosoft.fooddaily.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.rndtechnosoft.fooddaily.Model.OrderTopping;
import com.rndtechnosoft.fooddaily.R;
import com.rndtechnosoft.fooddaily.Util.SharedPref;

import java.util.ArrayList;

public class OrderToppingAdapter extends RecyclerView.Adapter<OrderToppingAdapter.ViewHolder> {

    private Activity activity;
    ArrayList<OrderTopping> orderLists;

    public OrderToppingAdapter(Activity activity, ArrayList<OrderTopping> orderLists) {
        this.activity = activity;
        this.orderLists = orderLists;
    }

    @Override
    public OrderToppingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(activity).inflate(R.layout.order_topping_list, parent, false);

        return new OrderToppingAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(OrderToppingAdapter.ViewHolder holder, final int position) {

        OrderTopping orderMenu=orderLists.get(position);
        holder.tvMenuname.setText(orderMenu.getTopping_name());
        holder.tvPrice.setText(SharedPref.getCurrency(activity)+orderMenu.getTopping_price());

    }

    @Override
    public int getItemCount() {
        return orderLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvMenuname,tvPrice;

        public ViewHolder(View itemView) {
            super(itemView);

            tvMenuname = itemView.findViewById(R.id.tvMenuname);
            tvPrice = itemView.findViewById(R.id.tvPrice);

        }
    }
}