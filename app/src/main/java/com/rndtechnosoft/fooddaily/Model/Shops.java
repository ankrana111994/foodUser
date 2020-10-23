package com.rndtechnosoft.fooddaily.Model;


public class Shops {

    private String id;
    private String restaurantTitle;
    private String restaurantImage;
    private String restaurantAddress;
    private String restaurantDescription;
    private String rating;
    private String deliveryCharges;
    private String costForTwo;
    private String openingTime;
    private String closingTime;

    public Shops(String id, String restaurantTitle, String restaurantImage, String restaurantAddress,
                 String restaurantDescription, String rating, String deliveryCharges,
                 String costForTwo, String openingTime, String closingTime) {
        this.id = id;
        this.restaurantTitle = restaurantTitle;
        this.restaurantImage = restaurantImage;
        this.restaurantAddress = restaurantAddress;
        this.restaurantDescription = restaurantDescription;
        this.rating = rating;
        this.deliveryCharges = deliveryCharges;
        this.costForTwo = costForTwo;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRestaurantTitle() {
        return restaurantTitle;
    }

    public void setRestaurantTitle(String restaurantTitle) {
        this.restaurantTitle = restaurantTitle;
    }

    public String getRestaurantImage() {
        return restaurantImage;
    }

    public void setRestaurantImage(String restaurantImage) {
        this.restaurantImage = restaurantImage;
    }

    public String getRestaurantAddress() {
        return restaurantAddress;
    }

    public void setRestaurantAddress(String restaurantAddress) {
        this.restaurantAddress = restaurantAddress;
    }

    public String getRestaurantDescription() {
        return restaurantDescription;
    }

    public void setRestaurantDescription(String restaurantDescription) {
        this.restaurantDescription = restaurantDescription;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getDeliveryCharges() {
        return deliveryCharges;
    }

    public void setDeliveryCharges(String deliveryCharges) {
        this.deliveryCharges = deliveryCharges;
    }

    public String getCostForTwo() {
        return costForTwo;
    }

    public void setCostForTwo(String costForTwo) {
        this.costForTwo = costForTwo;
    }

    public String getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(String openingTime) {
        this.openingTime = openingTime;
    }

    public String getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(String closingTime) {
        this.closingTime = closingTime;
    }
}