package com.rndtechnosoft.fooddaily.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
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
import com.rndtechnosoft.fooddaily.Activity.CartActivity;
import com.rndtechnosoft.fooddaily.Activity.CategoryDetailActivity;
import com.rndtechnosoft.fooddaily.Activity.MainActivity;
import com.rndtechnosoft.fooddaily.Activity.MenuDetailActivity;
import com.rndtechnosoft.fooddaily.Activity.WebViewActivity;
import com.rndtechnosoft.fooddaily.Model.Banner;
import com.rndtechnosoft.fooddaily.Model.Shops;
import com.rndtechnosoft.fooddaily.R;
import com.rndtechnosoft.fooddaily.Util.SharedPref;

import java.util.ArrayList;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ViewHolder> {

    private Activity activity;
    ArrayList<Shops> shopsList;

    public ShopAdapter(Activity activity, ArrayList<Shops> shopsList) {
        this.activity = activity;
        this.shopsList = shopsList;
    }

    @Override
    public ShopAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(activity).inflate(R.layout.shop_item, parent, false);

        return new ShopAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ShopAdapter.ViewHolder holder, final int position) {
        Glide.with(activity).load(shopsList.get(position).getRestaurantImage()).centerCrop().thumbnail(Glide.with(activity).load(R.drawable.loading)).into(holder.imgShop);
        holder.tvShop.setText(shopsList.get(position).getRestaurantTitle());
      //  holder.tvBanner.setVisibility(View.GONE);
        GradientDrawable gd = new GradientDrawable();
        if (SharedPref.getDASHED(activity).equalsIgnoreCase("1")) {
            gd.setColor(Color.WHITE);
            gd.setCornerRadius(10);

//        gd.setStroke(2, Color.RED);
            //  gd.setStroke(4, Color.parseColor(homeCategoryLists.get(position).getColor_code()), 12, 16);
        }else{
            gd.setColor(Color.WHITE);
            gd.setCornerRadius(10);
            // gd.setStroke(4, Color.parseColor(homeCategoryLists.get(position).getColor_code()));
        }
        holder.cat_lay.setBackground(gd);
        final MainActivity mainActivity= (MainActivity) activity;

        holder.cat_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if (shopsList.get(position).getType().equalsIgnoreCase("Food")){
               // activity.startActivity(new Intent(activity, CategoryDetailActivity.class));
                Intent intent = new Intent(activity, CategoryDetailActivity.class);
                intent.putExtra("id",shopsList.get(position).getId());
               // intent.putExtra("cat_id",homeCategoryLists.get(position).getId());
                activity.startActivity(intent);
//                    Intent intent=new Intent(activity, MenuDetailActivity.class);
//                    intent.putExtra("menu_id",bannerList.get(position).getLink());
//                    intent.putExtra("menu_name",bannerList.get(position).getTitle());
//                    activity.startActivity(intent);
//                }else if (bannerList.get(position).getType().equalsIgnoreCase("Cart")){
//                    Intent intent=new Intent(activity, CartActivity.class);
//                    activity.startActivity(intent);
//                }else if (bannerList.get(position).getType().equalsIgnoreCase("Link")){
//                    Intent intent=new Intent(activity, WebViewActivity.class);
//                    intent.putExtra("link", bannerList.get(position).getLink());
//                    intent.putExtra("title", bannerList.get(position).getTitle());
//                    activity.startActivity(intent);
//                }else if (bannerList.get(position).getType().equalsIgnoreCase("Orders")){
//                    mainActivity.viewPager.setCurrentItem(1);
//                }else{
//                    mainActivity.viewPager.setCurrentItem(0);
//                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return shopsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgShop;
        TextView tvShop;
        CardView cat_lay;

        public ViewHolder(View itemView) {
            super(itemView);

            imgShop = (ImageView) itemView.findViewById(R.id.imgShop);
            tvShop = (TextView) itemView.findViewById(R.id.tvShop);
            cat_lay = (CardView) itemView.findViewById(R.id.cat_lay);

        }
    }
}