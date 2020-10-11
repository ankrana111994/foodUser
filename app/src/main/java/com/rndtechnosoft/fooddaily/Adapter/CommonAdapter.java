package com.rndtechnosoft.fooddaily.Adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rndtechnosoft.fooddaily.Activity.NotificationDetailActivity;
import com.rndtechnosoft.fooddaily.Model.ItemNotification;
import com.rndtechnosoft.fooddaily.R;

import java.util.ArrayList;

public class CommonAdapter extends RecyclerView.Adapter<CommonAdapter.ItemRowHolder> {

    private ArrayList<ItemNotification> dataList;
    private Context mContext;

    public CommonAdapter(Context context, ArrayList<ItemNotification> dataList) {
        this.dataList = dataList;
        this.mContext = context;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_notification, parent, false);
        return new ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(ItemRowHolder holder, final int position) {
        final ItemNotification singleItem = dataList.get(position);
        holder.text.setText(singleItem.getNotificationTitle());
        holder.textMessage.setText(singleItem.getNotificationMessage());
        Glide.with(mContext).load(singleItem.getNotificationImage().replace(" ","%20")).thumbnail(Glide.with(mContext).load(R.drawable.loading)).into(holder.image);
        holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, NotificationDetailActivity.class);
                intent.putExtra("title", singleItem.getNotificationTitle());
                intent.putExtra("message", singleItem.getNotificationMessage());
                intent.putExtra("image", singleItem.getNotificationImage());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }


    public class ItemRowHolder extends RecyclerView.ViewHolder {
        public TextView text,textMessage;
        public LinearLayout lyt_parent;
        public ImageView image;
        public ItemRowHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            text = (TextView) itemView.findViewById(R.id.text);
            textMessage = (TextView) itemView.findViewById(R.id.textMessage);
            lyt_parent = (LinearLayout) itemView.findViewById(R.id.rootLayout);
        }
    }
}
