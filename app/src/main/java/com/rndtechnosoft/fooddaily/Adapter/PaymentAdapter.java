package com.rndtechnosoft.fooddaily.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.rndtechnosoft.fooddaily.Activity.CartActivity;
import com.rndtechnosoft.fooddaily.Activity.MainActivity;
import com.rndtechnosoft.fooddaily.Activity.MenuDetailActivity;
import com.rndtechnosoft.fooddaily.Activity.WebViewActivity;
import com.rndtechnosoft.fooddaily.Model.Banner;
import com.rndtechnosoft.fooddaily.Model.Payment;
import com.rndtechnosoft.fooddaily.R;
import com.rndtechnosoft.fooddaily.Util.SharedPref;

import java.util.ArrayList;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.ViewHolder> {

    private Activity activity;
    ArrayList<Payment> payments;
    String payment_type;

    public PaymentAdapter(Activity activity, ArrayList<Payment> payments) {
        this.activity = activity;
        this.payments = payments;
    }

    @Override
    public PaymentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(activity).inflate(R.layout.pay_item, parent, false);

        return new PaymentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PaymentAdapter.ViewHolder holder, final int position) {
        Glide.with(activity).load(payments.get(position).getImage()).centerCrop().thumbnail(Glide.with(activity).load(R.drawable.loading)).into(holder.imgPay);
        holder.tvPay.setText(payments.get(position).getTitle());
        final GradientDrawable gd = new GradientDrawable();
        if (SharedPref.getDASHED(activity).equalsIgnoreCase("1")) {
            gd.setColor(activity.getResources().getColor(R.color.colorWhite));
            gd.setCornerRadius(25);
          //  gd.setStroke(4, activity.getResources().getColor(R.color.colorGrey1), 12, 16);
        }else{
            gd.setColor(activity.getResources().getColor(R.color.colorWhite));
            gd.setCornerRadius(25);
          //  gd.setStroke(4, activity.getResources().getColor(R.color.colorGrey1));
        }
        holder.rel_layout.setBackgroundDrawable(gd);

        holder.rel_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payment_type = payments.get(position).getTitle();
                if (SharedPref.getDASHED(activity).equalsIgnoreCase("1")) {
                    gd.setColor(activity.getResources().getColor(R.color.colorAccentLightLight));
                    gd.setCornerRadius(25);
                  //  gd.setStroke(4, activity.getResources().getColor(R.color.colorGrey1), 12, 16);
                }else{
                    gd.setColor(activity.getResources().getColor(R.color.colorAccentLightLight));
                    gd.setCornerRadius(25);
                  //  gd.setStroke(4, activity.getResources().getColor(R.color.colorGrey1));
                }
                holder.rel_layout.setBackgroundDrawable(gd);
            }
        });

        String title = payments.get(position).getTitle();
        if (title.equalsIgnoreCase("Razorpay")){

        }else if (title.equalsIgnoreCase("PayPal")){

        }else if (title.equalsIgnoreCase("Cash On Delivery")){

        }

    }

    @Override
    public int getItemCount() {
        return payments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgPay;
        TextView tvPay;
        RelativeLayout rel_layout;

        public ViewHolder(View itemView) {
            super(itemView);

            imgPay = (ImageView) itemView.findViewById(R.id.pay_img);
            tvPay = (TextView) itemView.findViewById(R.id.tvPay);
            rel_layout = (RelativeLayout) itemView.findViewById(R.id.rel_layout);

        }
    }
}