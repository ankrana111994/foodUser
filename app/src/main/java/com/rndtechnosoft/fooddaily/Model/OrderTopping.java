package com.rndtechnosoft.fooddaily.Model;

public class OrderTopping {

    String topping_name, topping_price;

    public OrderTopping(String topping_name, String topping_price) {
        this.topping_name = topping_name;
        this.topping_price = topping_price;
    }

    public String getTopping_name() {
        return topping_name;
    }

    public void setTopping_name(String topping_name) {
        this.topping_name = topping_name;
    }

    public String getTopping_price() {
        return topping_price;
    }

    public void setTopping_price(String topping_price) {
        this.topping_price = topping_price;
    }
}
