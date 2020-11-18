package com.rndtechnosoft.fooddaily.Model;

public class Payment {

    String id, title, method_key, image;

    public Payment(String id, String title, String method_key, String image) {
        this.id = id;
        this.title = title;
        this.method_key = method_key;
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

    public String getMethod_key() {
        return method_key;
    }

    public void setMethod_key(String method_key) {
        this.method_key = method_key;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
