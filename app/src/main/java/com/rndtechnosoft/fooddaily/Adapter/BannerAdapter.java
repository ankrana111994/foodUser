package com.rndtechnosoft.fooddaily.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.rndtechnosoft.fooddaily.Activity.CartActivity;
import com.rndtechnosoft.fooddaily.Activity.MainActivity;
import com.rndtechnosoft.fooddaily.Activity.MenuDetailActivity;
import com.rndtechnosoft.fooddaily.Activity.WebViewActivity;
import com.rndtechnosoft.fooddaily.Model.Banner;
import com.rndtechnosoft.fooddaily.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.ViewHolder> {

    private Activity activity;
    ArrayList<Banner> bannerList;

    public BannerAdapter(Activity activity, ArrayList<Banner> bannerList) {
        this.activity = activity;
        this.bannerList = bannerList;
    }

    @Override
    public BannerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(activity).inflate(R.layout.banner_item, parent, false);

        return new BannerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BannerAdapter.ViewHolder holder, final int position) {
        Glide.with(activity).load(bannerList.get(position).getImage()).centerCrop().thumbnail(Glide.with(activity).load(R.drawable.loading)).into(holder.imgBanner);
        holder.tvBanner.setText(bannerList.get(position).getTitle());
        holder.tvBanner.setVisibility(View.GONE);

        final MainActivity mainActivity= (MainActivity) activity;
//        RelativeLayout Rl = holder.rel_layout;
if (bannerList.size()==1) {
    RelativeLayout.LayoutParams layout_description = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
            (int) activity.getResources().getDimension(R.dimen._160sdp));

    holder.rel_layout.setLayoutParams(layout_description);
}
        holder.rel_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (bannerList.get(position).getType().equalsIgnoreCase("Food")){
                    Intent intent=new Intent(activity, MenuDetailActivity.class);
                    intent.putExtra("menu_id",bannerList.get(position).getLink());
                    intent.putExtra("menu_name",bannerList.get(position).getTitle());
                    activity.startActivity(intent);
                }else if (bannerList.get(position).getType().equalsIgnoreCase("Cart")){
                    Intent intent=new Intent(activity, CartActivity.class);
                    activity.startActivity(intent);
                }else if (bannerList.get(position).getType().equalsIgnoreCase("Link")){
                    Intent intent=new Intent(activity, WebViewActivity.class);
                    intent.putExtra("link", bannerList.get(position).getLink());
                    intent.putExtra("title", bannerList.get(position).getTitle());
                    activity.startActivity(intent);
                }else if (bannerList.get(position).getType().equalsIgnoreCase("Orders")){
                    mainActivity.viewPager.setCurrentItem(1);
                }else{
                    mainActivity.viewPager.setCurrentItem(0);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return bannerList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgBanner;
        TextView tvBanner;
        RelativeLayout rel_layout;

        public ViewHolder(View itemView) {
            super(itemView);

            imgBanner = (ImageView) itemView.findViewById(R.id.imgBanner);
            tvBanner = (TextView) itemView.findViewById(R.id.tvBanner);
            rel_layout = (RelativeLayout) itemView.findViewById(R.id.rel_layout);

        }
    }
}