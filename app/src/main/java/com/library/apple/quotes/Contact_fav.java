package com.library.apple.quotes;

public class Contact_fav {

    private String name;
    private String url;
    private String fav;


    public Contact_fav() {
    }

    public Contact_fav(String name, String url,String fav) {
        this.name = name;
        this.url = url;
        this.fav = fav;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getFav() {
        return fav;
    }
}
