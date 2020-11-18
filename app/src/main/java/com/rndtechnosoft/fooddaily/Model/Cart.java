package com.rndtechnosoft.fooddaily.Model;

import java.util.ArrayList;

public class Cart {

    String id, user_id, category_id, menu_id, menu_name, menu_image, variant_id, variant_volume_type, variant_price, variant_qty;
    ArrayList<Toppings> toppings;

    public Cart(String id, String user_id, String category_id, String menu_id, String menu_name, String menu_image, String variant_id, String variant_volume_type, String variant_price, String variant_qty, ArrayList<Toppings> toppings) {
        this.id = id;
        this.user_id = user_id;
        this.category_id = category_id;
        this.menu_id = menu_id;
        this.menu_name = menu_name;
        this.menu_image = menu_image;
        this.variant_id = variant_id;
        this.variant_volume_type = variant_volume_type;
        this.variant_price = variant_price;
        this.variant_qty = variant_qty;
        this.toppings = toppings;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getMenu_id() {
        return menu_id;
    }

    public void setMenu_id(String menu_id) {
        this.menu_id = menu_id;
    }

    public String getMenu_name() {
        return menu_name;
    }

    public void setMenu_name(String menu_name) {
        this.menu_name = menu_name;
    }

    public String getMenu_image() {
        return menu_image;
    }

    public void setMenu_image(String menu_image) {
        this.menu_image = menu_image;
    }

    public String getVariant_id() {
        return variant_id;
    }

    public void setVariant_id(String variant_id) {
        this.variant_id = variant_id;
    }

    public String getVariant_volume_type() {
        return variant_volume_type;
    }

    public void setVariant_volume_type(String variant_volume_type) {
        this.variant_volume_type = variant_volume_type;
    }

    public String getVariant_price() {
        return variant_price;
    }

    public void setVariant_price(String variant_price) {
        this.variant_price = variant_price;
    }

    public String getVariant_qty() {
        return variant_qty;
    }

    public void setVariant_qty(String variant_qty) {
        this.variant_qty = variant_qty;
    }

    public ArrayList<Toppings> getToppings() {
        return toppings;
    }

    public void setToppings(ArrayList<Toppings> toppings) {
        this.toppings = toppings;
    }
}
