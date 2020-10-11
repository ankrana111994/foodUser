package com.rndtechnosoft.fooddaily.Model;


public class ItemNotification {
    String id;
    String notificationTitle;
    String notificationMessage;
    String notificationImage;

    public ItemNotification(String id, String notificationTitle, String notificationMessage, String notificationImage) {
        this.id = id;
        this.notificationTitle = notificationTitle;
        this.notificationMessage = notificationMessage;
        this.notificationImage = notificationImage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNotificationTitle() {
        return notificationTitle;
    }

    public void setNotificationTitle(String title){
        this.notificationTitle=title;
    }

    public String getNotificationMessage() {
        return notificationMessage;
    }

    public void setNotificationMessage(String message){
        this.notificationMessage=message;
    }

    public String getNotificationImage() {
        return notificationImage;
    }

    public void setNotificationImage(String image){
        this.notificationImage=image;
    }
}
