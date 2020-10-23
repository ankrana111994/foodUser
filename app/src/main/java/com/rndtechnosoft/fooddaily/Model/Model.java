package com.rndtechnosoft.fooddaily.Model;

import java.util.ArrayList;

public class Model {

    private String id,title,subtitle,link,bg_color, border_color, strip_link, icon, cat_list;
    public int type;
    public static final int HOME_BANNER = 1;
    public static final int HOME_SEARCH = 2;
    public static final int HOME_CATEGORY=3;
    public static final int HOME_MENU=4;
    public static final int HOME_STRIP=5;
    public static final int HOME_TRENDING=6;
    public static final int HOME_STEPS=7;
    public static final int HOME_SHOP=8;


    public static final String SHOPS = "Shops";
    public static final String BANNER = "Banner";
    public static final String SEARCH = "Search";
    public static final String CATEGORY = "Category";
    public static final String MENU = "Menu";
    public static final String STRIP = "Strip";
    public static final String TRENDING = "Trending";
    public static final String STEPS = "Steps";



    private ArrayList<Shops> shops;
    private ArrayList<Banner> banners;
    private ArrayList<HomeCategoryList> categoryLists;
    private ArrayList<HomeMenuList> menuLists;
    private ArrayList<Steps> steps;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getBg_color() {
        return bg_color;
    }

    public void setBg_color(String bg_color) {
        this.bg_color = bg_color;
    }

    public String getBorder_color() {
        return border_color;
    }

    public void setBorder_color(String border_color) {
        this.border_color = border_color;
    }

    public String getStrip_link() {
        return strip_link;
    }

    public void setStrip_link(String strip_link) {
        this.strip_link = strip_link;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getCat_list() {
        return cat_list;
    }

    public void setCat_list(String cat_list) {
        this.cat_list = cat_list;
    }
    public ArrayList<Shops> getShops() {
        return shops;
    }

    public void setShops(ArrayList<Shops> shops) {
        this.shops = shops;
    }
    public ArrayList<Banner> getBanners() {
        return banners;
    }

    public void setBanners(ArrayList<Banner> banners) {
        this.banners = banners;
    }

    public ArrayList<HomeCategoryList> getCategoryLists() {
        return categoryLists;
    }

    public void setCategoryLists(ArrayList<HomeCategoryList> categoryLists) {
        this.categoryLists = categoryLists;
    }

    public ArrayList<HomeMenuList> getMenuLists() {
        return menuLists;
    }

    public void setMenuLists(ArrayList<HomeMenuList> menuLists) {
        this.menuLists = menuLists;
    }

    public ArrayList<Steps> getSteps() {
        return steps;
    }

    public void setSteps(ArrayList<Steps> steps) {
        this.steps = steps;
    }
}
