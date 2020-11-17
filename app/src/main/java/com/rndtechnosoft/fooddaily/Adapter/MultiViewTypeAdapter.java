package com.rndtechnosoft.fooddaily.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.rndtechnosoft.fooddaily.Activity.CartActivity;
import com.rndtechnosoft.fooddaily.Activity.CategoryDetailActivity;
import com.rndtechnosoft.fooddaily.Activity.MainActivity;
import com.rndtechnosoft.fooddaily.Activity.MenuDetailActivity;
import com.rndtechnosoft.fooddaily.Activity.SearchActivity;
import com.rndtechnosoft.fooddaily.Activity.WebViewActivity;
import com.rndtechnosoft.fooddaily.Fragment.HomeFragment;
import com.rndtechnosoft.fooddaily.Model.Model;
import com.rndtechnosoft.fooddaily.R;
import com.rndtechnosoft.fooddaily.Util.SharedPref;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.IOException;
import java.util.ArrayList;

public class MultiViewTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Activity activity;
    ArrayList<Model> homeModel;
    HomeFragment fragment;

    public MultiViewTypeAdapter(Activity activity, ArrayList<Model> randomModelList, HomeFragment homeFragment) {
        this.activity = activity;
        this.homeModel = randomModelList;
        this.fragment = homeFragment;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        switch (viewType){
            case Model.HOME_SHOP:
                view = layoutInflater.inflate(R.layout.shop_recycler, parent, false);
                return new ShopViewHolder(view);
            case Model.HOME_BANNER:
                view = layoutInflater.inflate(R.layout.banner_recycler, parent, false);
                return new BannerViewHolder(view);
            case Model.HOME_SEARCH:
                view = layoutInflater.inflate(R.layout.search_layout, parent, false);
                return new SearchViewHolder(view);
            case Model.HOME_CATEGORY:
                view = layoutInflater.inflate(R.layout.home_category_recycler, parent, false);
                return new CategoryViewHolder(view);
            case Model.HOME_MENU:
                view = layoutInflater.inflate(R.layout.home_category_recycler, parent, false);
                return new MenuViewHolder(view);
            case Model.HOME_STRIP:
                view = layoutInflater.inflate(R.layout.strip_layout, parent, false);
                return new StripViewHolder(view);
            case Model.HOME_TRENDING:
                view = layoutInflater.inflate(R.layout.home_category_recycler, parent, false);
                return new MenuViewHolder(view);
            case Model.HOME_STEPS:
                view = layoutInflater.inflate(R.layout.home_category_recycler, parent, false);
                return new StepsViewHolder(view);
            default:
                return null;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {

        final Model object =homeModel.get(position);
        if (object!=null){
            switch (object.type){
                case Model.HOME_SHOP:

                            ((ShopViewHolder) holder).tvTitle.setText(object.getTitle());

                    ((ShopViewHolder) holder).recycler_shop.setLayoutManager(new LinearLayoutManager(activity,RecyclerView.HORIZONTAL,false));
                    ((ShopViewHolder) holder).recycler_shop.setAdapter(new ShopAdapter(activity,object.getShops()));
                    ((ShopViewHolder) holder).progress_shop.setVisibility(View.GONE);
                    break;
                case Model.HOME_BANNER:
                    ((BannerViewHolder) holder).recycler_banner.setLayoutManager(new LinearLayoutManager(activity,RecyclerView.HORIZONTAL,false));
                    ((BannerViewHolder) holder).recycler_banner.setAdapter(new BannerAdapter(activity,object.getBanners()));
                    ((BannerViewHolder) holder).progress_banner.setVisibility(View.GONE);
                    break;

                case Model.HOME_SEARCH:
                    ((SearchViewHolder) holder).tvSearch.setHint(object.getTitle());
                    ((SearchViewHolder) holder).tvSearch.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                           activity.startActivity(new Intent(activity, SearchActivity.class));
                        }
                    });
                    break;

                case Model.HOME_CATEGORY:
                 //   ((CategoryViewHolder) holder).tvTitle.setText(object.getTitle());
                    ((CategoryViewHolder) holder).viewall.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                         //   activity.startActivity(new Intent(activity, CategoryDetailActivity.class));
                        }
                    });
                    if (object.getCategoryLists().size()>0) {
                        if (object.getCat_list().equalsIgnoreCase("HORIZONTAL")) {
                            ((CategoryViewHolder) holder).recyclerView.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false));
                        } else if (object.getCat_list().equalsIgnoreCase("VERTICAL")) {
                            ((CategoryViewHolder) holder).recyclerView.setLayoutManager(new GridLayoutManager(activity, 2));
                        } else {
                            ((CategoryViewHolder) holder).recyclerView.setLayoutManager(new GridLayoutManager(activity, 2));
                        }
                        ((CategoryViewHolder) holder).recyclerView.setAdapter(new HomeCategoryAdapter(activity, object.getCategoryLists()));
                    }
                    else {
                        ((CategoryViewHolder) holder).tvTitle.setVisibility(View.GONE);
                    }
                    break;

                case Model.HOME_MENU:
                    if (object.getMenuLists().size()>0) {
                        ((MenuViewHolder) holder).tvTitle.setText(object.getTitle());
                    ((MenuViewHolder) holder).recyclerView.setLayoutManager(new LinearLayoutManager(activity,RecyclerView.HORIZONTAL,false));
                    ((MenuViewHolder) holder).recyclerView.setAdapter(new HomeMenuAdapter(activity,object.getMenuLists(),object.getLink(),fragment));
                    ((MenuViewHolder) holder).viewall.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(activity, CategoryDetailActivity.class);
                            intent.putExtra("position",position);
                            intent.putExtra("cat_id",object.getLink());
                            activity.startActivity(intent);
                        }
                    });
                    }
                    else {
                        ((MenuViewHolder) holder).tvTitle.setVisibility(View.GONE);
                    }
                    break;
                case Model.HOME_STRIP:
                    ((StripViewHolder) holder).tvTitle.setText(object.getTitle());
                    ((StripViewHolder) holder).tvSubtitle.setText(object.getSubtitle());
                    GradientDrawable gd = new GradientDrawable();
                    if (SharedPref.getDASHED(activity).equalsIgnoreCase("1")) {
                        gd.setColor(Color.WHITE);
                      //  gd.setCornerRadius(50);
//        gd.setStroke(2, Color.RED);
                      //  gd.setStroke(4, Color.parseColor(object.getBorder_color()), 12, 16);
                    }else{
                        gd.setColor(Color.WHITE);
                     //   gd.setCornerRadius(50);
                      //  gd.setStroke(4, Color.parseColor(object.getBorder_color()));
                    }
                   // ((StripViewHolder) holder).rel_layout.setBackgroundDrawable(gd);
                    Glide.with(activity).load(object.getIcon()).thumbnail(Glide.with(activity).load(R.drawable.loading)).into(((StripViewHolder) holder).img_strip);

                    ((StripViewHolder) holder).rel_layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            MainActivity mainActivity = (MainActivity) activity;
                            if (object.getLink().equalsIgnoreCase("Food")){
                                Intent intent=new Intent(activity, MenuDetailActivity.class);
                                intent.putExtra("menu_id",object.getStrip_link());
                                intent.putExtra("menu_name",object.getTitle());
                                activity.startActivity(intent);
                            }else if (object.getLink().equalsIgnoreCase("Cart")){
                                Intent intent=new Intent(activity, CartActivity.class);
                                activity.startActivity(intent);
                            }else if (object.getLink().equalsIgnoreCase("Link")){
                                Intent intent=new Intent(activity, WebViewActivity.class);
                                intent.putExtra("link", object.getStrip_link());
                                intent.putExtra("title", object.getTitle());
                                activity.startActivity(intent);
                            }else if (object.getLink().equalsIgnoreCase("categories")){
                                Intent intent=new Intent(activity, CategoryDetailActivity.class);
                                intent.putExtra("position", position);
                                intent.putExtra("cat_id", object.getStrip_link());
                                activity.startActivity(intent);
                            }else if (object.getLink().equalsIgnoreCase("Orders")){
                                mainActivity.viewPager.setCurrentItem(1);
                            }else{
                                mainActivity.viewPager.setCurrentItem(0);
                            }

                        }
                    });

                    break;
                case Model.HOME_TRENDING:
                    ((MenuViewHolder) holder).tvTitle.setText(object.getTitle());
                    ((MenuViewHolder) holder).viewall.setVisibility(View.GONE);
                    ((MenuViewHolder) holder).recyclerView.setLayoutManager(new LinearLayoutManager(activity,RecyclerView.HORIZONTAL,false));
                    ((MenuViewHolder) holder).recyclerView.setAdapter(new HomeMenuAdapter(activity,object.getMenuLists(),object.getLink(),fragment));
                    ((MenuViewHolder) holder).viewall.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(activity, CategoryDetailActivity.class);
                            intent.putExtra("position",position);
                            intent.putExtra("cat_id",object.getLink());
                            activity.startActivity(intent);
                        }
                    });
                    break;
                case Model.HOME_STEPS:
                    ((StepsViewHolder) holder).tvTitle.setText(Html.fromHtml(object.getTitle()));
                    ((StepsViewHolder) holder).viewall.setVisibility(View.GONE);
                    ((StepsViewHolder) holder).recyclerView.setLayoutManager(new GridLayoutManager(activity,3));
                    ((StepsViewHolder) holder).recyclerView.setAdapter(new StepsAdapter(activity,object.getSteps()));
                    break;
                default:
                    break;
            }
        }

    }

    @Override
    public int getItemCount() {
        return homeModel.size();
    }

    @Override
    public int getItemViewType(int position) {
        switch (homeModel.get(position).type){
            case 1:
                return Model.HOME_BANNER;
            case 2:
                return Model.HOME_SEARCH;
            case 3:
                return Model.HOME_CATEGORY;
            case 4:
                return Model.HOME_MENU;
            case 5:
                return Model.HOME_STRIP;
            case 6:
                return Model.HOME_TRENDING;
            case 7:
                return Model.HOME_STEPS;
            case 8:
                return Model.HOME_SHOP;
            default:
                return -1;
        }
    }

    private class ShopViewHolder extends RecyclerView.ViewHolder {

        RecyclerView recycler_shop;
        AVLoadingIndicatorView progress_shop;
        TextView tvTitle;

        public ShopViewHolder(View view) {
            super(view);
            recycler_shop = (RecyclerView) itemView.findViewById(R.id.recycler_shop);
            tvTitle = (TextView) itemView.findViewById(R.id.tvCategory);

            progress_shop = (AVLoadingIndicatorView) itemView.findViewById(R.id.progress_shop);
        }
    }

    private class BannerViewHolder extends RecyclerView.ViewHolder {

        RecyclerView recycler_banner;
        AVLoadingIndicatorView progress_banner;

        public BannerViewHolder(View view) {
            super(view);
            recycler_banner = (RecyclerView) itemView.findViewById(R.id.recycler_banner);
            progress_banner = (AVLoadingIndicatorView) itemView.findViewById(R.id.progress_banner);
        }
    }

    private class SearchViewHolder extends RecyclerView.ViewHolder {

        TextView tvSearch;

        public SearchViewHolder(View view) {
            super(view);
            tvSearch = (TextView) itemView.findViewById(R.id.tvSearch);
        }
    }

    private class CategoryViewHolder extends RecyclerView.ViewHolder {

        RecyclerView recyclerView;
        TextView tvTitle,viewall;

        public CategoryViewHolder(View view) {
            super(view);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.recycler_category);
            tvTitle = (TextView) itemView.findViewById(R.id.tvCategory);
            viewall = (TextView) itemView.findViewById(R.id.viewall);
        }
    }

    private class MenuViewHolder extends RecyclerView.ViewHolder {

        RecyclerView recyclerView;
        TextView tvTitle,viewall;

        public MenuViewHolder(View view) {
            super(view);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.recycler_category);
            tvTitle = (TextView) itemView.findViewById(R.id.tvCategory);
            viewall = (TextView) itemView.findViewById(R.id.viewall);
        }
    }

    private class StripViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle,tvSubtitle;
        ImageView img_strip;
        RelativeLayout rel_layout;
//        CardView card_layout;

        public StripViewHolder(View view) {
            super(view);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvSubtitle = (TextView) itemView.findViewById(R.id.tvSubtitle);
            img_strip = (ImageView) itemView.findViewById(R.id.img_strip);
            rel_layout = (RelativeLayout) itemView.findViewById(R.id.rel_layout);
//            card_layout = (CardView) itemView.findViewById(R.id.card_layout);
        }
    }

    private class StepsViewHolder extends RecyclerView.ViewHolder {

        RecyclerView recyclerView;
        TextView tvTitle,viewall;

        public StepsViewHolder(View view) {
            super(view);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.recycler_category);
            tvTitle = (TextView) itemView.findViewById(R.id.tvCategory);
            viewall = (TextView) itemView.findViewById(R.id.viewall);
        }
    }

}
