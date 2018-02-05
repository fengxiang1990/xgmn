package com.fxa.xgmn;

public class ImageResult {

    public int id;

    public String url;

    public String type;

    public String name;

    @Override
    public String toString() {
        return "ImageResult{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
