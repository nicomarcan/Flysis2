package com.example.nmarcantonio.flysys2;

/**
 * Created by Nicolas on 11/21/2016.
 */

public class FlickrImg {


    private String id;
    private String server;
    private String secret;
    private String farm;

    public FlickrImg(String id, String server, String secret, String farm) {
        this.id = id;
        this.server = server;
        this.secret = secret;
        this.farm = farm;
    }

    public String getFarm() {
        return farm;
    }

    public String getId() {
        return id;
    }

    public String getServer() {
        return server;
    }

    public String getSecret() {
        return secret;
    }
}
