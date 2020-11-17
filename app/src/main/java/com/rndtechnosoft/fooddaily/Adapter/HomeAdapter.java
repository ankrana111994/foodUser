package com.rndtechnosoft.fooddaily.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.rndtechnosoft.fooddaily.Model.Banner;
import com.rndtechnosoft.fooddaily.R;

import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    private Activity activity;
    ArrayList<Banner> bannerList;

    public HomeAdapter(Activity activity, ArrayList<Banner> bannerList) {
        this.activity = activity;
        this.bannerList = bannerList;
    }

    @Override
    public HomeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(activity).inflate(R.layout.home_category_recycler, parent, false);

        return new HomeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HomeAdapter.ViewHolder holder, final int position) {
        holder.tvTitle.setText(bannerList.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return bannerList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RecyclerView recyclerView;
        TextView tvTitle;

        public ViewHolder(View itemView) {
            super(itemView);

            recyclerView = (RecyclerView) itemView.findViewById(R.id.recycler_category);
            tvTitle = (TextView) itemView.findViewById(R.id.tvCategory);

        }
    }
}