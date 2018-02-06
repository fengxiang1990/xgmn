package com.fxa.xgmn;

import android.os.Parcel;
import android.os.Parcelable;


public class ImageResult implements Parcelable {

    public int id;

    public String url;

    public String type;

    public String name;

    public ImageResult() {

    }

    protected ImageResult(Parcel in) {
        id = in.readInt();
        url = in.readString();
        type = in.readString();
        name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(url);
        dest.writeString(type);
        dest.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ImageResult> CREATOR = new Creator<ImageResult>() {
        @Override
        public ImageResult createFromParcel(Parcel in) {
            return new ImageResult(in);
        }

        @Override
        public ImageResult[] newArray(int size) {
            return new ImageResult[size];
        }
    };

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
