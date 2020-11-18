package com.rndtechnosoft.fooddaily.Model;

public class Strip {

    String id, title, link_type, bg_color, strip_link;

    public Strip(String id, String title, String link_type, String bg_color, String strip_link) {
        this.id = id;
        this.title = title;
        this.link_type = link_type;
        this.bg_color = bg_color;
        this.strip_link = strip_link;
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

    public String getLink_type() {
        return link_type;
    }

    public void setLink_type(String link_type) {
        this.link_type = link_type;
    }

    public String getBg_color() {
        return bg_color;
    }

    public void setBg_color(String bg_color) {
        this.bg_color = bg_color;
    }

    public String getStrip_link() {
        return strip_link;
    }

    public void setStrip_link(String strip_link) {
        this.strip_link = strip_link;
    }
}
