package com.rndtechnosoft.fooddaily.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.CountDownTimer;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rndtechnosoft.fooddaily.Activity.BillActivity;
import com.rndtechnosoft.fooddaily.Model.OrderList;
import com.rndtechnosoft.fooddaily.R;
import com.rndtechnosoft.fooddaily.Util.SharedPref;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private Activity activity;
    ArrayList<OrderList> orderLists;
    private long milliseconds;
    private long startTime;
    private CountDownTimer mCountDownTimer;

    public OrderAdapter(Activity activity, ArrayList<OrderList> orderLists) {
        this.activity = activity;
        this.orderLists = orderLists;
    }

    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(activity).inflate(R.layout.order_list, parent, false);

        return new OrderAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final OrderAdapter.ViewHolder holder, final int position) {
        final OrderList orderList = orderLists.get(position);
        String order_id = "<font color="+activity.getResources().getColor(R.color.colorBlack)+">Order No : </font> <font color="+activity.getResources().getColor(R.color.colorAccent)+"><b>"+orderList.getUnique_order_id()+"</b></font>";
        holder.tvOrder.setText(Html.fromHtml(order_id));
        holder.tvTotal.setText(SharedPref.getCurrency(activity)+" "+orderList.getTotal_price());
        holder.recyclerMenu.setLayoutManager(new LinearLayoutManager(activity));
        holder.recyclerMenu.setAdapter(new OrderMenuAdapter(activity,orderList.getMenus()));
        holder.btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, BillActivity.class).putExtra("order_id",Integer.parseInt(orderList.getOrder_id()));
                activity.startActivity(intent);
            }
        });

        GradientDrawable gd = new GradientDrawable();
        if (SharedPref.getDASHED(activity).equalsIgnoreCase("1")) {
            gd.setColor(activity.getResources().getColor(R.color.colorWhite));
            gd.setCornerRadius(25);
         //   gd.setStroke(4, activity.getResources().getColor(R.color.colorGrey1), 12, 16);
        }else{
            gd.setColor(activity.getResources().getColor(R.color.colorWhite));
            gd.setCornerRadius(25);
         //   gd.setStroke(4, activity.getResources().getColor(R.color.colorGrey1));
        }
        holder.rel_layout.setBackground(gd);

        if (SharedPref.getDASHED(activity).equalsIgnoreCase("1")) {
            holder.view1.setBackground(activity.getResources().getDrawable(R.drawable.dashed_line));
        }else{
            holder.view1.setBackground(activity.getResources().getDrawable(R.drawable.stroke_line));
        }

        if (!orderList.getDiscount().equals("") && !orderList.getDiscount().equals("0")) {
            holder.tvDiscount.setText(SharedPref.getCurrency(activity)+orderList.getDiscount());
            holder.discount_layout.setVisibility(View.VISIBLE);
        }else{
            holder.discount_layout.setVisibility(View.GONE);
        }

        if (!orderList.getWallet_amount().equals("") && !orderList.getWallet_amount().equals("0")) {
            holder.tvWallet.setText(SharedPref.getCurrency(activity)+orderList.getWallet_amount());
            holder.wallet_layout.setVisibility(View.VISIBLE);
        }else{
            holder.wallet_layout.setVisibility(View.GONE);
        }

        if (!orderList.getDelivery().equals("") && !orderList.getDelivery().equals("0")) {
            holder.tvDelivery.setText(SharedPref.getCurrency(activity)+orderList.getDelivery());
            holder.delivery_layout.setVisibility(View.VISIBLE);
        }else{
            holder.delivery_layout.setVisibility(View.GONE);
        }

        holder.tvStatus.setText(orderList.getStatus());

        if (!orderLists.get(position).getStatus().equalsIgnoreCase("New Order") && !orderLists.get(position).getStatus().equalsIgnoreCase("Completed")) {
            holder.tvDeliverytime.setVisibility(View.VISIBLE);
            String del_time = orderLists.get(position).getDelivery_time();
            if (del_time != null && !del_time.equals("")) {
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy, HH:mm:ss");
                formatter.setLenient(false);

                holder.tvDeliverytime.setVisibility(View.VISIBLE);

                String endTime = del_time;
                Date endDate;
                try {
                    endDate = formatter.parse(endTime);
                    milliseconds = endDate.getTime();

                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                startTime = System.currentTimeMillis();
                mCountDownTimer = new CountDownTimer(milliseconds, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        startTime = startTime - 1;
                        Long serverUptimeSeconds =
                                (millisUntilFinished - startTime) / 1000;
                        long min = ((serverUptimeSeconds % 86400) % 3600) / 60;
                        long hour = ((serverUptimeSeconds % 86400) / 3600);
                        long sec = ((serverUptimeSeconds % 86400) % 3600) % 60;
                        if (min > 0) {
                            if (hour<10 && min<10 && sec<10) {
                                String minutesLeft = null;
                                try {
                                    minutesLeft = String.format(orderLists.get(position).getDelmsg(), "0"+hour + ":0" + min + ":0" + sec);
                                    holder.tvDeliverytime.setText(minutesLeft);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }else if (hour<10 && min<10){
                                String minutesLeft = null;
                                try {
                                    minutesLeft = String.format(orderLists.get(position).getDelmsg(), "0"+hour + ":0" + min + ":" + sec);
                                    holder.tvDeliverytime.setText(minutesLeft);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }else if (hour<10 && sec<10){
                                String minutesLeft = null;
                                try {
                                    minutesLeft = String.format(orderLists.get(position).getDelmsg(), "0"+hour + ":" + min + ":0" + sec);
                                    holder.tvDeliverytime.setText(minutesLeft);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }else if (min<10 && sec<10){
                                String minutesLeft = null;
                                try {
                                    minutesLeft = String.format(orderLists.get(position).getDelmsg(), hour + ":0" + min + ":0" + sec);
                                    holder.tvDeliverytime.setText(minutesLeft);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }else if (hour<10){
                                String minutesLeft = null;
                                try {
                                    minutesLeft = String.format(orderLists.get(position).getDelmsg(), "0"+hour + ":" + min + ":" + sec);
                                    holder.tvDeliverytime.setText(minutesLeft);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }else if (min<10){
                                String minutesLeft = null;
                                try {
                                    minutesLeft = String.format(orderLists.get(position).getDelmsg(), hour + ":0" + min + ":" + sec);
                                    holder.tvDeliverytime.setText(minutesLeft);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }else if (sec<10){
                                String minutesLeft = null;
                                try {
                                    minutesLeft = String.format(orderLists.get(position).getDelmsg(), hour + ":" + min + ":0" + sec);
                                    holder.tvDeliverytime.setText(minutesLeft);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }else {
                                String minutesLeft = null;
                                try {
                                    minutesLeft = String.format(orderLists.get(position).getDelmsg(), hour + ":" + min + ":" + sec);
                                    holder.tvDeliverytime.setText(minutesLeft);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                        } else {
                            try {
//                                    holder.deliveryTime.setText("Your order is on the way");
                                holder.tvDeliverytime.setVisibility(View.GONE);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFinish() {
//                            holder.deliveryTime.setText(orderLists.get(position).getDel_done());
                        holder.tvDeliverytime.setVisibility(View.GONE);
                    }
                }.start();
            }
        }
        if (orderLists.get(position).getStatus().equalsIgnoreCase("Completed")){
            holder.tvDeliverytime.setVisibility(View.VISIBLE);
            holder.tvDeliverytime.setText(orderLists.get(position).getDeldone());
        }


    }

    @Override
    public int getItemCount() {
        return orderLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RecyclerView recyclerMenu;
        TextView tvTotal,btnView,tvOrder,tvDeliverytime,tvStatus,tvDiscount,tvWallet,tvDelivery;
        LinearLayout discount_layout,wallet_layout,delivery_layout;
        RelativeLayout rel_layout;
        View view1;

        public ViewHolder(View itemView) {
            super(itemView);

            recyclerMenu = itemView.findViewById(R.id.recycler_menu);
            rel_layout = itemView.findViewById(R.id.rel_layout);
            tvTotal = itemView.findViewById(R.id.tvTotal);
            tvOrder = itemView.findViewById(R.id.tvOrder);
            btnView = itemView.findViewById(R.id.btnView);
            tvDeliverytime = itemView.findViewById(R.id.tvDeliverytime);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvDiscount = itemView.findViewById(R.id.tvDiscount);
            tvWallet = itemView.findViewById(R.id.tvWallet);
            tvDelivery = itemView.findViewById(R.id.tvDelivery);
            discount_layout = itemView.findViewById(R.id.discount_layout);
            wallet_layout = itemView.findViewById(R.id.wallet_layout);
            delivery_layout = itemView.findViewById(R.id.delivery_layout);
            view1 = itemView.findViewById(R.id.view1);
        }
    }
}