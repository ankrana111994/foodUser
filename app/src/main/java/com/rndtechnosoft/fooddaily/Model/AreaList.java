package com.rndtechnosoft.fooddaily.Model;

public class AreaList {

    String id, name, delivery;

    public AreaList(String id, String name, String delivery) {
        this.id = id;
        this.name = name;
        this.delivery = delivery;
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

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }
}
