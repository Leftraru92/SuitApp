package com.example.suitapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Province implements Parcelable {
    private int id;
    private String name;

    public Province(int id, String name){
        this.id = id;
        this.name = name;
    }

    protected Province(Parcel in) {
        id = in.readInt();
        name = in.readString();
    }

    public static final Creator<Province> CREATOR = new Creator<Province>() {
        @Override
        public Province createFromParcel(Parcel in) {
            return new Province(in);
        }

        @Override
        public Province[] newArray(int size) {
            return new Province[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
    }
}
