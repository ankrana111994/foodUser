package com.rndtechnosoft.fooddaily.Model;

import java.io.Serializable;

public class CategoryList implements Serializable {

    String id, category_name, category_image, count;

    public CategoryList(String id, String category_name, String category_image, String count) {
        this.id = id;
        this.category_name = category_name;
        this.category_image = category_image;
        this.count = count;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getCategory_image() {
        return category_image;
    }

    public void setCategory_image(String category_image) {
        this.category_image = category_image;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
