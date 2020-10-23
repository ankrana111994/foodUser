package com.rndtechnosoft.fooddaily.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.rndtechnosoft.fooddaily.Activity.CategoryDetailActivity;
import com.rndtechnosoft.fooddaily.Activity.MainActivity;
import com.rndtechnosoft.fooddaily.Model.RestaurantCategoryList;
import com.rndtechnosoft.fooddaily.R;
import com.rndtechnosoft.fooddaily.Util.SharedPref;

import java.util.ArrayList;

public class RestaurantAdapter  extends RecyclerView.Adapter<RestaurantAdapter.ViewHolder> {

// ... view holder defined above...

// Store a member variable for the contacts
private ArrayList<RestaurantCategoryList> mRestaurantList;
    private Activity activity;
    String catId;
// Pass in the contact array into the constructor
public RestaurantAdapter(ArrayList<RestaurantCategoryList> restaurantList, Activity activity,String catId) {
    mRestaurantList = restaurantList;
    this.activity = activity;
     this.catId=catId;

}

    @NonNull
    @Override
    public RestaurantAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_restaurant, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
}

    @Override
    public void onBindViewHolder(@NonNull RestaurantAdapter.ViewHolder holder,final int position) {
        Glide.with(activity).load(mRestaurantList.get(position).getRestaurantImage()).centerCrop().thumbnail(Glide.with(activity).load(R.drawable.loading)).into(holder.imgShop);
        holder.tvShop.setText(mRestaurantList.get(position).getRestaurantTitle());
        //  holder.tvBanner.setVisibility(View.GONE);
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
      //  final MainActivity mainActivity= (MainActivity) activity;

        holder.cat_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if (shopsList.get(position).getType().equalsIgnoreCase("Food")){
                // activity.startActivity(new Intent(activity, CategoryDetailActivity.class));
                Intent intent = new Intent(activity, CategoryDetailActivity.class);
                intent.putExtra("id",mRestaurantList.get(position).getId());
                 intent.putExtra("cat_id",catId);
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
        return  mRestaurantList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {

       // ImageView imgCategory;
        TextView tvShop;
        private ImageView imgShop;
        CardView cat_lay;
                //,tvPrice, tvPlus, tvMinus;
       // CardView cat_lay;

        public ViewHolder(View itemView) {
            super(itemView);

        //    imgCategory = (ImageView) itemView.findViewById(R.id.imgCategory);
            imgShop = (ImageView) itemView.findViewById(R.id.imgShop);
            tvShop = (TextView) itemView.findViewById(R.id.tvShop);
            cat_lay = (CardView) itemView.findViewById(R.id.cat_lay);
//            tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
//            tvPlus = (TextView) itemView.findViewById(R.id.tvPlus);
//            tvMinus = (TextView) itemView.findViewById(R.id.tvMinus);
//            cat_lay = (CardView) itemView.findViewById(R.id.cat_lay);

        }
    }

}
