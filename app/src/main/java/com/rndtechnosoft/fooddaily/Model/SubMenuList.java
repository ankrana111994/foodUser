package com.rndtechnosoft.fooddaily.Model;

public class SubMenuList {

    String id, sub_menu, price, status;

    public SubMenuList(String id, String sub_menu, String price, String status) {
        this.id = id;
        this.sub_menu = sub_menu;
        this.price = price;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSub_menu() {
        return sub_menu;
    }

    public void setSub_menu(String sub_menu) {
        this.sub_menu = sub_menu;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
