package com.rndtechnosoft.fooddaily.Model;

import java.util.ArrayList;

public class HomeMenuList {

    String id, name, image, cat_id, food_opening_time, food_closing_time, food_time_msg;
    ArrayList<VariantList> variantLists;

    public HomeMenuList(String id, String name, String image, String cat_id, String food_opening_time, String food_closing_time, String food_time_msg, ArrayList<VariantList> variantLists) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.cat_id = cat_id;
        this.food_opening_time = food_opening_time;
        this.food_closing_time = food_closing_time;
        this.food_time_msg = food_time_msg;
        this.variantLists = variantLists;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getFood_opening_time() {
        return food_opening_time;
    }

    public void setFood_opening_time(String food_opening_time) {
        this.food_opening_time = food_opening_time;
    }

    public String getFood_closing_time() {
        return food_closing_time;
    }

    public void setFood_closing_time(String food_closing_time) {
        this.food_closing_time = food_closing_time;
    }

    public String getFood_time_msg() {
        return food_time_msg;
    }

    public void setFood_time_msg(String food_time_msg) {
        this.food_time_msg = food_time_msg;
    }

    public ArrayList<VariantList> getVariantLists() {
        return variantLists;
    }

    public void setVariantLists(ArrayList<VariantList> variantLists) {
        this.variantLists = variantLists;
    }
}
