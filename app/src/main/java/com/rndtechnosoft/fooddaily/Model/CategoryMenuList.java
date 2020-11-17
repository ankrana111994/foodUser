package com.rndtechnosoft.fooddaily.Model;

public class CategoryMenuList {

    String id,food_type_icon,cat_food_type,rest_id,rest_name,food_type,cat_id,name,des,food_opening_time,food_closing_time,food_time_msg,wallpaper_image,price;

    public CategoryMenuList(String id, String food_type_icon,String cat_food_type, String rest_id,String rest_name, String food_type,String cat_id, String name, String des, String food_opening_time, String food_closing_time, String food_time_msg, String wallpaper_image, String price) {
        this.id = id;
        this.food_type_icon = food_type_icon;
        this.cat_food_type = cat_food_type;
        this.rest_id = rest_id;
        this.rest_name = rest_name;
        this.food_type = food_type;
        this.cat_id = cat_id;
        this.name = name;
        this.des = des;
        this.food_opening_time = food_opening_time;
        this.food_closing_time = food_closing_time;
        this.food_time_msg = food_time_msg;
        this.wallpaper_image = wallpaper_image;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public String getFood_type_icon() {
        return food_type_icon;
    }

    public String getCat_food_type() {
        return cat_food_type;
    }

    public void setCat_food_type(String cat_food_type) {
        this.cat_food_type = cat_food_type;
    }

    public String getRest_id() {
        return rest_id;
    }

    public String getRest_name() {
        return rest_name;
    }

    public String getFood_type() {
        return food_type;
    }

    public String getCat_id() {
        return cat_id;
    }

    public String getName() {
        return name;
    }

    public String getDes() {
        return des;
    }

    public String getFood_opening_time() {
        return food_opening_time;
    }

    public String getFood_closing_time() {
        return food_closing_time;
    }

    public String getFood_time_msg() {
        return food_time_msg;
    }

    public String getWallpaper_image() {
        return wallpaper_image;
    }

    public String getPrice() {
        return price;
    }

}
