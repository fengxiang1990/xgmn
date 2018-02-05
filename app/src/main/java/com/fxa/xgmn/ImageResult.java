package com.fxa.xgmn;

import java.io.Serializable;

public class ImageResult implements Serializable{

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
