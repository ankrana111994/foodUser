package com.rndtechnosoft.fooddaily.Model;

import java.util.ArrayList;

public class VariantList {

    String id, volume, price, status, qty;
    ArrayList<SubMenuList> subMenuLists;

    public VariantList(String id, String volume, String price, String status, String qty, ArrayList<SubMenuList> subMenuLists) {
        this.id = id;
        this.volume = volume;
        this.price = price;
        this.status = status;
        this.qty = qty;
        this.subMenuLists = subMenuLists;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
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

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public ArrayList<SubMenuList> getSubMenuLists() {
        return subMenuLists;
    }

    public void setSubMenuLists(ArrayList<SubMenuList> subMenuLists) {
        this.subMenuLists = subMenuLists;
    }
}
