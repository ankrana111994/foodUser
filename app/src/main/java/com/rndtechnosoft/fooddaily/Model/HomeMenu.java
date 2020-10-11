package com.rndtechnosoft.fooddaily.Model;

import java.util.ArrayList;

public class HomeMenu {

    String cat_id,cat_name;
    ArrayList<HomeMenuList> homeMenuLists;

    public HomeMenu(String cat_id, String cat_name, ArrayList<HomeMenuList> homeMenuLists) {
        this.cat_id = cat_id;
        this.cat_name = cat_name;
        this.homeMenuLists = homeMenuLists;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    public ArrayList<HomeMenuList> getHomeMenuLists() {
        return homeMenuLists;
    }

    public void setHomeMenuLists(ArrayList<HomeMenuList> homeMenuLists) {
        this.homeMenuLists = homeMenuLists;
    }
}
