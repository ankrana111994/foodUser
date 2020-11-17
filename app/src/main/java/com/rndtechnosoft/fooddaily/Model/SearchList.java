package com.rndtechnosoft.fooddaily.Model;

public class SearchList {

    String id, name, image, food_type_icon, des;

    public SearchList(String id, String name, String image, String food_type_icon, String des) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.food_type_icon = food_type_icon;
        this.des = des;
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
}
