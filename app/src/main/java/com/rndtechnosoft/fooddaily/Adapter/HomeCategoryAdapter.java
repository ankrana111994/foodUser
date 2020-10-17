package com.rndtechnosoft.fooddaily.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.rndtechnosoft.fooddaily.Activity.CategoryDetailActivity;
import com.rndtechnosoft.fooddaily.Model.HomeCategoryList;
import com.rndtechnosoft.fooddaily.R;
import com.rndtechnosoft.fooddaily.Util.SharedPref;

import java.io.IOException;
import java.util.ArrayList;

public class HomeCategoryAdapter extends RecyclerView.Adapter<HomeCategoryAdapter.ViewHolder> {

    private Activity activity;
    ArrayList<HomeCategoryList> homeCategoryLists;

    public HomeCategoryAdapter(Activity activity, ArrayList<HomeCategoryList> homeCategoryLists) {
        this.activity = activity;
        this.homeCategoryLists = homeCategoryLists;
    }

    @Override
    public HomeCategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(activity).inflate(R.layout.home_single_item, parent, false);

        return new HomeCategoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HomeCategoryAdapter.ViewHolder holder, final int position) {

        Glide.with(activity).load(homeCategoryLists.get(position).getImage()).thumbnail(Glide.with(activity).load(R.drawable.loading)).centerCrop().into(holder.imgCategory);
        holder.tvCategory.setText(homeCategoryLists.get(position).getName());
//        holder.cat_lay.getBackground().setColorFilter(Color.parseColor("#ff3300"), PorterDuff.Mode.SRC_OVER);
        GradientDrawable gd = new GradientDrawable();
        if (SharedPref.getDASHED(activity).equalsIgnoreCase("1")) {
            gd.setColor(Color.WHITE);
            gd.setCornerRadius(50);
//        gd.setStroke(2, Color.RED);
          //  gd.setStroke(4, Color.parseColor(homeCategoryLists.get(position).getColor_code()), 12, 16);
        }else{
            gd.setColor(Color.WHITE);
            gd.setCornerRadius(50);
           // gd.setStroke(4, Color.parseColor(homeCategoryLists.get(position).getColor_code()));
        }
        holder.cat_lay.setBackground(gd);
        holder.cat_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, CategoryDetailActivity.class);
                intent.putExtra("position",position);
                intent.putExtra("cat_id",homeCategoryLists.get(position).getId());
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return homeCategoryLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgCategory;
        TextView tvCategory;
        CardView cat_lay;

        public ViewHolder(View itemView) {
            super(itemView);

            imgCategory = (ImageView) itemView.findViewById(R.id.imgCategory);
            tvCategory = (TextView) itemView.findViewById(R.id.tvCategory);
            cat_lay = (CardView) itemView.findViewById(R.id.cat_lay);

        }
    }
}