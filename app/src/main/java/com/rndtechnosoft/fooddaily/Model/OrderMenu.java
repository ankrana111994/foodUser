package com.rndtechnosoft.fooddaily.Model;

import java.util.ArrayList;

public class OrderMenu {

    String menu_name, variant_price,variant_type,variant_qty;
    ArrayList<OrderTopping> toppings;

    public OrderMenu(String menu_name, String variant_price, String variant_type, String variant_qty, ArrayList<OrderTopping> toppings) {
        this.menu_name = menu_name;
        this.variant_price = variant_price;
        this.variant_type = variant_type;
        this.variant_qty = variant_qty;
        this.toppings = toppings;
    }

    public String getMenu_name() {
        return menu_name;
    }

    public void setMenu_name(String menu_name) {
        this.menu_name = menu_name;
    }

    public String getVariant_price() {
        return variant_price;
    }

    public void setVariant_price(String variant_price) {
        this.variant_price = variant_price;
    }

    public String getVariant_type() {
        return variant_type;
    }

    public void setVariant_type(String variant_type) {
        this.variant_type = variant_type;
    }

    public String getVariant_qty() {
        return variant_qty;
    }

    public void setVariant_qty(String variant_qty) {
        this.variant_qty = variant_qty;
    }

    public ArrayList<OrderTopping> getToppings() {
        return toppings;
    }

    public void setToppings(ArrayList<OrderTopping> toppings) {
        this.toppings = toppings;
    }
}
