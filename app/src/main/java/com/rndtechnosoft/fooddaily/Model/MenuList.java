package com.rndtechnosoft.fooddaily.Model;

import java.util.ArrayList;

public class MenuList {

    String id,cat_id, name, image, food_type_icon, des, food_opening_time, food_closing_time, food_time_msg;
    ArrayList<VariantList> variantLists;
    ArrayList<SubMenuList> subMenuLists;

    public MenuList(String id, String cat_id, String name, String image, String food_type_icon, String des, String food_opening_time, String food_closing_time, String food_time_msg, ArrayList<VariantList> variantLists) {
        this.id = id;
        this.cat_id = cat_id;
        this.name = name;
        this.image = image;
        this.food_type_icon = food_type_icon;
        this.des = des;
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

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
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

    public String getFood_type_icon() {
        return food_type_icon;
    }

    public void setFood_type_icon(String food_type_icon) {
        this.food_type_icon = food_type_icon;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
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

    public ArrayList<SubMenuList> getSubMenuLists() {
        return subMenuLists;
    }

    public void setSubMenuLists(ArrayList<SubMenuList> subMenuLists) {
        this.subMenuLists = subMenuLists;
    }
}
