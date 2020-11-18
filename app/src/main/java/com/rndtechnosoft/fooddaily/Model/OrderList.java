package com.rndtechnosoft.fooddaily.Model;

import java.util.ArrayList;

public class OrderList {

    String order_id, unique_order_id, total_price, sub_price, discount, delivery, wallet_amount, delivery_time, order_place_time, delmsg, deldone, status;
    ArrayList<OrderMenu> menus;

   /* public OrderList(String order_id, String total_price, String sub_price, String discount, String wallet_amount, ArrayList<OrderMenu> menus) {
        this.order_id = order_id;
        this.total_price = total_price;
        this.sub_price = sub_price;
        this.discount = discount;
        this.wallet_amount = wallet_amount;
        this.menus = menus;
    }*/

    public OrderList(String order_id, String unique_order_id, String total_price, String sub_price, String discount, String delivery, String wallet_amount, String delivery_time, String order_place_time, String delmsg, String deldone, String status, ArrayList<OrderMenu> menus) {
        this.order_id = order_id;
        this.unique_order_id = unique_order_id;
        this.total_price = total_price;
        this.sub_price = sub_price;
        this.discount = discount;
        this.delivery = delivery;
        this.wallet_amount = wallet_amount;
        this.delivery_time = delivery_time;
        this.order_place_time = order_place_time;
        this.delmsg = delmsg;
        this.deldone = deldone;
        this.status = status;
        this.menus = menus;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getUnique_order_id() {
        return unique_order_id;
    }

    public void setUnique_order_id(String unique_order_id) {
        this.unique_order_id = unique_order_id;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getSub_price() {
        return sub_price;
    }

    public void setSub_price(String sub_price) {
        this.sub_price = sub_price;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public String getWallet_amount() {
        return wallet_amount;
    }

    public void setWallet_amount(String wallet_amount) {
        this.wallet_amount = wallet_amount;
    }

    public String getDelivery_time() {
        return delivery_time;
    }

    public void setDelivery_time(String delivery_time) {
        this.delivery_time = delivery_time;
    }

    public String getOrder_place_time() {
        return order_place_time;
    }

    public void setOrder_place_time(String order_place_time) {
        this.order_place_time = order_place_time;
    }

    public String getDelmsg() {
        return delmsg;
    }

    public void setDelmsg(String delmsg) {
        this.delmsg = delmsg;
    }

    public String getDeldone() {
        return deldone;
    }

    public void setDeldone(String deldone) {
        this.deldone = deldone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<OrderMenu> getMenus() {
        return menus;
    }

    public void setMenus(ArrayList<OrderMenu> menus) {
        this.menus = menus;
    }
}
