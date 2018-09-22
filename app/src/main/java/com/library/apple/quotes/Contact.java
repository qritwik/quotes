package com.library.apple.quotes;

public class Contact {

    private int name;
    private String url;
    private String fav;


    public Contact() {
    }

    public Contact(int name, String url,String fav) {
        this.name = name;
        this.url = url;
        this.fav = fav;
    }

    public int getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getFav() {
        return fav;
    }
}
