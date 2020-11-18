package com.rndtechnosoft.fooddaily.Model;

import java.util.ArrayList;

public class HomeCat {

    String title,link;
    ArrayList<HomeCategoryList> homeCategoryLists;

    public HomeCat(String title, String link, ArrayList<HomeCategoryList> homeCategoryLists) {
        this.title = title;
        this.link = link;
        this.homeCategoryLists = homeCategoryLists;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public ArrayList<HomeCategoryList> getHomeCategoryLists() {
        return homeCategoryLists;
    }

    public void setHomeCategoryLists(ArrayList<HomeCategoryList> homeCategoryLists) {
        this.homeCategoryLists = homeCategoryLists;
    }
}
