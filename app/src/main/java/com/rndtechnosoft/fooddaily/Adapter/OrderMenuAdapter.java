package com.rndtechnosoft.fooddaily.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rndtechnosoft.fooddaily.Model.OrderMenu;
import com.rndtechnosoft.fooddaily.R;
import com.rndtechnosoft.fooddaily.Util.SharedPref;

import java.util.ArrayList;

public class OrderMenuAdapter extends RecyclerView.Adapter<OrderMenuAdapter.ViewHolder> {

    private Activity activity;
    ArrayList<OrderMenu> orderLists;

    public OrderMenuAdapter(Activity activity, ArrayList<OrderMenu> orderLists) {
        this.activity = activity;
        this.orderLists = orderLists;
    }

    @Override
    public OrderMenuAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(activity).inflate(R.layout.order_menu_list, parent, false);

        return new OrderMenuAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(OrderMenuAdapter.ViewHolder holder, final int position) {

        OrderMenu orderMenu=orderLists.get(position);
        holder.tvMenuname.setText(orderMenu.getMenu_name()+"( Qty :"+orderMenu.getVariant_qty()+" )");
        holder.tvPrice.setText(SharedPref.getCurrency(activity)+orderMenu.getVariant_price());
        if (orderMenu.getToppings().size()>0) {
            holder.tvextra.setVisibility(View.VISIBLE);
            holder.recycler_topping.setVisibility(View.VISIBLE);
            holder.recycler_topping.setLayoutManager(new LinearLayoutManager(activity));
            holder.recycler_topping.setAdapter(new OrderToppingAdapter(activity, orderMenu.getToppings()));
        }else {
            holder.tvextra.setVisibility(View.GONE);
            holder.recycler_topping.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return orderLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvMenuname,tvPrice,tvextra;
        RecyclerView recycler_topping;

        public ViewHolder(View itemView) {
            super(itemView);

            tvMenuname = itemView.findViewById(R.id.tvMenuname);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvextra = itemView.findViewById(R.id.tvextra);
            recycler_topping = itemView.findViewById(R.id.recycler_topping);

        }
    }
}