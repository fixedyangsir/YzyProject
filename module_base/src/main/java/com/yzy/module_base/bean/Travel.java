package com.yzy.module_base.bean;

import android.os.Parcel;
import android.os.Parcelable;


public class Travel implements Parcelable {
    String name;
    String front_left;
    String front_right;
    String bottom_content;
    String bottom_type;
    int bottom_img;
    int image;

    public Travel(String name, String front_left, String front_right, String bottom_content, String bottom_type, int bottom_img, int image) {
        this.name = name;
        this.front_left = front_left;
        this.front_right = front_right;
        this.bottom_content = bottom_content;
        this.bottom_type = bottom_type;
        this.bottom_img = bottom_img;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFront_left() {
        return front_left;
    }

    public void setFront_left(String front_left) {
        this.front_left = front_left;
    }

    public String getFront_right() {
        return front_right;
    }

    public void setFront_right(String front_right) {
        this.front_right = front_right;
    }

    public String getBottom_content() {
        return bottom_content;
    }

    public void setBottom_content(String bottom_content) {
        this.bottom_content = bottom_content;
    }

    public String getBottom_type() {
        return bottom_type;
    }

    public void setBottom_type(String bottom_type) {
        this.bottom_type = bottom_type;
    }

    public int getBottom_img() {
        return bottom_img;
    }

    public void setBottom_img(int bottom_img) {
        this.bottom_img = bottom_img;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.front_left);
        dest.writeString(this.front_right);
        dest.writeString(this.bottom_content);
        dest.writeString(this.bottom_type);
        dest.writeInt(this.bottom_img);
        dest.writeInt(this.image);
    }

    protected Travel(Parcel in) {
        this.name = in.readString();
        this.front_left = in.readString();
        this.front_right = in.readString();
        this.bottom_content = in.readString();
        this.bottom_type = in.readString();
        this.bottom_img = in.readInt();
        this.image = in.readInt();
    }

    public static final Creator<Travel> CREATOR = new Creator<Travel>() {
        @Override
        public Travel createFromParcel(Parcel source) {
            return new Travel(source);
        }

        @Override
        public Travel[] newArray(int size) {
            return new Travel[size];
        }
    };
}
