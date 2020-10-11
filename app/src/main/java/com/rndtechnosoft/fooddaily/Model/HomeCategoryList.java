package com.rndtechnosoft.fooddaily.Model;

public class HomeCategoryList {

    String id, name, image, color_code, bg_color;

    public HomeCategoryList(String id, String name, String image, String color_code, String bg_color) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.color_code = color_code;
        this.bg_color = bg_color;
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

    public String getColor_code() {
        return color_code;
    }

    public void setColor_code(String color_code) {
        this.color_code = color_code;
    }

    public String getBg_color() {
        return bg_color;
    }

    public void setBg_color(String bg_color) {
        this.bg_color = bg_color;
    }
}
