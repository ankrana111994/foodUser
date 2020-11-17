package com.rndtechnosoft.fooddaily.Model;

public class CouponList {

    String id, title, tnc, min_order, exp_date, coupon_code, image;

    public CouponList(String id, String title, String tnc, String min_order, String exp_date, String coupon_code, String image) {
        this.id = id;
        this.title = title;
        this.tnc = tnc;
        this.min_order = min_order;
        this.exp_date = exp_date;
        this.coupon_code = coupon_code;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTnc() {
        return tnc;
    }

    public void setTnc(String tnc) {
        this.tnc = tnc;
    }

    public String getMin_order() {
        return min_order;
    }

    public void setMin_order(String min_order) {
        this.min_order = min_order;
    }

    public String getExp_date() {
        return exp_date;
    }

    public void setExp_date(String exp_date) {
        this.exp_date = exp_date;
    }

    public String getCoupon_code() {
        return coupon_code;
    }

    public void setCoupon_code(String coupon_code) {
        this.coupon_code = coupon_code;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
