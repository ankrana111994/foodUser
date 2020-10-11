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

import com.bumptech.glide.Glide;
import com.rndtechnosoft.fooddaily.Activity.MenuDetailActivity;
import com.rndtechnosoft.fooddaily.Model.SearchList;
import com.rndtechnosoft.fooddaily.R;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private Activity activity;
    ArrayList<SearchList> searchLists;

    public SearchAdapter(Activity activity, ArrayList<SearchList> searchLists) {
        this.activity = activity;
        this.searchLists = searchLists;
    }

    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(activity).inflate(R.layout.search_list, parent, false);

        return new SearchAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchAdapter.ViewHolder holder, final int position) {
        Glide.with(activity).load(searchLists.get(position).getImage()).centerCrop().into(holder.imgMenu);
        holder.tvMenu.setText(searchLists.get(position).getName());
        holder.rel_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, MenuDetailActivity.class);
                intent.putExtra("menu_image",searchLists.get(position).getImage());
                intent.putExtra("menu_name",searchLists.get(position).getName());
                intent.putExtra("menu_id",searchLists.get(position).getId());
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return searchLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgMenu;
        TextView tvMenu;
        RelativeLayout rel_menu;

        public ViewHolder(View itemView) {
            super(itemView);

            imgMenu = (ImageView) itemView.findViewById(R.id.imgMenu);
            tvMenu = (TextView) itemView.findViewById(R.id.tvMenu);
            rel_menu = (RelativeLayout) itemView.findViewById(R.id.rel_menu);
        }
    }
}